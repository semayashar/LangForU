package LangForU_DevTeam.LangForU.courses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Модулни тестове за класа CourseService.
 * Тества логиката за управление на курсове, базирано стриктно на методите в сервиза.
 */
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    //<editor-fold desc="Mocks and InjectMocks">
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;
    //</editor-fold>

    private Course testCourse;

    @BeforeEach
    void setUp() {
        // Подготвяме тестови данни съобразени с реалния Course клас
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setLanguage("English");
        testCourse.setLevel(Level.B1);
        testCourse.setPrice(100.0f);
        testCourse.setStartDate(LocalDate.now().plusDays(1));
        testCourse.setEndDate(LocalDate.now().plusMonths(3));
        testCourse.setDescription("A test course for English learners.");
        testCourse.setMainInstructorName("John Doe");
        testCourse.setAssistantInstructorName("Jane Smith");
        testCourse.setTechnicianName("Tech Guy");
        testCourse.setPictureUrl("/img/test.png");
    }

    @Test
    void findCourseById_whenCourseExists_shouldReturnCourse() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Course foundCourse = courseService.findCourseById(1L);

        // Assert
        assertNotNull(foundCourse);
        assertEquals(1L, foundCourse.getId());
        verify(courseRepository).findById(1L);
    }

    @Test
    void findCourseById_whenCourseDoesNotExist_shouldThrowNoSuchElementException() {
        // Arrange
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            courseService.findCourseById(99L);
        });
    }

    @Test
    void saveCourse_whenCalled_shouldInvokeRepositorySave() {
        // Arrange
        // Не е нужна подготовка, само ще проверим извикването

        // Act
        courseService.saveCourse(testCourse);

        // Assert
        verify(courseRepository).save(testCourse);
    }

    @Test
    void deleteCourseById_whenCalled_shouldInvokeRepositoryDelete() {
        // Arrange
        doNothing().when(courseRepository).deleteById(1L);

        // Act
        courseService.deleteCourseById(1L);

        // Assert
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void getAllCourses_whenCalled_shouldReturnListOfCourses() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(List.of(testCourse));

        // Act
        List<Course> courses = courseService.getAllCourses();

        // Assert
        assertNotNull(courses);
        assertEquals(1, courses.size());
        verify(courseRepository).findAll();
    }

    @Test
    void getAllActiveCourses_whenCalled_shouldInvokeRepositoryMethod() {
        // Arrange
        when(courseRepository.findActiveCourses(any(LocalDate.class))).thenReturn(List.of(testCourse));

        // Act
        List<Course> activeCourses = courseService.getAllActiveCourses();

        // Assert
        assertNotNull(activeCourses);
        assertEquals(1, activeCourses.size());
        verify(courseRepository).findActiveCourses(any(LocalDate.class));
    }

    @Test
    void getCoursesByLanguage_whenCalled_shouldInvokeRepositoryMethod() {
        // Arrange
        String language = "English";
        when(courseRepository.findByLanguageContainingIgnoreCase(language)).thenReturn(List.of(testCourse));

        // Act
        List<Course> courses = courseService.getCoursesByLanguage(language);

        // Assert
        assertNotNull(courses);
        assertEquals(1, courses.size());
        verify(courseRepository).findByLanguageContainingIgnoreCase(language);
    }

    @Test
    void findCoursesWithoutFinalExam_whenCalled_shouldInvokeRepositoryMethod() {
        // Arrange
        // ПОПРАВКА: Конфигурираме мока за правилния метод
        when(courseRepository.findCoursesWithoutFinalExamJPQL()).thenReturn(List.of(testCourse));

        // Act
        List<Course> courses = courseService.findCoursesWithoutFinalExam();

        // Assert
        assertNotNull(courses);
        assertEquals(1, courses.size());
        // ПОПРАВКА: Проверяваме извикването на правилния метод
        verify(courseRepository).findCoursesWithoutFinalExamJPQL();
    }
}