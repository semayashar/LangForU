package LangForU_DevTeam.LangForU.Data_Layer_Tests;

import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseRepository;
import LangForU_DevTeam.LangForU.courses.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        Course course1 = new Course(null, "English", Level.A1, 100.0f,
                LocalDate.now(), LocalDate.now().plusMonths(3), "Basic English course",
                "John Doe", "Assistant Jane", "Tech Guy", "http://example.com/course1.jpg", 5, null, null);

        Course course2 = new Course(null, "Spanish", Level.B1, 150.0f,
                LocalDate.now(), LocalDate.now().plusMonths(4), "Intermediate Spanish",
                "Jane Smith", "Assistant Juan", "Tech Girl", "http://example.com/course2.jpg", 4, null, null);
        course2.setFinalExam(null);

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush();
    }

    @Test
    public void whenFindByLanguageContainingIgnoreCase_thenReturnMatchingCourses() {
        List<Course> englishCourses = courseRepository.findByLanguageContainingIgnoreCase("english");
        assertThat(englishCourses).hasSize(1);
        assertThat(englishCourses.get(0).getLanguage()).isEqualTo("English");
    }

    @Test
    public void whenFindByDescriptionContaining_thenReturnMatchingCourses() {
        List<Course> basicCourses = courseRepository.findByDescriptionContaining("Basic");
        assertThat(basicCourses).hasSize(1);
        assertThat(basicCourses.get(0).getDescription()).contains("Basic English course");
    }

    @Test
    public void whenFindByFinalExamIsNull_thenReturnCoursesWithoutFinalExam() {
        List<Course> coursesWithoutExam = courseRepository.findByFinalExamIsNull();
        assertThat(coursesWithoutExam).hasSize(2);
    }
}