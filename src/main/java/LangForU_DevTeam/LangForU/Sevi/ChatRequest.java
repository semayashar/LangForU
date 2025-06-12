package LangForU_DevTeam.LangForU.Sevi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO), който представлява тялото на заявка (request body)
 * към AI чат услуга като OpenAI API.
 * Този клас капсулира модела, който трябва да се използва, и поредицата от съобщения,
 * съставляващи разговора до момента.
 */
@Getter             // Lombok: Генерира get-методи за всички полета.
@Setter             // Lombok: Генерира set-методи за всички полета.
@AllArgsConstructor // Lombok: Генерира конструктор, който приема всички полета като аргументи.
public class ChatRequest {

    /**
     * Името на AI модела, който трябва да бъде използван за генериране на отговор.
     * Например: "gpt-4o-mini".
     */
    private String model;

    /**
     * Масив от обекти {@link Message}, представляващи историята на разговора.
     * Включва системни инструкции, потребителски въпроси и предишни отговори от AI.
     */
    private Message[] messages;
}