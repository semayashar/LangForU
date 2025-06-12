package LangForU_DevTeam.LangForU.Sevi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Сервизен клас, който служи като клиент за OpenAI ChatGPT API.
 * Управлява създаването на заявки, изпращането им до API-то и обработката на отговорите.
 */
@Service
@RequiredArgsConstructor // Lombok: Генерира конструктор с всички финални полета.
public class ChatGPTService {
    private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);

    private final OpenAIConfig openAIConfig; // Конфигурация, съдържаща API ключ, URL и модел.
    private final WebClient.Builder webClientBuilder; // "Строител" за WebClient, предоставен от Spring.

    private WebClient webClient; // Инстанция на WebClient, която ще се използва за HTTP заявки.

    /**
     * Метод за инициализация, маркиран с {@link PostConstruct}.
     * Изпълнява се автоматично от Spring, след като всички зависимости са инжектирани.
     * Конфигурира и изгражда инстанцията на WebClient с необходимите базови настройки
     * като URL и хедъри за автентикация.
     */
    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder
                .baseUrl(openAIConfig.getUrl()) // Задава базовия URL на API-то.
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Задава Content-Type по подразбиране.
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAIConfig.getKey()) // Задава хедъра за автентикация с API ключа.
                .build();
    }

    /**
     * Основен метод за изпращане на съобщение от потребител до ChatGPT и получаване на отговор.
     *
     * @param userMessage Съобщението, въведено от потребителя.
     * @return Текстовият отговор, генериран от AI асистента, или съобщение за грешка.
     */
    public String askChatGPT(String userMessage) {
        try {
            // Дефинира "системна инструкция" (system prompt), която задава самоличността,
            // поведението и ограниченията на AI асистента "Севи".
            String seviSystemPrompt = """
                    Sevi is a virtual assistant specializing in supporting users with foreign language learning. She is 25 years old and responds exclusively in Bulgarian. Sevi’s expertise covers English, German, Spanish, Italian, Russian, and Japanese languages.
                    
                    Sevi provides detailed, clear, and well-structured explanations on the following topics:
                    
                    - Grammar: rules, explanations, and clarifications
                    - Translation of words, phrases, and sentences
                    - Word meanings and synonyms
                    - Example sentences illustrating vocabulary and grammatical structures
                    - Spelling and punctuation guidance
                    - Pronunciation and phonetic transcription
                    - Differences between closely related words and expressions
                    - Idioms and phraseology
                    - Recommendations for improving written and spoken language skills
                    - Explanations of linguistic structures and their correct usage
                    - Etymology and historical origins of words and expressions
                    - Practice exercises and tests (Sevi directs users to reputable, official online resources without providing direct exercises)
                    
                    Operational Constraints:
                    
                    - Sevi only responds to queries related to language learning and linguistic topics.
                    - She does not generate or provide direct exercises but refers users to vetted and approved resources for practice.
                    - She refrains from promoting third-party applications or websites unless officially endorsed and integrated within the language learning platform.
                    - All communication is strictly conducted in Bulgarian.
                    - Responses remain focused on language education without deviating into unrelated subjects.
                    
                    Objective:
                    Sevi aims to facilitate and motivate learners by delivering reliable, precise, and comprehensive language learning assistance, fostering an engaging and supportive educational experience.
                    
                    Tone and Style:
                    Sevi communicates in a professional, approachable, and encouraging manner. Her explanations are thorough yet accessible, utilizing bullet points and examples as needed to enhance clarity and comprehension.
                    """;

            Message systemMessage = new Message("system", seviSystemPrompt);
            Message userMessageObj = new Message("user", userMessage);

            // Създава обект на заявката, съдържащ модела и съобщенията (системно и потребителско).
            ChatRequest chatRequest = new ChatRequest(
                    openAIConfig.getModel(),
                    new Message[]{systemMessage, userMessageObj}
            );

            // Изпраща POST заявка към OpenAI API.
            String response = webClient.post()
                    .uri("/chat/completions") // Ендпойнтът се добавя към базовия URL.
                    .bodyValue(chatRequest) // Задава тялото на заявката.
                    .retrieve() // Извлича отговора.
                    .bodyToMono(String.class) // Конвертира тялото на отговора в Mono<String>.
                    .block(); // Блокира изпълнението, докато не се получи отговор (подходящо за нереактивни приложения).

            // Парсва получения JSON отговор, за да извлече само текста.
            return parseResponse(response);

        } catch (Exception e) {
            logger.error("Грешка при връзка с ChatGPT: {}", e.getMessage(), e);
            return "Грешка: Неуспешно свързване с асистента.";
        }
    }


    /**
     * Помощен метод за парсване на JSON отговора от OpenAI API.
     *
     * @param jsonResponse JSON низът, получен от API-то.
     * @return Извлеченият текстов отговор от AI асистента или съобщение за грешка при неуспешно парсване.
     */
    private String parseResponse(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            logger.info("Парснат отговор: {}", root);

            // Навигира през JSON структурата, за да достигне до съдържанието на съобщението.
            // Структура: { "choices": [ { "message": { "content": "..." } } ] }
            JsonNode choicesNode = root.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                return choicesNode.get(0).path("message").path("content").asText().trim();
            }
            return "Асистентът не върна отговор.";
        } catch (Exception e) {
            logger.error("Грешка при парсване на отговора от ChatGPT: {}", e.getMessage(), e);
            return "Грешка: Неуспешно разчитане на отговора от асистента.";
        }
    }

}