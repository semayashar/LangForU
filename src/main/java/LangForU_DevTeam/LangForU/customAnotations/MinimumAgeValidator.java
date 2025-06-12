package LangForU_DevTeam.LangForU.customAnotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

/**
 * Клас-валидатор, който имплементира логиката за персонализираната анотация {@link MinimumAge}.
 * Този клас проверява дали възрастта, изчислена от дадена дата на раждане (LocalDate),
 * е по-голяма или равна на зададения минимум.
 */
public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, LocalDate> {

    /**
     * Поле, което съхранява минималната изисквана възраст, взета от анотацията.
     */
    private int age;

    /**
     * Метод за инициализация, който се извиква от framework-а за валидация.
     * Той извлича стойността, зададена в анотацията (напр. @MinimumAge(18)),
     * и я записва в полето age.
     *
     * @param constraintAnnotation инстанция на анотацията, приложена върху полето.
     */
    @Override
    public void initialize(MinimumAge constraintAnnotation) {
        this.age = constraintAnnotation.value();
    }

    /**
     * Методът, който извършва същинската валидация на възрастта.
     *
     * @param dateOfBirth Стойността на полето, което се валидира (дата на раждане).
     * @param context     Контекстът на валидацията.
     * @return {@code true}, ако изчислената възраст е по-голяма или равна на минималната,
     * и {@code false} в противен случай или ако датата на раждане е null.
     */
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        // Ако датата на раждане е null, валидацията е неуспешна.
        if (dateOfBirth == null) {
            return false;
        }

        // Изчислява периода между датата на раждане и днешна дата,
        // след което взима броя на пълните години.
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= age;
    }
}