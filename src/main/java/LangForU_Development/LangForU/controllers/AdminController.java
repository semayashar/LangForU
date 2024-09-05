package LangForU_Development.LangForU.controllers;

import java.util.List;
import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.appuser.AppUserService;
import LangForU_Development.LangForU.singUpForCourse.UserCourseRequest;
import LangForU_Development.LangForU.singUpForCourse.UserCourseRequestService;
import lombok.AllArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AppUserService appUserService;
    private final UserCourseRequestService userCourseRequestService;

    @GetMapping("/users-list")
    @ResponseBody
    public List<AppUser> getEnabledUsersWithRoleUser() {
        return appUserService.getEnabledUsersWithRoleUser();
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", appUserService.getEnabledUsersWithRoleUser());
        return "admin/users"; // Ensure this matches the actual path to your Thymeleaf template
    }

    @GetMapping("/user/view")
    public String viewUser(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);
        return "admin/viewUser";
    }

    @GetMapping("/user/view/{id}")
    public String viewUserDetails(@PathVariable("id") Long id, Model model) {
        AppUser user = appUserService.findUserById(id);
        if (user == null) {
            return "errorPage"; // Ensure this template exists or handle the error differently
        }
        model.addAttribute("user", user);
        return "admin/viewUser"; // The Thymeleaf template name
    }

    @GetMapping("/deleteUser/{id}")
    public String softDeleteAccount(@PathVariable("id") Long id) {
        System.out.println("Attempting to delete user with ID: " + id);
        appUserService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/sendUserAdminRequest/{id}")
    public String sendUserAdminRequest(@PathVariable("id") Long id) {
        AppUser user = appUserService.findUserById(id);
        if (user != null) {
            appUserService.sendAdminRequest(user);
        }
        return "redirect:/admin/user/view/" + id;
    }

    @GetMapping("/confirmAdmin")
    public String confirmAdmin(@RequestParam("token") String token) {
        int result = appUserService.confirmAdmin(token);
        if (result == 1) {
            return "admin/confirmationSuccess";
        } else {
            return "admin/confirmationFailure";
        }
    }

    @GetMapping("/unconfirmed-users")
    public String viewUnconfirmedUsers(Model model) {
        List<UserCourseRequest> requests = userCourseRequestService.getAllUnconfirmedUsers();
        model.addAttribute("requests", requests);
        return "admin/unconfirmedUsers";
    }

    @GetMapping("/request/confirm/{requestID}")
    public String confirmUserCourseRequest(@PathVariable Long requestID) {
        userCourseRequestService.confirmUserCourseRequest(requestID);
        return "redirect:/admin/unconfirmed-users";
    }
}
