package LangForU_DevTeam.LangForU.customSettings;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Помощен (utility) клас, предоставящ методи за манипулация на низове (strings).
 * Маркиран е като {@link Component}, за да може да бъде управляван от Spring
 * и инжектиран в други компоненти при нужда.
 */
@Component
public class StringUtil {

    /**
     * Съкращава даден текст до определен максимален брой думи.
     * Ако текстът е по-дълъг от зададения лимит, той се отрязва и в края му се добавя многоточие "...".
     *
     * @param text     Текстът, който трябва да бъде съкратен.
     * @param maxWords Максималният брой думи, които да бъдат запазени.
     * @return Съкратеният текст с добавено многоточие, или оригиналният текст, ако той е по-къс от лимита.
     */
    public String abbreviate(String text, int maxWords) {
        // Разделяме текста на думи по празно пространство.
        String[] words = text.split(" ");

        // Проверяваме дали броят на думите надвишава максимално допустимия.
        if (words.length > maxWords) {
            // Ако да, взимаме само първите 'maxWords' на брой думи,
            // съединяваме ги отново с празно пространство и добавяме "..." в края.
            return String.join(" ", Arrays.copyOfRange(words, 0, maxWords)) + "...";
        }

        // Ако броят на думите не надвишава лимита, връщаме оригиналния текст.
        return text;
    }
}