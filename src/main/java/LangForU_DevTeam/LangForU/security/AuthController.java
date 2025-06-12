package LangForU_DevTeam.LangForU.security;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.registration.RegistrationRequest;
import LangForU_DevTeam.LangForU.registration.RegistrationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Контролер, който управлява всички HTTP заявки, свързани с автентикация и регистрация.
 * Обработва показването на страниците за вход и регистрация, както и обработката на
 * подадените от тях данни и процеса по потвърждение на имейл.
 */
@Controller
@RequestMapping // Този контролер обработва заявки към основния път (root path).
public class AuthController {

    private final RegistrationService registrationService;
    private final AppUserService userService;

    /**
     * Конструктор за инжектиране на зависимости.
     */
    public AuthController(RegistrationService registrationService, AppUserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    /**
     * Обработва GET заявки за показване на страницата за вход.
     * @param authentication Обект на текущата автентикация (ако има такава).
     * @param error Параметър от URL, указващ грешка при вход.
     * @param session HTTP сесия, използвана за съхранение на съобщения за грешки.
     * @param model Модел за подаване на данни към изгледа.
     * @return Името на шаблона 'login' или пренасочване, ако потребителят вече е логнат.
     */
    @GetMapping("/login")
    public String loginPage(
            @AuthenticationPrincipal Authentication authentication,
            @RequestParam(value = "error", required = false) String error,
            HttpSession session,
            Model model) {

        // Ако потребителят вече е логнат, го пренасочваме към профила му.
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/profile";
        }

        // Показва грешка, ако е подаден параметър 'error' (стандартно поведение на Spring Security).
        if (error != null) {
            model.addAttribute("error", "Грешен имейл или парола. Моля, опитайте отново!");
        }

        // Показва грешка, ако има такава, записана в сесията (от нашата персонализирана логика).
        String sessionError = (String) session.getAttribute("error");
        if (sessionError != null) {
            model.addAttribute("error", sessionError);
            session.removeAttribute("error"); // Изтрива грешката от сесията, след като е показана.
        }

        return "login";
    }

    /**
     * Обработва POST заявка за вход.
     * Забележка: Този метод изглежда е персонализирана имплементация, която може да не се използва,
     * ако Spring Security е конфигуриран да обработва /login POST заявки автоматично.
     * @param email Подаденият имейл.
     * @param password Подадената парола.
     * @param session HTTP сесия.
     * @return Пренасочване към профила при успех или обратно към страницата за вход при грешка.
     */
    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {

        AppUser user = userService.authenticate(email, password);

        if (user != null) {
            if (user.isEnabled()) {
                // Успешен вход
                return "redirect:/profile";
            } else {
                // Акаунтът е деактивиран
                session.setAttribute("error", "Вашият акаунт е деактивиран. Моля, свържете се с администратора.");
                return "redirect:/login";
            }
        } else {
            // Грешен имейл или парола
            session.setAttribute("error", "Грешен имейл или парола. Моля, опитайте отново!");
            return "redirect:/login";
        }
    }

    /**
     * Обработва GET заявки за показване на страницата за регистрация.
     * @param model Модел за подаване на данни.
     * @param authentication Обект на текущата автентикация.
     * @return Името на шаблона 'register' или пренасочване, ако потребителят е вече логнат.
     */
    @GetMapping("/register")
    public String registerPage(Model model, @AuthenticationPrincipal Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/profile";
        }
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    /**
     * Обработва POST заявка с данните от формата за регистрация.
     * @param request DTO обект, съдържащ данните от формата.
     * @param bindingResult Обект за грешки от валидацията.
     * @param session HTTP сесия.
     * @return Пренасочване към страница за успешна регистрация или връщане към формата при грешка.
     */
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registrationRequest") RegistrationRequest request,
            BindingResult bindingResult,
            HttpSession session) {

        // Проверка за грешки от стандартните валидационни анотации (@Email, @Size и др.).
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Персонализирана валидация за минимална възраст.
        if (!isValidAge(request.getDateOfBirth(), 14)) {
            bindingResult.rejectValue("dateOfBirth", "error.dateOfBirth", "Трябва да сте на поне 14 години.");
            return "register";
        }

        // Делегира същинската регистрация към сервизния слой.
        registrationService.register(request);
        session.setAttribute("registrationSuccess", true);
        return "redirect:/registrationSuccess";
    }

    /**
     * Помощен метод за проверка на минимална възраст.
     * @param dateOfBirth Дата на раждане.
     * @param minimumAge Минимална изисквана възраст.
     * @return true, ако възрастта е достатъчна, в противен случай false.
     */
    private boolean isValidAge(LocalDate dateOfBirth, int minimumAge) {
        if (dateOfBirth == null) return false;
        return dateOfBirth.plusYears(minimumAge).isBefore(LocalDate.now());
    }

    /**
     * Показва страницата за успешна регистрация.
     * Достъпна е само след пренасочване от регистрационния процес.
     * @param session HTTP сесия.
     * @return Името на шаблона 'notifications/registrationSuccess'.
     */
    @GetMapping("/registrationSuccess")
    public String registrationSuccess(HttpSession session) {
        Boolean registrationSuccess = (Boolean) session.getAttribute("registrationSuccess");
        if (registrationSuccess != null && registrationSuccess) {
            session.removeAttribute("registrationSuccess"); // Изтрива атрибута, за да не може страницата да се достъпи отново.
            return "notifications/registrationSuccess";
        } else {
            return "redirect:/"; // Ако се опита директен достъп, пренасочва.
        }
    }

    /**
     * Обработва GET заявка от линка за потвърждение на имейл.
     * @param token Токенът, получен като параметър в URL.
     * @param model Модел за подаване на данни.
     * @param session HTTP сесия.
     * @return Пренасочване към страница за потвърдена регистрация или страница за грешка.
     */
    @GetMapping("/registration/confirm")
    public String confirm(@RequestParam("token") String token, Model model, HttpSession session) {
        try {
            String result = registrationService.confirmToken(token);
            if ("confirmed".equals(result)) {
                session.setAttribute("confirmedRegistration", true);
                return "redirect:/confirmedRegistration";
            } else {
                model.addAttribute("error", "Невалиден или изтекъл токен");
                return "notifications/error";
            }
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "notifications/error";
        }
    }

    /**
     * Показва страницата за потвърдена регистрация.
     * Достъпна е само след пренасочване от процеса по потвърждение.
     * @param session HTTP сесия.
     * @return Името на шаблона 'notifications/confirmedRegistration'.
     */
    @GetMapping("/confirmedRegistration")
    public String confirmedRegistration(HttpSession session) {
        Boolean confirmedRegistration = (Boolean) session.getAttribute("confirmedRegistration");
        if (confirmedRegistration != null && confirmedRegistration) {
            session.removeAttribute("confirmedRegistration");
            return "notifications/confirmedRegistration";
        } else {
            return "redirect:/";
        }
    }
}