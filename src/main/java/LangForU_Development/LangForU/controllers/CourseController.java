package LangForU_Development.LangForU.controllers;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.appuser.AppUserRepository;
import LangForU_Development.LangForU.appuser.AppUserService;
import LangForU_Development.LangForU.courses.Course;
import LangForU_Development.LangForU.courses.CourseRepository;
import LangForU_Development.LangForU.courses.CourseService;
import LangForU_Development.LangForU.singUpForCourse.UserCourseRequest;
import LangForU_Development.LangForU.singUpForCourse.UserCourseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final AppUserRepository appUserRepository;
    private final UserCourseRequestService userCourseRequestService;

    @Autowired
    public CourseController(CourseService courseService, AppUserService appUserService, CourseRepository courseRepository, AppUserRepository appUserRepository, UserCourseRequestService userCourseRequestService) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.appUserRepository = appUserRepository;
        this.userCourseRequestService = userCourseRequestService;
    }

    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/addCourse";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute("course") Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            return "course/addCourse";
        }
        courseService.saveCourse(course);
        return "redirect:/courses";  // Redirect to the list of courses
    }

    @GetMapping
    public String getAllCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/view/{id}")
    public String viewCourseDetails(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);
        if (course == null) {
            return "errorPage";
        }
        model.addAttribute("course", course);
        return "viewCourse"; // Make sure this is correctly named and located in the templates
    }

    @GetMapping("/delete/{id}")
    public String softDeleteCourse(@PathVariable("id") Long id) {
        System.out.println("Attempting to delete course with ID: " + id);
        courseService.deleteCourseById(id);
        return "redirect:/courses";
    }

    @GetMapping("/signup/{id}")
    public String signupCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser loggedInUser,
            Model model) {

        Course course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("pin", new String());
        return "course/signup"; // Thymeleaf template name
    }

    @PostMapping("/signup/{id}")
    public String submitSignup(
            @PathVariable Long id,
            @RequestParam String pin,
            @RequestParam String citizenship,
            @AuthenticationPrincipal AppUser loggedInUser,
            RedirectAttributes redirectAttributes) {

        try {
            userCourseRequestService.createRequest(loggedInUser.getId(), id, pin, citizenship);
            redirectAttributes.addFlashAttribute("message", "Вие сте записан на този курс");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Неуспешно записване на курса.");
        }
        return "redirect:/courses/signup/" + id;
    }

    @GetMapping("/signedup/successfully")
    public String signupSuccessfully(@RequestParam Long userId, @RequestParam Long courseId, @RequestParam Long requestId, Model model) {
        try {
            AppUser user = appUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            UserCourseRequest userCourseRequest = userCourseRequestService.findById(requestId);

            model.addAttribute("user", user);
            model.addAttribute("course", course);
            model.addAttribute("userCourseRequest", userCourseRequest);

            return "course/singedupSuccessfully";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while processing your request.");
            return "errorPage"; // Or any other error handling page
        }
    }

    @GetMapping("/course/details/{id}")
    public String getCourseDetails(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);
        if (course != null) {
            model.addAttribute("course", course);
            return "course/courseDetails"; // Ensure this template exists
        } else {
            return "error/404"; // Redirect to an error page if course not found
        }
    }


}
