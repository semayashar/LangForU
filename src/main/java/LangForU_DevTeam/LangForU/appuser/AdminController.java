package LangForU_DevTeam.LangForU.appuser;

import LangForU_DevTeam.LangForU.blog.Blog;
import LangForU_DevTeam.LangForU.blog.BlogService;
import LangForU_DevTeam.LangForU.contactRequest.ContactRequest;
import LangForU_DevTeam.LangForU.contactRequest.ContactRequestService;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.email.EmailService;
import LangForU_DevTeam.LangForU.finalexam.FinalExam;
import LangForU_DevTeam.LangForU.finalexam.FinalExamService;
import LangForU_DevTeam.LangForU.lections.Lection;
import LangForU_DevTeam.LangForU.lections.LectionService;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequest;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Контролер, който управлява всички функционалности на административния панел.
 * Обработва заявки под пътя "/admin" и изисква администраторски права.
 */
@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AppUserService appUserService;
    private final UserCourseRequestService userCourseRequestService;
    private final ContactRequestService contactRequestService;
    private final EmailService emailService;
    private final BlogService blogService;
    private final LectionService lectionService;
    private final CourseService coursesServices;
    private final FinalExamService finalExamService;

    @GetMapping("/users-list")
    @ResponseBody
    public List<AppUser> getEnabledUsersWithRoleUser() {
        List<AppUser> users = appUserService.getEnabledUsersWithRoleUser();
        if (users.isEmpty()) {
            throw new RuntimeException("Няма налични активирани потребители с роля USER.");
        }
        return users;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<AppUser> users = appUserService.getAllUsersByRole(AppUserRole.USER);
        if (users.isEmpty()) {
            model.addAttribute("message", "Няма налични потребители.");
            model.addAttribute("success", false);
        } else {
            model.addAttribute("users", users);
        }
        return "user/users";
    }

    @GetMapping("/user/view")
    public String viewUser(Model model, HttpServletRequest request) {
        try {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if (csrfToken == null) {
                model.addAttribute("message", "Грешка при извличането на CSRF токен.");
                return "notifications/error";
            }
            model.addAttribute("_csrf", csrfToken);
            return "user/viewUser";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при зареждането на страницата.");
            return "notifications/error";
        }
    }

    @GetMapping("/user/view/{id}")
    public String viewUserDetails(@PathVariable("id") Long id, Model model) {
        try {
            AppUser user = appUserService.findUserById(id);
            if (user == null) {
                model.addAttribute("message", "Потребителят не е намерен.");
                return "notifications/error";
            }
            model.addAttribute("user", user);
            return "user/viewUser";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при извличането на данни за потребителя.");
            return "notifications/error";
        }
    }

    /**
     * ПРОМЯНА: Методът е променен от GET на POST, тъй като изтриването е променяща операция.
     * Изтрива потребител по ID.
     * @param id ID на потребителя за изтриване.
     * @param redirectAttributes Атрибути за предаване на съобщения след пренасочване.
     * @return Пренасочване към списъка с потребители.
     */
    @PostMapping("/deleteUser/{id}")
    public String deleteUserById(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            appUserService.deleteUserAndRelatedRecords(id);
            redirectAttributes.addFlashAttribute("message", "Потребителят е изтрит успешно.");
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Грешка при изтриването на потребителя.");
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/admin/users";
    }

    /**
     * ПРОМЯНА: Методът е променен от GET на POST, тъй като деактивирането е променяща операция.
     * Деактивира потребителски акаунт по ID.
     * @param id ID на потребителя за деактивиране.
     * @param redirectAttributes Атрибути за пренасочване.
     * @return Пренасочване към страницата с детайли на потребителя.
     */
    @PostMapping("/disableUser/{id}")
    public String disableUserById(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            appUserService.disableUserById(id);
            redirectAttributes.addFlashAttribute("message", "Потребителят е деактивиран успешно.");
            redirectAttributes.addFlashAttribute("success", true);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", "Грешка при деактивирането на потребителя: " + e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/admin/user/view/" + id;
    }

    /**
     * ПРОМЯНА: Методът е променен от GET на POST, тъй като активирането е променяща операция.
     * Активира потребителски акаунт по ID.
     * @param id ID на потребителя за активиране.
     * @param redirectAttributes Атрибути за пренасочване.
     * @return Пренасочване към страницата с детайли на потребителя.
     */
    @PostMapping("/enableUser/{id}")
    public String enableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appUserService.enableUserById(id);
            redirectAttributes.addFlashAttribute("message", "Потребителят е активиран успешно.");
            redirectAttributes.addFlashAttribute("success", true);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", "Грешка при активирането на потребителя: " + e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/admin/user/view/" + id;
    }

    /**
     * ПРОМЯНА: Методът е променен от GET на POST, тъй като изпращането на заявка е променяща операция.
     * Изпраща заявка до потребител да стане администратор.
     * @param id ID на потребителя, до когото се изпраща заявка.
     * @param redirectAttributes Атрибути за пренасочване.
     * @return Пренасочване към страницата с детайли на потребителя.
     */
    @PostMapping("/sendUserAdminRequest/{id}")
    public String sendUserAdminRequest(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        AppUser user = appUserService.findUserById(id);
        if (user != null) {
            appUserService.sendAdminRequest(user);
            redirectAttributes.addFlashAttribute("message", "Заявката за администратор е изпратена успешно.");
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("message", "Потребителят не е намерен.");
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/admin/user/view/" + id;
    }

    @GetMapping("/confirmAdmin")
    public String confirmAdmin(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        int result = appUserService.confirmAdmin(token);
        if (result == 1) {
            redirectAttributes.addFlashAttribute("message", "Администраторът е потвърден успешно.");
            redirectAttributes.addFlashAttribute("success", true);
            return "notifications/confirmationSuccess";
        } else {
            redirectAttributes.addFlashAttribute("message", "Грешка при потвърждаването на администратор.");
            redirectAttributes.addFlashAttribute("success", false);
            return "notifications/confirmationFailure";
        }
    }

    @GetMapping("/unconfirmedUsers")
    public String viewUnconfirmedUsers(Model model) {
        List<UserCourseRequest> requests = userCourseRequestService.getAllUnconfirmedUsers();
        if (requests.isEmpty()) {
            model.addAttribute("message", "Няма непотвърдени потребители.");
            model.addAttribute("success", false);
        } else {
            model.addAttribute("requests", requests);
        }
        return "user/unconfirmedUsers";
    }

    /**
     * ПРОМЯНА: Методът е променен от GET на POST, тъй като потвърждаването на заявка е променяща операция.
     * Потвърждава заявка за записване в курс.
     * @param requestID ID на заявката за потвърждаване.
     * @param redirectAttributes Атрибути за пренасочване.
     * @return Пренасочване към списъка с непотвърдени заявки.
     */
    @PostMapping("/request/confirm/{requestID}")
    public String confirmUserCourseRequest(@PathVariable Long requestID, RedirectAttributes redirectAttributes) {
        try {
            userCourseRequestService.confirmUserCourseRequest(requestID);
            redirectAttributes.addFlashAttribute("message", "Заявката за курс е потвърдена успешно.");
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Грешка при потвърждаването на заявката за курс.");
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/admin/unconfirmedUsers";
    }

    @GetMapping("/contactRequests")
    public String getAllContactRequests(Model model) {
        try {
            List<ContactRequest> contactRequests = contactRequestService.getAllRequests();
            if (contactRequests.isEmpty()) {
                model.addAttribute("message", "Няма налични заявки за контакт.");
                model.addAttribute("success", false);
            } else {
                model.addAttribute("contactRequests", contactRequests);
            }
            return "admin/contactRequests";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при зареждането на заявките за контакт.");
            model.addAttribute("success", false);
            return "notifications/error";
        }
    }

    @GetMapping("/contactRequests/view/{id}")
    public String getContactRequestDetails(@PathVariable("id") Long id, Model model) {
        try {
            Optional<ContactRequest> request = contactRequestService.getRequestById(id);
            if (request.isEmpty()) {
                model.addAttribute("message", "Заявката не е намерена.");
                return "notifications/error";
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUser admin = (AppUser) authentication.getPrincipal();

            model.addAttribute("request", request.get());
            model.addAttribute("adminName", admin.getName());

            return "admin/requestDetails";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при извличането на данни за заявката.");
            model.addAttribute("success", false);
            return "notifications/error";
        }
    }

    @PostMapping("/sendEmail")
    public String sendEmail(
            @RequestParam("id") Long id,
            @RequestParam("middleContent") String middleContent,
            @RequestParam("adminName") String adminName,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Optional<ContactRequest> requestOpt = contactRequestService.getRequestById(id);
            if (requestOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Заявката не е намерена.");
                return "redirect:/admin/contactRequests";
            }

            ContactRequest request = requestOpt.get();
            String userEmail = request.getEmail();
            String userName = request.getName();

            emailService.sendEmail(userEmail, middleContent, adminName, userName);
            contactRequestService.deleteRequestById(id);

            redirectAttributes.addFlashAttribute("successMessage", "Имейлът беше изпратен успешно и заявката е изтрита!");
            return "redirect:/admin/contactRequests";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Възникна грешка при изпращането на имейла.");
            return "redirect:/admin/contactRequests";
        }
    }

    @GetMapping("/blogs")
    public String listBlogs(Model model) {
        try {
            List<Blog> blogs = blogService.findAllBlogs();
            if (blogs.isEmpty()) {
                model.addAttribute("message", "Няма налични блогове.");
                model.addAttribute("success", false);
            } else {
                model.addAttribute("blogs", blogs);
            }
            return "blog/blogList";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при зареждането на блоговете.");
            model.addAttribute("success", false);
            return "notifications/error";
        }
    }

    @GetMapping("/lections")
    public String listLections(Model model) {
        try {
            List<Lection> lections = lectionService.getAllLections();
            if (lections.isEmpty()) {
                model.addAttribute("message", "Няма налични лекции.");
                model.addAttribute("success", false);
            } else {
                model.addAttribute("lections", lections);
            }
            return "lections/lectionList";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при зареждането на лекциите.");
            model.addAttribute("success", false);
            return "notifications/error";
        }
    }

    @GetMapping("/courses")
    public String listCourses(Model model) {
        try {
            List<Course> courses = coursesServices.getAllActiveCourses();
            if (courses.isEmpty()) {
                model.addAttribute("message", "Няма налични курсове.");
                model.addAttribute("success", false);
            } else {
                model.addAttribute("courses", courses);
            }
            return "course/courseList";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при зареждането на курсовете.");
            model.addAttribute("success", false);
            return "notifications/error";
        }
    }

    @GetMapping("/final-exams")
    public String showFinalExamsList(Model model) {
        try {
            List<FinalExam> finalExams = finalExamService.getAllFinalExams();
            if (finalExams.isEmpty()) {
                model.addAttribute("message", "Няма налични финални изпити.");
                model.addAttribute("success", false);
            } else {
                model.addAttribute("finalExams", finalExams);
            }
            return "final-exams/finalExamsList";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при зареждането на финалните изпити.");
            model.addAttribute("success", false);
            return "notifications/error";
        }
    }

    @GetMapping("/dashboard")
    public String adminPanel(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof AppUser)) {
                model.addAttribute("message", "Не сте влезли като администратор.");
                model.addAttribute("success", false);
                return "notifications/error";
            }
            return "admin/adminDashboard";
        } catch (Exception e) {
            model.addAttribute("message", "Грешка при зареждането на административния панел.");
            model.addAttribute("success", false);
            return "notifications/error";
        }
    }
}