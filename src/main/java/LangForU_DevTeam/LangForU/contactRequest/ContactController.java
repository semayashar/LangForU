package LangForU_DevTeam.LangForU.contactRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Контролер, който обработва HTTP заявките, свързани с формата за контакт.
 * Отговаря за показването на формата и за обработката на изпратените данни.
 */
@Controller
public class ContactController {

    /**
     * Сервиз за управление на логиката, свързана със заявките за контакт.
     * Инжектира се автоматично от Spring.
     */
    @Autowired
    private ContactRequestService contactRequestService;

    /**
     * Обработва GET заявки за показване на страницата за контакт.
     * Проверява за параметри 'success' или 'error' в URL-а, за да покаже
     * съответните съобщения за успех или грешка след пренасочване.
     *
     * @param success Параметър, указващ успешно изпращане (незадължителен).
     * @param error   Параметър, указващ грешка при изпращане (незадължителен).
     * @param model   Модел за подаване на данни към изгледа (view).
     * @return Името на шаблона 'contact'.
     */
    @GetMapping("/contact")
    public String showContactForm(
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error,
            Model model) {

        // Ако е налице параметър 'success=true', добавя съобщение за успех към модела.
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Вашето съобщение е изпратено успешно!");
        }
        // Ако е налице параметър 'error=true', добавя съобщение за грешка към модела.
        if ("true".equals(error)) {
            model.addAttribute("errorMessage", "Възникна грешка при изпращането. Моля, опитайте отново!");
        }
        return "contact";
    }

    /**
     * Обработва POST заявки от формата за контакт.
     * Приема данните от формата, валидира ги и ги записва в базата данни.
     *
     * @param name             Името на подателя от полето 'name'.
     * @param email            Имейлът на подателя от полето 'email'.
     * @param subject          Темата на съобщението от полето 'subject'.
     * @param message          Текстът на съобщението от полето 'message'.
     * @return Пренасочване (redirect) към GET ендпойнта '/contact' със съответния параметър за успех или грешка.
     */
    @PostMapping("/contact")
    @ResponseBody
    public ResponseEntity<String> submitContactRequest(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message) {

        if (name.isBlank() || email.isBlank() || subject.isBlank() || message.isBlank()) {
            return ResponseEntity.badRequest().body("Полетата не могат да бъдат празни.");
        }

        try {
            ContactRequest request = new ContactRequest(name, email, subject, message);
            contactRequestService.save(request);
            return ResponseEntity.ok("Успешно изпратено");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Възникна грешка.");
        }
    }
}