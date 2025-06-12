package LangForU_DevTeam.LangForU.Sevi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * REST контролер, който обработва заявките за чат в реално време с AI асистента.
 * Тъй като е маркиран с {@link RestController}, всички методи в него връщат
 * данни директно в тялото на HTTP отговора (обикновено в JSON формат).
 */
@RestController // Указва, че това е REST контролер.
@RequestMapping("/chat") // Всички пътища в този контролер ще започват с /chat.
@RequiredArgsConstructor // Lombok: Генерира конструктор с всички финални полета.
public class ChatPageController {

    private final ChatGPTService chatGPTService; // Сервиз за комуникация с OpenAI API.

    /**
     * Списък, който пази историята на чата в паметта на приложението.
     * ВАЖНО: Тази история е временна и ще бъде изтрита при всяко рестартиране на сървъра.
     * Това не е персистентно съхранение (в база данни).
     */
    private final List<Message> chatHistory = new ArrayList<>();

    /**
     * Обработва изпращането на ново съобщение от потребителя.
     *
     * @param userInput Текстът, въведен от потребителя.
     * @return Отговорът, генериран от AI асистента, като чист текст (String).
     */
    @PostMapping("/send")
    public String sendMessage(@RequestParam("userInput") String userInput) {
        // Добавя съобщението на потребителя към историята.
        chatHistory.add(new Message("You", userInput));
        // Изпраща съобщението към ChatGPT и получава отговор.
        String assistantResponse = chatGPTService.askChatGPT(userInput);
        // Добавя отговора на асистента към историята.
        chatHistory.add(new Message("Assistant", assistantResponse));
        return assistantResponse;
    }

    /**
     * Обработва заявка за помощ по конкретен въпрос.
     *
     * @param request Карта (Map), съдържаща JSON тялото на заявката, очаква се ключ "question".
     * @return {@link ResponseEntity}, съдържащ JSON с отговора на асистента или съобщение за грешка.
     */
    @PostMapping("/question-help")
    public ResponseEntity<?> getQuestionHelp(@RequestBody Map<String, String> request) {
        String questionDetails = request.get("question");
        if (questionDetails == null || questionDetails.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Невалиден вход."));
        }
        try {
            chatHistory.add(new Message("You", questionDetails));
            String assistantResponse = chatGPTService.askChatGPT(questionDetails);
            chatHistory.add(new Message("Assistant", assistantResponse));
            return ResponseEntity.ok(Map.of("reply", assistantResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Неуспешна връзка с асистента."));
        }
    }

    /**
     * Връща текущата история на чата.
     *
     * @return Списък ({@link List}) от {@link Message} обекти, представляващи разговора.
     */
    @GetMapping("/history")
    public List<Message> getChatHistory() {
        return chatHistory;
    }

}