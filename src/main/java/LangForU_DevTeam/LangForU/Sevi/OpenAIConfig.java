package LangForU_DevTeam.LangForU.Sevi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационен клас, който служи за зареждане на настройки за OpenAI API
 * от външен конфигурационен файл (напр. application.properties).
 * Използва type-safe подход за управление на конфигурационни променливи.
 */
@Configuration // Указва на Spring, че този клас е източник на дефиниции на бийнове.
@Getter // Lombok: Автоматично генерира get-методи за всички полета.
@Setter // Lombok: Автоматично генерира set-методи за всички полета.
@ConfigurationProperties(prefix = "openai.api") // Ключова анотация, която казва на Spring да свърже (bind) всички променливи с префикс "openai.api" към полетата на този клас.
public class OpenAIConfig {

    /**
     * API ключът за достъп до OpenAI услугите.
     * Очаква се стойността да бъде зададена в application.properties като: openai.api.key=ВАШИЯТ_КЛЮЧ
     */
    private String key;

    /**
     * URL адресът на OpenAI API.
     * Очаква се стойността да бъде зададена в application.properties като: openai.api.url=https://api.openai.com/v1/chat/completions
     */
    private String url;

    /**
     * Моделът на OpenAI, който ще се използва (напр. gpt-4, gpt-4o-mini).
     * Има зададена стойност по подразбиране "gpt-4o-mini", която ще се използва,
     * ако не е дефинирана друга в application.properties (openai.api.model=...).
     */
    private String model = "gpt-4o-mini";
}