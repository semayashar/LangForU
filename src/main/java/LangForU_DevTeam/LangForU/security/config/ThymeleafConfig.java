package LangForU_DevTeam.LangForU.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

/**
 * Конфигурационен клас за Thymeleaf.
 * Този клас дефинира бийнове (beans), които персонализират работата на Thymeleaf,
 * по-специално като добавят интеграция със Spring Security.
 */
@Configuration // Указва на Spring, че този клас съдържа дефиниции на бийнове.
public class ThymeleafConfig {

    /**
     * Създава и конфигурира бийн за диалекта на Spring Security за Thymeleaf.
     * Този диалект позволява използването на специфични за Spring Security атрибути
     * и изрази в Thymeleaf шаблоните (напр. `sec:authorize`, `sec:authentication`).
     *
     * @return Инстанция на {@link SpringSecurityDialect}.
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    /**
     * Този бийн не е стриктно необходим в по-новите версии на Spring Boot,
     * тъй като интеграцията често се случва автоматично.
     * Въпреки това, той изрично конфигурира Template Engine на Thymeleaf
     * да използва добавения {@link SpringSecurityDialect}.
     *
     * @param templateResolver Стандартният template resolver, инжектиран от Spring.
     * @return Конфигурирана инстанция на {@link SpringTemplateEngine}.
     */
    @Bean
    public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        // Добавяме диалекта за сигурност към енджина на Thymeleaf.
        templateEngine.addDialect(springSecurityDialect());
        return templateEngine;
    }
}