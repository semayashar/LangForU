package LangForU_DevTeam.LangForU.customSettings;

import LangForU_DevTeam.LangForU.exceptions.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Глобален конфигурационен клас, маркиран с {@link ControllerAdvice}.
 * Този клас позволява централизираното прилагане на общи настройки и обработка на грешки
 * за всички контролери в приложението.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Метод, маркиран с {@link InitBinder}, който се изпълнява преди свързването на данни от заявката към модела.
     * Тук регистрираме персонализиран редактор (PropertyEditor) за типа {@link LocalDate},
     * за да може Spring да разбира и форматира дати във формат "dd/MM/yyyy".
     * @param binder Обектът WebDataBinder, който ще бъде конфигуриран.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Дефинираме желания формат за датите
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            /**
             * Преобразува обект LocalDate към текст (String) за показване във форми.
             */
            @Override
            public String getAsText() {
                if (getValue() == null) return "";
                return formatter.format((LocalDate) getValue());
            }

            /**
             * Преобразува текст (String) от формата към обект LocalDate.
             */
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(LocalDate.parse(text, formatter));
                }
            }
        });
    }

    /**
     * Метод, който обработва специфично изключения от тип {@link ResourceNotFoundException}.
     * Когато някой сервиз хвърли това изключение (напр. при търсене на несъществуващ курс),
     * този метод ще го "хване" и ще покаже на потребителя шаблона 'notifications/error'
     * със съответното съобщение за грешка.
     * @param ex Хвърленото изключение от тип ResourceNotFoundException.
     * @param model Модел за подаване на данни към изгледа (view).
     * @return Името на Thymeleaf шаблона за грешка.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        // Добавяме съобщението от изключението към модела, за да се покаже на страницата.
        model.addAttribute("error", ex.getMessage());
        return "notifications/error";
    }

    /**
     * НОВО: Глобален метод за обработка на всички останали изключения.
     * Този метод ще "хване" всякакви други грешки (напр. NullPointerException, SQLException и др.),
     * които не са обработени от по-специфични ExceptionHandler-и.
     * Това гарантира, че потребителят винаги ще вижда форматирана страница за грешка.
     * @param ex Хвърленото изключение.
     * @param model Модел за подаване на данни към изгледа (view).
     * @return Името на Thymeleaf шаблона за грешка.
     */
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        // В реално приложение тук е добре да има и логиране на грешката за дебъгване:
        // import org.slf4j.Logger;
        // import org.slf4j.LoggerFactory;
        // private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        // logger.error("An unexpected error occurred: ", ex);

        // Добавяме съобщението от изключението към модела.
        model.addAttribute("error", ex.getMessage() != null ? ex.getMessage() : "Възникна неочаквана грешка.");
        return "notifications/error";
    }
}