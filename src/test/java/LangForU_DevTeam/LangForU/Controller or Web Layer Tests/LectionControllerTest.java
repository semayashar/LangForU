package LangForU_DevTeam.LangForU.lections;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRole;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.email.EmailSender;
import LangForU_DevTeam.LangForU.email.EmailTemplateService;
import LangForU_DevTeam.LangForU.question.Question;
import LangForU_DevTeam.LangForU.question.QuestionAnswerService;
import LangForU_DevTeam.LangForU.security.config.WebConfig;
import LangForU_DevTeam.LangForU.security.config.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LectionController.class)
@Import({WebSecurityConfig.class, WebConfig.class})
class LectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //<editor-fold desc="Mock Beans">
    @MockBean
    private LectionService lectionService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private QuestionAnswerService questionAnswerService;
    @MockBean
    private EmailSender emailSender;
    @MockBean
    private EmailTemplateService emailTemplateService;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //</editor-fold>

    @Test
    @WithMockUser(roles = "ADMIN")
    void showAddLectionForm_ShouldReturnAddLectionView() throws Exception {
        when(courseService.getAllCourses()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/lections/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("lections/addLection"))
                .andExpect(model().attributeExists("lection", "courses"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addLection_ShouldCreateLectionAndRedirect() throws Exception {
        Course course = new Course();
        course.setStartDate(LocalDate.now().minusDays(1));
        course.setEndDate(LocalDate.now().plusDays(1));
        when(courseService.findCourseById(anyLong())).thenReturn(course);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);

        mockMvc.perform(post("/lections/add")
                        .param("courseId", "1")
                        .param("name", "Test Lection")
                        .param("releaseDate", formattedDate)
                        .param("examQuestions", "Q1---A=B---A;Q2---***---OpenAnswer")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/lections"));

        verify(lectionService).save(any(Lection.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void viewLection_ShouldReturnLectionView() throws Exception {
        // 1. Setup mock data
        Course course = new Course();
        course.setId(1L);
        course.setLanguage("Тестов курс"); // Corrected method name
        course.setDescription("Описание");
        course.setMainInstructorName("Преподавател"); // Corrected method name
        course.setStartDate(LocalDate.now());
        course.setEndDate(LocalDate.now().plusDays(10));

        Lection lection = new Lection();
        lection.setId(1L);
        lection.setName("Тестова лекция");
        lection.setCourse(course);
        lection.setReleaseDate(LocalDate.now());

        AppUser adminUser = new AppUser();
        adminUser.setEmail("admin@test.com");
        adminUser.setAppUserRole(AppUserRole.ADMIN);

        // 2. Mock service calls
        when(lectionService.findById(1L)).thenReturn(lection);
        when(appUserService.getCurrentUser()).thenReturn(adminUser);

        // 3. Perform the request and assert
        mockMvc.perform(get("/lections/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("lections/lection")) // Correct view name
                .andExpect(model().attributeExists("lection"))
                .andExpect(model().attribute("lection", lection));
    }

    @Test
    @WithMockUser(username = "test@user.com")
    void submitAnswers_ShouldCalculateResultAndReturnJson() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("test@user.com");

        Question question = new Question();
        question.setId(1L);
        question.setCorrectAnswer("Correct");

        Lection lection = new Lection();
        lection.setQuestions(List.of(question));

        when(appUserService.getCurrentUser()).thenReturn(user); // Mock the actual called method
        when(lectionService.findById(anyLong())).thenReturn(lection);

        mockMvc.perform(post("/lections/submit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("question_" + question.getId(), "Correct")
                        .param("lectionId", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.questions[0].answeredCorrectly").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteLection_ShouldDeleteAndRedirect() throws Exception {
        mockMvc.perform(get("/lections/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/lections"));

        verify(lectionService).deleteLectionById(1L);
    }
}
