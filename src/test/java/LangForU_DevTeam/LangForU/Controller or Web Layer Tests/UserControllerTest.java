package LangForU_DevTeam.LangForU.appuser;

import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.security.config.WebSecurityConfig;
// Добавете този импорт
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// КОРЕКЦИЯ: Добавяме excludeAutoConfiguration, за да изключим библиотеката за документация
@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = {SpringDocConfiguration.class})
@Import(WebSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testGetAboutPage() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

}