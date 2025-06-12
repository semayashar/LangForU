package LangForU_DevTeam.LangForU.customAnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Персонализирана анотация за валидация, която проверява дали даден низ (String)
 * не надвишава определен максимален брой думи.
 * Тази анотация е част от Jakarta Bean Validation framework.
 * Логиката за валидация се намира в класа {@link MaxWordsValidator}.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE}) // Указва къде може да се прилага анотацията (полета, методи и др.).
@Retention(RetentionPolicy.RUNTIME) // Указва, че анотацията ще бъде достъпна по време на изпълнение (runtime), което е необходимо за валидацията.
@Constraint(validatedBy = MaxWordsValidator.class) // Свързва анотацията с нейния валидатор.
public @interface MaxWords {

    /**
     * Съобщението за грешка, което ще се покаже, ако валидацията е неуспешна.
     * Плейсхолдърът {value} ще бъде заменен със стойността, подадена на анотацията.
     * @return Съобщението за грешка.
     */
    String message() default "Описание не може да съдържа повече от {value} думи.";

    /**
     * Позволява групиране на валидации. Може да се използва за прилагане
     * на определени набори от правила в различни ситуации (напр. при създаване срещу обновяване).
     * @return Масив от класове, представляващи групите за валидация.
     */
    Class<?>[] groups() default {};

    /**
     * Позволява прикачането на допълнителни метаданни (payload) към анотацията.
     * Може да се използва за разширени сценарии, като например задаване на нива на сериозност на грешката.
     * @return Масив от класове, наследяващи Payload.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Задължителен атрибут на анотацията, който указва максималния разрешен брой думи.
     * Пример за употреба: @MaxWords(100)
     * @return Максималният брой думи.
     */
    int value();
}