package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.Sevi.ChatGPTService;
import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRole;
import LangForU_DevTeam.LangForU.certificate.CertificateGeneratorService;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.question.Question;
import LangForU_DevTeam.LangForU.question.QuestionRepository;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequest;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@WebMvcTest(FinalExamController.class)
class FinalExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatGPTService chatGPTService;
    @MockBean
    private FinalExamService finalExamService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private CertificateGeneratorService certificateGeneratorService;
    @MockBean
    private UserCourseRequestService userCourseRequestService;
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private ExamResultRepository examResultRepository;

    private AppUser testUser;
    private Course testCourse;
    private FinalExam testFinalExam;
    private Question testQuestion1;
    private Question testQuestion2;

    @BeforeEach
    void setUp() {
        testUser = new AppUser(1L, "/img/avatar.png", "test@example.com", "password", "Test User", LocalDate.now().minusYears(20), "Male", "Test Address", AppUserRole.USER, true, new ArrayList<>());

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setLanguage("English B2");
        testCourse.setStartDate(LocalDate.now().minusMonths(2));
        testCourse.setEndDate(LocalDate.now());

        testQuestion1 = new Question("What is 1+1?", List.of("1", "2", "3"), "2");
        testQuestion1.setId(101L);

        testQuestion2 = new Question("Capital of Bulgaria?", "Sofia");
        testQuestion2.setId(102L);

        testFinalExam = new FinalExam();
        testFinalExam.setId(1L);
        testFinalExam.setCourse(testCourse);
        testFinalExam.setExamDate(LocalDate.now());
        testFinalExam.setEssayTopic("My Summer Vacation");
        testFinalExam.setExamQuestions(Arrays.asList(testQuestion1, testQuestion2));

        testQuestion1.setFinalExam(testFinalExam);
        testQuestion2.setFinalExam(testFinalExam);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void viewAllFinalExamsForAdmin_ShouldReturnListView() throws Exception {
        when(finalExamService.getAllFinalExams()).thenReturn(Collections.singletonList(testFinalExam));

        mockMvc.perform(get("/final-exams/admin/view-all"))
                .andExpect(status().isOk())
                .andExpect(view().name("final-exams/finalExamsList"))
                .andExpect(model().attribute("finalExams", hasSize(1)));
    }

    @Test
    void viewFinalExamDetailsByUser_WhenNotEnrolled_ShouldRedirectWithErrorMessage() throws Exception {
        when(finalExamService.getFinalExamById(1L)).thenReturn(testFinalExam);
        when(userCourseRequestService.findByUserAndCourse(testUser.getId(), testCourse.getId())).thenReturn(null);

        mockMvc.perform(get("/final-exams/view/{id}", 1L).with(user(testUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("errorMessage", "Нямате достъп до този изпит. Не сте записан в съответния курс."));
    }

    @Test
    void viewFinalExamDetailsByUser_WhenExamDateIsNotToday_ShouldRedirectWithErrorMessage() throws Exception {
        testFinalExam.setExamDate(LocalDate.now().plusDays(1));
        UserCourseRequest request = new UserCourseRequest();
        when(finalExamService.getFinalExamById(1L)).thenReturn(testFinalExam);
        when(userCourseRequestService.findByUserAndCourse(testUser.getId(), testCourse.getId())).thenReturn(request);

        // Очакваният формат на датата трябва да е "dd.MM.yyyy"
        String expectedDate = testFinalExam.getExamDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        mockMvc.perform(get("/final-exams/view/{id}", 1L).with(user(testUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                // Променете очакваното съобщение тук
                .andExpect(flash().attribute("errorMessage", "Финалният изпит ще бъде достъпен на " + expectedDate + "."));
    }

    @Test
    void viewFinalExamDetailsByUser_WhenAccessIsGranted_ShouldReturnView() throws Exception {
        UserCourseRequest request = new UserCourseRequest();
        when(finalExamService.getFinalExamById(1L)).thenReturn(testFinalExam);
        when(userCourseRequestService.findByUserAndCourse(testUser.getId(), testCourse.getId())).thenReturn(request);

        mockMvc.perform(get("/final-exams/view/{id}", 1L).with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("final-exams/viewFinalExam"))
                .andExpect(model().attribute("finalExam", testFinalExam));
    }

    @Test
    void testParseQuestions_ShouldReturnCorrectListOfQuestions() {
        FinalExamController controller = new FinalExamController(chatGPTService, finalExamService, courseService, certificateGeneratorService, userCourseRequestService, questionRepository, examResultRepository);
        String examQuestionsStr = "What is Java?---OOP=Scripting---OOP;Is Spring a framework?---***---Yes";

        List<Question> questions = controller.parseQuestions(examQuestionsStr, testFinalExam);

        assertEquals(2, questions.size());

        Question q1 = questions.get(0);
        assertEquals("What is Java?", q1.getQuestion());
        assertEquals(List.of("OOP", "Scripting"), q1.getPossibleAnswers());
        assertEquals("OOP", q1.getCorrectAnswer());

        Question q2 = questions.get(1);
        assertEquals("Is Spring a framework?", q2.getQuestion());
        assertEquals(Collections.emptyList(), q2.getPossibleAnswers());
        assertEquals("Yes", q2.getCorrectAnswer());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFinalExamViaGet_ShouldDeleteAndRedirect() throws Exception {
        doNothing().when(finalExamService).deleteFinalExamById(1L);

        mockMvc.perform(get("/final-exams/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/final-exams"))
                .andExpect(flash().attribute("successMessage", "Финалният изпит е изтрит успешно."));

        verify(finalExamService, times(1)).deleteFinalExamById(1L);
    }

    @Test
    void submitFinalExam_WhenPassed_ShouldCalculateScoreAndShowResults() throws Exception {
        when(finalExamService.getFinalExamById(1L)).thenReturn(testFinalExam);

        // FIX 1: Увеличаваме точките от есето, за да мине изпита (1 + 2 + 28 = 31 >= 30)
        String gptResponseJson = new ObjectMapper().writeValueAsString(
                Map.of("общо", 28, "коментар", "Excellent essay.")
        );
        when(chatGPTService.askChatGPT(anyString())).thenReturn(gptResponseJson);

        ResultActions resultActions = mockMvc.perform(post("/final-exams/submit/{id}", 1L)
                .with(csrf())
                .with(user(testUser))
                .param("question_" + testQuestion1.getId(), "2")
                .param("question_" + testQuestion2.getId(), "Some answer")
                .param("essayAnswer", "This is my essay."));

        resultActions.andExpect(status().isOk())
                .andExpect(view().name("final-exams/finalExamResults"))
                .andExpect(model().attribute("shouldDownload", true)) // Вече трябва да е true
                .andExpect(model().attribute("result", hasProperty("passed", is(true))));

        ArgumentCaptor<Integer> scoreCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(finalExamService).submitNewExamResult(
                eq(1L), eq(testUser),
                scoreCaptor.capture(), scoreCaptor.capture(),
                scoreCaptor.capture(), scoreCaptor.capture(),
                eq(true), // Проверката за `passed` вече трябва да е true
                anyString());

        List<Integer> scores = scoreCaptor.getAllValues();
        assertEquals(1, scores.get(0));
        assertEquals(2, scores.get(1));
        assertEquals(28, scores.get(2)); // Променената оценка от есето
        assertEquals(31, scores.get(3)); // Новият общ резултат
    }

    @Test
    void downloadCertificate_WhenNotPassed_ShouldReturnForbidden() throws Exception {
        ExamResult examResult = new ExamResult(testFinalExam, testUser, 10, 0, 5, "", false);
        when(examResultRepository.findByFinalExamIdAndUserId(1L, testUser.getId()))
                .thenReturn(Optional.of(examResult));

        mockMvc.perform(get("/final-exams/{id}/certificate", 1L).with(user(testUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void downloadCertificate_WhenPassed_ShouldReturnPdf() throws Exception {
        ExamResult examResult = new ExamResult(testFinalExam, testUser, 20, 2, 25, "", true);
        examResult.setFinalScore(47);
        UserCourseRequest request = new UserCourseRequest();
        byte[] pdfBytes = "This is a dummy PDF".getBytes();

        when(examResultRepository.findByFinalExamIdAndUserId(1L, testUser.getId()))
                .thenReturn(Optional.of(examResult));
        when(userCourseRequestService.findByUserAndCourse(testUser.getId(), testCourse.getId()))
                .thenReturn(request);
        when(certificateGeneratorService.generateCertificateAsBytes(eq(request), eq(47)))
                .thenReturn(pdfBytes);

        mockMvc.perform(get("/final-exams/{id}/certificate", 1L).with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdfBytes));
    }
}