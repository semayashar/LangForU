package LangForU_DevTeam.LangForU.customAnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Персонализирана анотация за валидация, която проверява дали дадено лице
 * е на възраст, по-голяма или равна на определен минимум.
 * Тази анотация е предназначена за употреба върху полета от тип LocalDate,
 * представляващи дата на раждане.
 * Логиката за валидация се намира в класа {@link MinimumAgeValidator}.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE}) // Указва къде може да се прилага анотацията.
@Retention(RetentionPolicy.RUNTIME) // Указва, че анотацията ще бъде достъпна по време на изпълнение.
@Constraint(validatedBy = MinimumAgeValidator.class) // Свързва анотацията с нейния валидатор.
public @interface MinimumAge {

    /**
     * Съобщението за грешка, което ще се покаже, ако валидацията е неуспешна.
     * Плейсхолдърът {value} ще бъде заменен със стойността на минималната възраст.
     * @return Съобщението за грешка.
     */
    String message() default "Потребителят трябва да бъде на поне {value} години.";

    /**
     * Позволява групиране на валидации.
     * @return Масив от класове, представляващи групите за валидация.
     */
    Class<?>[] groups() default {};

    /**
     * Позволява прикачането на допълнителни метаданни (payload) към анотацията.
     * @return Масив от класове, наследяващи Payload.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Задължителен атрибут на анотацията, който указва минималната разрешена възраст.
     * Пример за употреба: @MinimumAge(18)
     * @return Минималната възраст в години.
     */
    int value();
}