package LangForU_DevTeam.LangForU.customAnotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Клас-валидатор, който имплементира логиката за персонализираната анотация {@link MaxWords}.
 * Този клас проверява дали броят на думите в даден низ не надвишава зададения максимум.
 */
public class MaxWordsValidator implements ConstraintValidator<MaxWords, String> {

    /**
     * Поле, което съхранява максималния разрешен брой думи, взет от анотацията.
     */
    private int maxWords;

    /**
     * Метод за инициализация, който се извиква от framework-а за валидация преди първата употреба.
     * Той извлича стойността, зададена в анотацията (напр. @MaxWords(100)), и я записва в полето maxWords.
     *
     * @param constraintAnnotation инстанция на анотацията, приложена върху полето/метода.
     */
    @Override
    public void initialize(MaxWords constraintAnnotation) {
        this.maxWords = constraintAnnotation.value();
    }

    /**
     * Методът, който извършва същинската валидация.
     *
     * @param value   Стойността на полето, което се валидира (в случая - низ).
     * @param context Контекстът на валидацията, който може да се използва за промяна на съобщението за грешка.
     * @return {@code true}, ако низът е валиден (съдържа по-малко или равен брой думи на maxWords, или е null),
     * и {@code false} в противен случай.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Ако стойността е null, приемаме я за валидна. Задължителността на полето
        // трябва да се контролира с отделна анотация като @NotNull.
        if (value == null) {
            return true;
        }

        // Премахваме празните пространства в началото и края и разделяме низа на думи
        // по едно или повече празни пространства.
        String[] words = value.trim().split("\\s+");

        // Връщаме true, ако броят на думите е по-малък или равен на зададения максимум.
        return words.length <= maxWords;
    }
}