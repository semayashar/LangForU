package LangForU_DevTeam.LangForU.appuser;

import LangForU_DevTeam.LangForU.blog.BlogService;
import LangForU_DevTeam.LangForU.contactRequest.ContactRequestService;
import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.email.EmailService;
import LangForU_DevTeam.LangForU.finalexam.FinalExamService;
import LangForU_DevTeam.LangForU.lections.LectionService;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AppUserService appUserService;
    @Mock
    private UserCourseRequestService userCourseRequestService;
    @Mock
    private ContactRequestService contactRequestService;
    @Mock
    private EmailService emailService;
    @Mock
    private BlogService blogService;
    @Mock
    private LectionService lectionService;
    @Mock
    private CourseService courseService;
    @Mock
    private FinalExamService finalExamService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getEnabledUsersWithRoleUser_ShouldReturnUsers() throws Exception {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@user.com");
        user.setAppUserRole(AppUserRole.USER);
        user.setEnabled(true);

        when(appUserService.getEnabledUsersWithRoleUser()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/admin/users-list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test"))
                .andExpect(jsonPath("$[0].email").value("test@user.com"));
    }

    @Test
    void getUsers_ShouldReturnUsersView() throws Exception {
        when(appUserService.getAllUsersByRole(AppUserRole.USER)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void viewUnconfirmedUsers_ShouldReturnUnconfirmedUsersView() throws Exception {
        when(userCourseRequestService.getAllUnconfirmedUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/unconfirmedUsers"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/unconfirmedUsers"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void getAllContactRequests_ShouldReturnContactRequestsView() throws Exception {
        when(contactRequestService.getAllRequests()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/contactRequests"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/contactRequests"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void listBlogs_ShouldReturnBlogListView() throws Exception {
        when(blogService.findAllBlogs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/blogs"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog/blogList"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void listLections_ShouldReturnLectionListView() throws Exception {
        when(lectionService.getAllLections()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/lections"))
                .andExpect(status().isOk())
                .andExpect(view().name("lections/lectionList"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void listCourses_ShouldReturnCourseListView() throws Exception {
        when(courseService.getAllActiveCourses()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("course/courseList"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void showFinalExamsList_ShouldReturnFinalExamsListView() throws Exception {
        when(finalExamService.getAllFinalExams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/final-exams"))
                .andExpect(status().isOk())
                .andExpect(view().name("final-exams/finalExamsList"))
                .andExpect(model().attributeExists("message"));
    }
}