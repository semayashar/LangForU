package LangForU_DevTeam.LangForU.Sevi;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRepository;
import LangForU_DevTeam.LangForU.appuser.AppUserRole;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контролер, който отговаря за показването на страницата за чат с AI асистента "Севи".
 * Основната му роля е да извърши проверка за автентикация и оторизация, преди да
 * позволи достъп до чат функционалността.
 */
@Controller
public class ChatPageViewController {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    /**
     * Обработва GET заявка за достъп до чат страницата на "Севи".
     *
     * @param model Модел за подаване на данни към изгледа (view).
     * @return Името на шаблона 'Sevi/chat' при успешен достъп, пренасочване към '/login',
     * ако потребителят не е логнат, или страница за грешка при липса на права.
     */
    @GetMapping("/Sevi/chat")
    public String chatPageSevi(Model model) {
        // 1. Взима информация за текущата автентикация от контекста на Spring Security.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Проверява дали потребителят е логнат. Ако не е, пренасочва към страницата за вход.
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        // 3. Зарежда пълния обект на потребителя от базата данни.
        String email = authentication.getName();
        AppUser currentUser = (AppUser) appUserService.loadUserByUsername(email);

        if (currentUser == null) {
            model.addAttribute("error", "Грешка при зареждане на профила. Потребителят не е намерен.");
            return "notifications/error";
        }

        // 4. Проверка за оторизация: потребителят трябва да е администратор или да е записан поне в един курс.
        boolean isAdmin = currentUser.getAppUserRole() == AppUserRole.ADMIN;
        boolean hasCourses = currentUser.getCourses() != null && !currentUser.getCourses().isEmpty();

        if (!isAdmin && !hasCourses) {
            model.addAttribute("error", "Достъпът до помощника Севи изисква да сте админ или записан поне в един курс.");
            return "notifications/error";
        }

        // 5. Ако проверката е успешна, добавя необходимите данни към модела.
        model.addAttribute("user", currentUser);
        String avatarUrl = currentUser.getProfilePicture();
        model.addAttribute("userAvatar", avatarUrl != null ? avatarUrl : "/img/default-avatar.png");

        // 6. Връща името на шаблона за чат страницата.
        return "Sevi/chat";
    }

}