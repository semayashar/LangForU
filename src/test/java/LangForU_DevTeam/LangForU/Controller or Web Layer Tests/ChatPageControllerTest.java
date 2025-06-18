package LangForU_DevTeam.LangForU.Sevi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Модулни тестове за REST контролера {@link ChatPageController}.
 * Тези тестове се фокусират върху проверката на HTTP ендпойнтите,
 * валидацията на входните данни и коректността на HTTP отговорите,
 * без да се тества бизнес логиката в {@link ChatGPTService}, който е мокнат.
 */
@WebMvcTest(ChatPageController.class)
class ChatPageControllerTest {

    @Autowired
    private MockMvc mockMvc; // Основен инструмент за тестване на Spring MVC контролери без стартиране на реален сървър.

    @MockBean
    private ChatGPTService chatGPTService; // Мок (фалшива) версия на сервиза, за да изолираме контролера.

    @Autowired
    private ObjectMapper objectMapper; // Помощен инструмент за конвертиране на обекти в JSON и обратно.

    // Забележка: Тъй като chatHistory е състояние вътре в контролера,
    // който се пресъздава за тестовия контекст, не можем директно да го манипулираме тук.
    // Тестовете са написани така, че да работят с инстанцията на контролера, създадена от Spring.

    /**
     * Тества POST ендпойнта /chat/send.
     * Очаква се контролерът да приеме съобщение, да извика ChatGPTService
     * и да върне отговора на асистента като чист текст.
     */
    @Test
    @WithMockUser // ПОПРАВКА: Симулира заявка от логнат потребител.
    void sendMessage_shouldReturnAssistantResponse() throws Exception {
        // Arrange: Подготовка на данните
        String userInput = "Как се казваш?";
        String assistantResponse = "Казвам се Севи, вашият виртуален асистент.";

        when(chatGPTService.askChatGPT(anyString())).thenReturn(assistantResponse);

        // Act & Assert: Изпълнение на заявката и проверка на резултата
        mockMvc.perform(post("/chat/send")
                        .param("userInput", userInput)
                        .with(csrf())) // ПОПРАВКА: Добавя валиден CSRF токен към заявката.
                .andExpect(status().isOk())
                .andExpect(content().string(assistantResponse));

        // Verify: Проверяваме дали методът askChatGPT на сервиза е бил извикан точно веднъж с подадения от нас вход.
        verify(chatGPTService).askChatGPT(userInput);
    }

    /**
     * Тества POST ендпойнта /chat/question-help.
     * Очаква се да приеме JSON с въпрос и да върне JSON с отговор.
     */
    @Test
    @WithMockUser // ПОПРАВКА: Симулира заявка от логнат потребител.
    void getQuestionHelp_withValidQuestion_shouldReturnOkResponse() throws Exception {
        // Arrange
        String question = "Каква е разликата между 'affect' и 'effect'?";
        String answer = "'Affect' е глагол, а 'effect' е съществително.";
        Map<String, String> requestBody = Map.of("question", question);

        when(chatGPTService.askChatGPT(question)).thenReturn(answer);

        // Act & Assert
        mockMvc.perform(post("/chat/question-help")
                        .with(csrf()) // ПОПРАВКА: Добавя CSRF токен.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply", is(answer)));

        // Verify
        verify(chatGPTService).askChatGPT(question);
    }

    /**
     * Тества POST ендпойнта /chat/question-help с невалидни данни (празен въпрос).
     * Очаква се отговор със статус 400 Bad Request.
     */
    @Test
    @WithMockUser // ПОПРАВКА: Симулира заявка от логнат потребител.
    void getQuestionHelp_withBlankQuestion_shouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, String> requestBody = Map.of("question", " ");

        // Act & Assert
        mockMvc.perform(post("/chat/question-help")
                        .with(csrf()) // ПОПРАВКА: Добавя CSRF токен.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest()) // Очакваме грешка 400
                .andExpect(jsonPath("$.error", is("Невалиден вход.")));
    }

    /**
     * Тества GET ендпойнта /chat/history.
     * Тъй като историята се пази в самия контролер, първо трябва да я "напълним",
     * като симулираме извикване на /send, и след това да проверим какво връща /history.
     */
    @Test
    @WithMockUser // ПОПРАВКА: Симулира заявка от логнат потребител.
    void getChatHistory_shouldReturnPopulatedHistory() throws Exception {
        // Arrange: Първо, симулираме изпращане на съобщение, за да се напълни историята в контролера.
        String userInput = "Здравей";
        String assistantResponse = "Здравейте!";
        when(chatGPTService.askChatGPT(userInput)).thenReturn(assistantResponse);

        // Изпълняваме POST заявката, за да добавим съобщения в историята.
        mockMvc.perform(post("/chat/send")
                .param("userInput", userInput)
                .with(csrf()));

        // Act & Assert: Сега тестваме ендпойнта за история
        mockMvc.perform(get("/chat/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].role", is("You")))
                .andExpect(jsonPath("$[0].content", is(userInput)))
                .andExpect(jsonPath("$[1].role", is("Assistant")))
                .andExpect(jsonPath("$[1].content", is(assistantResponse)));
    }
}
