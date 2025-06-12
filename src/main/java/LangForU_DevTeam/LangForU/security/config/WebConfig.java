package LangForU_DevTeam.LangForU.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Конфигурационен клас, който персонализира поведението на Spring Web MVC.
 * Имплементира интерфейса {@link WebMvcConfigurer}, за да се включат
 * персонализирани настройки за форматeри, ресурсни хендлъри и др.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Създава бийн (bean) от тип {@link WebClient.Builder}.
     * WebClient е модерен, неблокиращ клиент за извършване на HTTP заявки.
     * Този бийн предоставя предварително конфигуриран "строител", който може да се инжектира
     * в други компоненти за създаване на WebClient инстанции.
     * Задава по подразбиране 'Content-Type' хедър на 'application/json'.
     *
     * @return Конфигуриран WebClient.Builder.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * Регистрира глобални форматeри за типове данни.
     * Този метод конфигурира приложението да разпознава и парсва дати
     * в формат "dd/MM/yyyy" автоматично, когато те се подават като параметри в заявки.
     *
     * @param registry Регистърът, в който се добавят форматерите.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        registrar.registerFormatters(registry);
    }

    /**
     * Конфигурира как се обслужват статични ресурси (като изображения, CSS, JavaScript).
     * Този метод мапва URL път към физическо местоположение в проекта.
     *
     * @param registry Регистърът, в който се добавят ресурсните хендлъри.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Всяка заявка към URL, започващ с /img/avatars/, ще търси ресурс
        // в директорията classpath:/static/img/avatars/ (т.е. src/main/resources/static/img/avatars/).
        registry.addResourceHandler("/img/avatars/**")
                .addResourceLocations("classpath:/static/img/avatars/");
    }

    /**
     * Вътрешен клас, който имплементира интерфейса Formatter на Spring за типа LocalDate.
     * Това е алтернативен начин за дефиниране на логиката за преобразуване между String и LocalDate.
     *
     * ЗАБЕЛЕЖКА: Този клас е дефиниран, но в момента НЕ СЕ ИЗПОЛЗВА.
     * Конфигурацията в addFormatters() използва DateTimeFormatterRegistrar,
     * а не инстанция на този клас.
     */
    private static class LocalDateFormatter implements org.springframework.format.Formatter<LocalDate> {

        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        /**
         * Преобразува низ (String) в обект от тип LocalDate.
         */
        @Override
        public LocalDate parse(String text, Locale locale) {
            return LocalDate.parse(text, formatter);
        }

        /**
         * Преобразува обект от тип LocalDate в низ (String).
         */
        @Override
        public String print(LocalDate object, Locale locale) {
            return formatter.format(object);
        }
    }
}