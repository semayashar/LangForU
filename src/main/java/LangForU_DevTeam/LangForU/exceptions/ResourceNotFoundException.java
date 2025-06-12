package LangForU_DevTeam.LangForU.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Персонализирано изключение, което се хвърля, когато даден ресурс
 * (например потребител, курс, лекция и т.н.) не може да бъде намерен в системата.
 *
 * Анотацията {@link ResponseStatus} указва на Spring MVC да върне
 * HTTP статус 404 (Not Found) към клиента, когато това изключение
 * не е обработено от друг {@link org.springframework.web.bind.annotation.ExceptionHandler}.
 * Това е стандартна практика за обозначаване на липсващи ресурси в REST API.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Конструктор, който приема съобщение за грешка.
     * @param message Съобщение, което описва по-детайлно грешката
     * (напр. "Потребител с ID 5 не е намерен.").
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}