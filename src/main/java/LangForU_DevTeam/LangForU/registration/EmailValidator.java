package LangForU_DevTeam.LangForU.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Сервизен клас, който валидира синтактичната коректност на имейл адреси.
 * Имплементира функционалния интерфейс {@link Predicate<String>}, което позволява
 * използването му като функция, която приема низ и връща булева стойност
 * (true, ако е валиден имейл, false, ако не е).
 */
@Service
public class EmailValidator implements Predicate<String> {

    /**
     * Регулярен израз (regex), който дефинира структурата на валиден имейл адрес.
     */
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    /**
     * Компилирана версия на регулярния израз.
     * Компилирането се извършва еднократно при зареждане на класа,
     * което подобрява производителността при многократни валидации.
     */
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Методът, който извършва същинската валидация.
     * Имплементира метода test() от интерфейса Predicate.
     *
     * @param email Имейл адресът, който трябва да бъде валидиран.
     * @return {@code true}, ако имейлът отговаря на регулярния израз, и {@code false} в противен случай (или ако е null).
     */
    @Override
    public boolean test(String email) {
        // Ако имейлът е null, той е невалиден.
        if (email == null) {
            return false;
        }
        // Използваме компилирания шаблон, за да проверим дали низът съвпада с него.
        return pattern.matcher(email).matches();
    }
}