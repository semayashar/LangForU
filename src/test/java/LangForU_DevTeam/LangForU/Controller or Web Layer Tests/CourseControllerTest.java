package LangForU_DevTeam.LangForU.courses;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRepository;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.lections.Lection;
import LangForU_DevTeam.LangForU.lections.LectionService;
import LangForU_DevTeam.LangForU.security.config.WebConfig;
import LangForU_DevTeam.LangForU.security.config.WebSecurityConfig;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequest;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary; // Импортваме @Primary
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
@Import({WebSecurityConfig.class, WebConfig.class})
class CourseControllerTest {

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        @Primary // РЕШЕНИЕ: Указваме, че това е основният UserDetailsService бийн
        public UserDetailsService userDetailsService() {
            AppUser testUser = new AppUser();
            testUser.setId(1L);
            testUser.setEmail("test@user.com");
            testUser.setPassword("password");
            testUser.setEnabled(true);
            testUser.setAppUserRole(LangForU_DevTeam.LangForU.appuser.AppUserRole.USER);

            return username -> {
                if (username.equals("test@user.com")) {
                    return testUser;
                }
                throw new UsernameNotFoundException("User not found: " + username);
            };
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private LectionService lectionService;
    @MockBean
    private UserCourseRequestService userCourseRequestService;
    @MockBean
    private AppUserRepository appUserRepository;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    private CourseRepository courseRepository;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setEmail("test@user.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        testUser.setAppUserRole(LangForU_DevTeam.LangForU.appuser.AppUserRole.USER);

        when(appUserService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    @WithMockUser
    void getAllCourses_ShouldReturnCoursesView() throws Exception {
        when(courseService.getAllCourses()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    @WithMockUser
    void getCourseDetails_ShouldReturnCourseDetailsView() throws Exception {
        Course course = new Course();
        course.setId(1L);
        course.setStartDate(LocalDate.now());
        course.setEndDate(LocalDate.now().plusMonths(2));

        when(courseService.findCourseById(1L)).thenReturn(course);
        when(lectionService.getLectionsByCourseId(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/courses/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("course/viewCourse"))
                .andExpect(model().attribute("course", course))
                .andExpect(model().attributeExists("lections"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showAddCourseForm_ShouldReturnAddCourseFormView() throws Exception {
        mockMvc.perform(get("/courses/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("course/addCourse"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    @WithUserDetails("test@user.com")
    void showSignupForm_ForAuthenticatedUser_ShouldReturnSignupFormView() throws Exception {
        Course course = new Course();
        course.setId(1L);
        when(courseService.findCourseById(1L)).thenReturn(course);

        mockMvc.perform(get("/courses/signup/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("course/signup"))
                .andExpect(model().attributeExists("course", "pin"));
    }

    @Test
    @WithUserDetails("test@user.com")
    void signupForCourse_ShouldCreateRequestAndRedirect() throws Exception {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(150.0f);

        UserCourseRequest request = new UserCourseRequest();
        request.setId(99L);

        when(courseService.findCourseById(1L)).thenReturn(course);
        when(userCourseRequestService.existsByUserIdAndCourseId(testUser.getId(), course.getId())).thenReturn(false);
        when(userCourseRequestService.createRequest(anyLong(), anyLong(), anyString(), anyString())).thenReturn(request);

        mockMvc.perform(post("/courses/signup/1")
                        .param("pin", "1234567890")
                        .param("citizenship", "Bulgarian")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/signedup/successfully?userId=1&courseId=1&requestId=99"));

        verify(userCourseRequestService).createRequest(eq(1L), eq(1L), eq("1234567890"), eq("Bulgarian"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCourse_ShouldDeleteAndRedirect() throws Exception {
        mockMvc.perform(get("/courses/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseService).deleteCourseById(1L);
    }
}