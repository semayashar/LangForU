package LangForU_DevTeam.LangForU.Sevi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatGPTServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private Mono<String> monoResponse;

    private OpenAIConfig openAIConfig;
    private ChatGPTService chatGPTService;

    @BeforeEach
    void setUp() {
        openAIConfig = new OpenAIConfig();
        openAIConfig.setKey("test-api-key");
        openAIConfig.setUrl("https://api.openai.com/v1");
        openAIConfig.setModel("gpt-4o-mini");

        chatGPTService = new ChatGPTService(openAIConfig, webClientBuilder);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(any(), any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(monoResponse);

        chatGPTService.init();
    }

    @Test
    void askChatGPT_whenApiCallIsSuccessful_shouldReturnParsedContent() {
        String successfulJsonResponse = "{\"choices\":[{\"message\":{\"content\":\"  Това е отговор от Севи.  \"}}]}";
        when(monoResponse.block()).thenReturn(successfulJsonResponse);

        String actualResponse = chatGPTService.askChatGPT("Какво е пролет?");

        assertEquals("Това е отговор от Севи.", actualResponse);

        ArgumentCaptor<ChatRequest> requestCaptor = ArgumentCaptor.forClass(ChatRequest.class);
        verify(requestBodySpec).bodyValue(requestCaptor.capture());
        ChatRequest capturedRequest = requestCaptor.getValue();

        assertEquals(openAIConfig.getModel(), capturedRequest.getModel());
        assertEquals("user", capturedRequest.getMessages()[1].getRole());
        assertEquals("Какво е пролет?", capturedRequest.getMessages()[1].getContent());
    }

    @Test
    void askChatGPT_whenApiCallFails_shouldReturnErrorMessage() {
        when(monoResponse.block()).thenThrow(new RuntimeException("API connection failed"));
        String actualResponse = chatGPTService.askChatGPT("Въпрос, който ще доведе до грешка");
        assertEquals("Грешка: Неуспешно свързване с асистента.", actualResponse);
    }

    @Test
    void askChatGPT_whenResponseIsMalformed_shouldReturnParsingErrorMessage() {
        when(monoResponse.block()).thenReturn("невалиден json");
        String actualResponse = chatGPTService.askChatGPT("въпрос");
        assertEquals("Грешка: Неуспешно разчитане на отговора от асистента.", actualResponse);
    }
}