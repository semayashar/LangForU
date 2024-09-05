package LangForU_Development.LangForU.controllers;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.appuser.AppUserService;
import LangForU_Development.LangForU.courses.Course;
import LangForU_Development.LangForU.courses.CourseService;
import LangForU_Development.LangForU.registration.RegistrationRequest;
import LangForU_Development.LangForU.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final CourseService courseService;
    private final AppUserService userService;
    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password) {
        AppUser user = userService.authenticate(email, password);
        if (user != null) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("user", user);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "Invalid email or password");
            return modelAndView;
        }
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        AppUser user = (AppUser) userService.loadUserByUsername(email);
        if (user == null) {
            return "errorPage"; // Or some error handling page
        }
        model.addAttribute("user", user);

        return "profile";
    }

    @GetMapping("/index")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        model.addAttribute("authentication", authentication);
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest") RegistrationRequest request, HttpSession session) {
        try {
            registrationService.register(request);
            session.setAttribute("registrationSuccess", true);
            return "redirect:/registrationSuccess";
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/registrationSuccess")
    public String registrationSuccess(HttpSession session) {
        Boolean registrationSuccess = (Boolean) session.getAttribute("registrationSuccess");

        if (registrationSuccess != null && registrationSuccess) {
            session.removeAttribute("registrationSuccess");
            return "registrationSuccess";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/registration/confirm")
    public String confirm(@RequestParam("token") String token, Model model, HttpSession session) {
        try {
            String result = registrationService.confirmToken(token);
            if ("confirmed".equals(result)) {
                session.setAttribute("confirmedRegistration", true);
                return "redirect:/confirmedRegistration";
            } else {
                model.addAttribute("error", "Invalid or expired token");
                return "errorPage";
            }
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/confirmedRegistration")
    public String confirmedRegistration(HttpSession session) {
        Boolean confirmedRegistration = (Boolean) session.getAttribute("confirmedRegistration");

        if (confirmedRegistration != null && confirmedRegistration) {
            session.removeAttribute("confirmedRegistration");
            return "confirmedRegistration";
        } else {
            return "redirect:/";
        }
    }
}