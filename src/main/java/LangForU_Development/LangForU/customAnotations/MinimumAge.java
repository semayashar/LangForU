package LangForU_Development.LangForU.customAnotations;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MinimumAgeValidator.class})
public @interface MinimumAge {
    String message() default "must be at least {value} years old";
    int value();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
