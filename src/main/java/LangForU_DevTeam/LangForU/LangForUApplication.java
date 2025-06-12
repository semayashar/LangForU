package LangForU_DevTeam.LangForU;

import LangForU_DevTeam.LangForU.Sevi.OpenAIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Главен клас за стартиране на Spring Boot приложението LangForU.
 * <p>
 * Анотациите, използвани тук, активират основни функционалности на Spring:
 * - {@code @SpringBootApplication} конфигурира и стартира приложението
 * - {@code @EnableScheduling} позволява използване на планирани задачи (@Scheduled)
 * - {@code @EnableConfigurationProperties} зарежда конфигурационен клас за OpenAI API
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(OpenAIConfig.class)
public class LangForUApplication {

    /**
     * Главен метод, който стартира Spring Boot приложението.
     *
     * @param args аргументи от командния ред
     */
    public static void main(String[] args) {
        SpringApplication.run(LangForUApplication.class, args);
    }

    /**
     * Създава и регистрира {@link RestTemplate} бин, който може да се използва за HTTP заявки.
     *
     * @return нова инстанция на RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
