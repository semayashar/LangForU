package LangForU_DevTeam.LangForU.SecurityTesting;

import LangForU_DevTeam.LangForU.appuser.AdminController;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.blog.BlogService;
import LangForU_DevTeam.LangForU.contactRequest.ContactRequestService;
import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.email.EmailService;
import LangForU_DevTeam.LangForU.finalexam.FinalExamService;
import LangForU_DevTeam.LangForU.lections.LectionService;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    // Мокваме всички зависимости на AdminController,
    // защото тестваме само сигурността на уеб нивото.
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private UserCourseRequestService userCourseRequestService;
    @MockBean
    private ContactRequestService contactRequestService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private BlogService blogService;
    @MockBean
    private LectionService lectionService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private FinalExamService finalExamService;

    @Test
    void testAdminPanelAccess_ForUnauthenticatedUser_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin-panel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdminPanelAccess_ForUserRole_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/admin-panel"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Симулираме логнат потребител с роля ADMIN
    void testAdminPanelAccess_ForAdminRole_ShouldBeOk() throws Exception {
        // Сценарий 3: Потребителят е логнат с роля ADMIN
        // Опитваме достъп до /admin-panel
        mockMvc.perform(get("/admin-panel"))
                .andExpect(status().isOk()) // Очакваме статус 200 OK
                .andExpect(view().name("admin/adminDashboard")); // ... и да се зареди правилният темплейт
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUserManagementAccess_ForUserRole_ShouldBeForbidden() throws Exception {
        // Сценарий 4: Потребител с роля USER се опитва да види списъка с потребители
        mockMvc.perform(get("/admin/users/all"))
                .andExpect(status().isForbidden()); // Очакваме статус 403 Forbidden
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUserManagementAccess_ForAdminRole_ShouldBeOk() throws Exception {
        // Сценарий 5: Потребител с роля ADMIN вижда списъка с потребители
        mockMvc.perform(get("/admin/users/all"))
                .andExpect(status().isOk()); // Очакваме статус 200 OK
    }


}