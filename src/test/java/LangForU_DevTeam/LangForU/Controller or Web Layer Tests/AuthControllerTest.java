package LangForU_DevTeam.LangForU.security;

import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.registration.RegistrationRequest;
import LangForU_DevTeam.LangForU.registration.RegistrationService;
import LangForU_DevTeam.LangForU.security.config.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестове за AuthController, фокусирани върху уеб слоя.
 */
// КОРЕКЦИЯ: Добавяме @Import, за да заредим нашата конфигурация за сигурност
@WebMvcTest(AuthController.class)
@Import(WebSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Мокваме сървисите, които се използват от AuthController и WebSecurityConfig
    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private AppUserService appUserService;

    // WebSecurityConfig зависи от BCryptPasswordEncoder, затова трябва да го предоставим.
    // Тъй като не е мокнат, Spring ще създаде реален бийн.
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * Тест, който проверява дали страницата за вход се зарежда успешно.
     */
    @Test
    public void testShowLoginPage() throws Exception {
        // Този тест вече трябва да работи, защото WebSecurityConfig знае,
        // че "/login" трябва да се обработи от нашия контролер.
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    /**
     * Тест, който проверява дали страницата за регистрация се зарежда успешно.
     */
    @Test
    public void testShowRegisterPage() throws Exception {
        // Този тест вече трябва да работи, защото WebSecurityConfig знае,
        // че "/register" е публично достъпен.
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }
}