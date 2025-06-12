package LangForU_DevTeam.LangForU.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Конфигурационен клас, който предоставя бийн (bean) за енкодиране на пароли.
 */
@Configuration // Указва на Spring, че този клас съдържа дефиниции на бийнове.
public class PasswordEncoder {

    /**
     * Създава и конфигурира бийн от тип {@link BCryptPasswordEncoder}.
     * BCrypt е силен и широко приет стандарт за хеширане на пароли, който включва "сол" (salt),
     * за да предпази от атаки тип "rainbow table".
     * Този бийн ще бъде използван в цялото приложение (напр. в WebSecurityConfig),
     * за да се енкодират паролите при регистрация и да се сравняват при вход.
     *
     * @return Инстанция на BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}