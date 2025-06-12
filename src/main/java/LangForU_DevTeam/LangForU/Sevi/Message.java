package LangForU_DevTeam.LangForU.Sevi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO), който представлява единично съобщение
 * в рамките на чат разговор с AI модел, като например OpenAI API.
 * Всеки обект от този клас съдържа роля и съдържание.
 */
@Getter             // Lombok: Генерира get-методи за всички полета.
@Setter             // Lombok: Генерира set-методи за всички полета.
@AllArgsConstructor // Lombok: Генерира конструктор, който приема всички полета като аргументи.
public class Message {

    /**
     * Ролята на автора на съобщението.
     * Типични стойности са "system" (за системни инструкции),
     * "user" (за съобщение от потребителя) и "assistant" (за отговор от AI).
     */
    private String role;

    /**
     * Текстовото съдържание на съобщението.
     */
    private String content;
}