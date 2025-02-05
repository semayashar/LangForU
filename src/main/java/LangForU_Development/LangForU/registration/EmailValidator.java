package LangForU_Development.LangForU.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean test(String email) {
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

}
