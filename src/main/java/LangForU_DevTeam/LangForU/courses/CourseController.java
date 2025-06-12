package LangForU_DevTeam.LangForU.courses;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRepository;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.lections.Lection;
import LangForU_DevTeam.LangForU.lections.LectionService;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequest;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Контролер, който управлява всички HTTP заявки, свързани с курсовете.
 * Включва показване на списък с курсове, създаване, редактиране, изтриване
 * и записване на потребители за курсове.
 */
@Controller
@RequestMapping("/courses")
public class CourseController {

    //<editor-fold desc="Dependencies">
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final AppUserRepository appUserRepository;
    private final LectionService lectionService;
    private final UserCourseRequestService userCourseRequestService;
    //</editor-fold>

    /**
     * Конструктор за инжектиране на зависимости (Dependency Injection).
     */
    @Autowired
    public CourseController(CourseService courseService, AppUserService appUserService, CourseRepository courseRepository, AppUserRepository appUserRepository, LectionService lectionService, UserCourseRequestService userCourseRequestService) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.appUserRepository = appUserRepository;
        this.lectionService = lectionService;
        this.userCourseRequestService = userCourseRequestService;
    }

    /**
     * Показва страница със списък на всички курсове.
     * Позволява филтриране по език или по съдържание в описанието.
     * @param searchLanguage Низ за търсене по език (незадължителен).
     * @param searchDescription Низ за търсене по описание (незадължителен).
     * @param model Модел за подаване на данни към изгледа.
     * @return Името на шаблона 'courses'.
     */
    @GetMapping
    public String getAllCourses(
            @RequestParam(value = "searchLanguage", required = false) String searchLanguage,
            @RequestParam(value = "searchDescription", required = false) String searchDescription,
            Model model) {
        List<Course> courses;
        if (searchLanguage != null && !searchLanguage.isEmpty()) {
            courses = courseService.getCoursesByLanguage(searchLanguage);
        } else if (searchDescription != null && !searchDescription.isEmpty()) {
            courses = courseService.getCoursesByDescription(searchDescription);
        } else {
            courses = courseService.getAllCourses();
        }

        model.addAttribute("courses", courses);
        return "courses";
    }

    /**
     * Показва формата за добавяне на нов курс.
     * @param model Модел, към който се добавя празен Course обект.
     * @return Името на шаблона 'course/addCourse'.
     */
    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/addCourse";
    }

    /**
     * Обработва POST заявка за добавяне на нов курс.
     * @param course Обект Course, създаден от данните във формата.
     * @param result Обект за съхранение на грешки от валидацията.
     * @param model Модел за подаване на данни към изгледа.
     * @param startDate Начална дата като текст.
     * @param endDate Крайна дата като текст.
     * @return Пренасочване към списъка с курсове при успех или връщане към формата при грешка.
     */
    @PostMapping("/add")
    public String addCourse(@ModelAttribute("course") Course course, BindingResult result, Model model,
                            @RequestParam("startDate") String startDate,
                            @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Ръчно парсване на датите, тъй като идват като String.
        try {
            course.setStartDate(LocalDate.parse(startDate, formatter));
            course.setEndDate(LocalDate.parse(endDate, formatter));
        } catch (DateTimeParseException e) {
            result.rejectValue("startDate", "error.course", "Невалиден формат на дата. Моля, използвайте dd/MM/yyyy.");
        }

        if (result.hasErrors()) {
            return "course/addCourse";
        }

        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    /**
     * Показва детайлна информация за избран курс, включително списък с лекциите към него.
     * @param id ID на курса.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'course/viewCourse' или страница за грешка.
     */
    @GetMapping("/view/{id}")
    public String viewCourseDetails(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);
        if (course == null) {
            return "notifications/error";
        }
        List<Lection> lections = lectionService.getLectionsByCourseId(course.getId());
        model.addAttribute("course", course);
        model.addAttribute("lections", lections);
        return "course/viewCourse";
    }

    /**
     * Изтрива курс по ID. Забележка: използва се GET заявка, което не е стандартна практика за изтриване.
     * @param id ID на курса за изтриване.
     * @return Пренасочване към списъка с курсове в административния панел.
     */
    @GetMapping("/delete/{id}")
    public String softDeleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourseById(id);
        return "redirect:/admin/courses";
    }

    /**
     * Показва формата за записване за курс.
     * @param id ID на курса.
     * @param loggedInUser Текущо логнатият потребител, инжектиран от Spring Security.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'course/signup'.
     */
    @GetMapping("/signup/{id}")
    public String signupCourse(@PathVariable Long id, @AuthenticationPrincipal AppUser loggedInUser, Model model) {
        Course course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("pin", ""); // Подава празен pin към формата
        return "course/signup";
    }

    /**
     * Обработва POST заявката за записване в курс.
     * @param id ID на курса.
     * @param pin ЕГН на потребителя.
     * @param citizenship Гражданство на потребителя.
     * @param loggedInUser Текущо логнатият потребител.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Пренасочване към страница за успешно записване или обратно към формата при грешка.
     */
    @PostMapping("/signup/{id}")
    public String submitSignup(
            @PathVariable Long id,
            @RequestParam String pin,
            @RequestParam String citizenship,
            @AuthenticationPrincipal AppUser loggedInUser,
            RedirectAttributes redirectAttributes) {

        if (userCourseRequestService.existsByUserIdAndCourseId(loggedInUser.getId(), id)) {
            redirectAttributes.addFlashAttribute("error", "Вече сте записани за този курс.");
            return "redirect:/courses/signup/" + id;
        }

        try {
            Course course = courseService.findCourseById(id);
            if (course.getPrice() == 0) { // Ако курсът е безплатен
                userCourseRequestService.createRequest(loggedInUser.getId(), id, pin, citizenship);
                return "redirect:/profile"; // Пренасочва директно към профила
            }

            UserCourseRequest request = userCourseRequestService.createRequest(loggedInUser.getId(), id, pin, citizenship);
            return "redirect:/courses/signedup/successfully?userId=" + loggedInUser.getId() +
                    "&courseId=" + id + "&requestId=" + request.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Неуспешно записване за курса: " + e.getMessage());
            return "redirect:/courses/signup/" + id;
        }
    }

    /**
     * Показва страница за потвърждение на успешно записване.
     * @param userId ID на потребителя.
     * @param courseId ID на курса.
     * @param requestId ID на създадената заявка за записване.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'notifications/signedupSuccessfully'.
     */
    @GetMapping("/signedup/successfully")
    public String signupSuccessfully(@RequestParam Long userId, @RequestParam Long courseId, @RequestParam Long requestId, Model model) {
        try {
            AppUser user = appUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
            UserCourseRequest userCourseRequest = userCourseRequestService.findById(requestId);

            model.addAttribute("user", user);
            model.addAttribute("course", course);
            model.addAttribute("userCourseRequest", userCourseRequest);
            return "notifications/signedupSuccessfully";
        } catch (Exception e) {
            model.addAttribute("error", "Възникна грешка при обработка на заявката.");
            return "notifications/error";
        }
    }

    /**
     * Показва детайлна страница за курс (алтернативен ендпойнт).
     * @param id ID на курса.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'course/courseDetails' или страница за грешка.
     */
    @GetMapping("/course/details/{id}")
    public String getCourseDetails(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);
        if (course != null) {
            model.addAttribute("course", course);
            return "course/courseDetails";
        } else {
            return "error/404";
        }
    }

    /**
     * Показва формата за редактиране на съществуващ курс.
     * @param id ID на курса за редактиране.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'course/editCourse' или страница за грешка.
     */
    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);
        if (course == null) {
            return "notifications/error";
        }
        model.addAttribute("course", course);
        return "course/editCourse";
    }

    /**
     * Обработва POST заявка за редактиране на съществуващ курс.
     * @param id ID на курса, който се редактира.
     * @param course Обект с обновените данни.
     * @param result Обект за грешки от валидацията.
     * @param model Модел за подаване на данни.
     * @param startDate Начална дата като текст.
     * @param endDate Крайна дата като текст.
     * @return Пренасочване към списъка с курсове при успех или връщане към формата при грешка.
     */
    @PostMapping("/edit/{id}")
    public String editCourse(
            @PathVariable("id") Long id,
            @ModelAttribute("course") Course course,
            BindingResult result,
            Model model,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            course.setStartDate(LocalDate.parse(startDate, formatter));
            course.setEndDate(LocalDate.parse(endDate, formatter));
        } catch (DateTimeParseException e) {
            result.rejectValue("startDate", "error.course", "Невалиден формат на дата. Моля, използвайте dd/MM/yyyy.");
        }

        if (result.hasErrors()) {
            return "course/editCourse";
        }

        course.setId(id); // Задава ID-то, за да се обнови съществуващият запис.
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    /**
     * Показва списък със заявките за записване в курсове на текущо логнатия потребител.
     * @param principal Обект, представляващ текущия потребител.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'user/userCourseRequests'.
     */
    @GetMapping("/requests")
    public String getUserCourseRequests(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        AppUser user = appUserRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserCourseRequest> userCourseRequests = userCourseRequestService.getRequestsByUserId(user.getId());
        model.addAttribute("userCourseRequests", userCourseRequests);
        return "user/userCourseRequests";
    }
}