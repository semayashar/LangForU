package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseRepository;
import LangForU_DevTeam.LangForU.question.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Модулни тестове за класа FinalExamService.
 * Тестват логиката за управление на финални изпити и резултати,
 * като изолират зависимостите към хранилищата.
 */
@ExtendWith(MockitoExtension.class)
class FinalExamServiceTest {

    //<editor-fold desc="Mocks and InjectMocks">
    @Mock
    private FinalExamRepository finalExamRepository;
    @Mock
    private ExamResultRepository examResultRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private QuestionRepository questionRepository; // Макар и да не се ползва директно, е зависимост

    @InjectMocks
    private FinalExamService finalExamService;
    //</editor-fold>

    private AppUser testUser;
    private Course testCourse;
    private FinalExam testFinalExam;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1L);

        testCourse = new Course();
        testCourse.setId(10L);
        testCourse.setFinalExam(null); // В началото курсът няма изпит

        testFinalExam = new FinalExam();
        testFinalExam.setId(100L);
        testFinalExam.setExamQuestions(new ArrayList<>());
    }

    @Test
    void createFinalExam_whenCalled_shouldCallRepositorySave() {
        // Arrange
        when(finalExamRepository.save(any(FinalExam.class))).thenReturn(testFinalExam);

        // Act
        FinalExam createdExam = finalExamService.createFinalExam(testFinalExam);

        // Assert
        assertNotNull(createdExam);
        assertEquals(100L, createdExam.getId());
        verify(finalExamRepository).save(testFinalExam);
    }

    @Test
    void deleteFinalExamById_whenExamExistsAndIsLinkedToCourse_shouldUnlinkAndDetele() {
        // Arrange
        // Свързваме курса и изпита двупосочно
        testCourse.setFinalExam(testFinalExam);
        testFinalExam.setCourse(testCourse);

        when(finalExamRepository.findById(100L)).thenReturn(Optional.of(testFinalExam));

        // Act
        finalExamService.deleteFinalExamById(100L);

        // Assert
        // Проверяваме дали връзката от страна на курса е премахната
        assertNull(testCourse.getFinalExam());
        // Проверяваме дали промяната в курса е запазена
        verify(courseRepository).save(testCourse);
        // Проверяваме дали самият изпит е изтрит
        verify(finalExamRepository).delete(testFinalExam);
    }

    @Test
    void deleteFinalExamById_whenExamNotFound_shouldThrowEntityNotFoundException() {
        // Arrange
        when(finalExamRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            finalExamService.deleteFinalExamById(99L);
        });
        // Проверяваме, че не се прави опит за изтриване
        verify(finalExamRepository, never()).delete(any());
    }

    @Test
    void getFinalExamById_whenExamExists_shouldReturnExam() {
        // Arrange
        when(finalExamRepository.findById(100L)).thenReturn(Optional.of(testFinalExam));

        // Act
        FinalExam foundExam = finalExamService.getFinalExamById(100L);

        // Assert
        assertNotNull(foundExam);
        assertEquals(100L, foundExam.getId());
    }

    @Test
    void submitNewExamResult_whenNoExistingResult_shouldSaveNewResult() {
        // Arrange
        when(examResultRepository.findByFinalExamIdAndUserId(100L, 1L)).thenReturn(Optional.empty());
        when(finalExamRepository.findById(100L)).thenReturn(Optional.of(testFinalExam));

        // Act
        finalExamService.submitNewExamResult(100L, testUser, 80, 10, 5, 95, true, "Excellent");

        // Assert
        // Проверяваме, че НЕ е извикан метод за триене
        verify(examResultRepository, never()).delete(any());

        // "Хващаме" резултата, който се подава за запис, за да го проверим
        ArgumentCaptor<ExamResult> resultCaptor = ArgumentCaptor.forClass(ExamResult.class);
        verify(examResultRepository).save(resultCaptor.capture());
        ExamResult savedResult = resultCaptor.getValue();

        assertEquals(testUser, savedResult.getUser());
        assertEquals(testFinalExam, savedResult.getFinalExam());
        assertEquals(95, savedResult.getFinalScore());
        assertTrue(savedResult.isPassed());
    }

    @Test
    void submitNewExamResult_whenResultExists_shouldDeleteOldAndSaveNew() {
        // Arrange
        ExamResult existingResult = new ExamResult();
        existingResult.setId(500L);

        when(examResultRepository.findByFinalExamIdAndUserId(100L, 1L)).thenReturn(Optional.of(existingResult));
        when(finalExamRepository.findById(100L)).thenReturn(Optional.of(testFinalExam));

        // Act
        finalExamService.submitNewExamResult(100L, testUser, 70, 0, 0, 70, true, "Good");

        // Assert
        // Проверяваме, че старият резултат е изтрит
        verify(examResultRepository).delete(existingResult);
        // Проверяваме, че новият резултат е записан
        verify(examResultRepository).save(any(ExamResult.class));
    }
}