package LangForU_DevTeam.LangForU.Data_Layer_Tests;

import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.Level;
import LangForU_DevTeam.LangForU.lections.Lection;
import LangForU_DevTeam.LangForU.lections.LectionRepository;
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
public class LectionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LectionRepository lectionRepository;

    private Course course;
    private Lection lection1;
    private Lection lection2;

    @BeforeEach
    public void setUp() {
        course = new Course(null, "German", Level.A2, 120.0f,
                LocalDate.now(), LocalDate.now().plusMonths(2), "German for beginners",
                "Hans Zimmer", "Assistant Greta", "Tech Klaus", "http://example.com/german.jpg", 4, null, null);
        entityManager.persist(course);

        lection1 = new Lection();
        lection1.setName("German Alphabet");
        lection1.setTheme("Basics");
        lection1.setReleaseDate(LocalDate.now());
        lection1.setCourse(course);
        lection1.setDifficultyLevel("Easy");
        lection1.setSummary("Summary text");
        lection1.setAdditionalResources("Resources text");
        entityManager.persist(lection1);

        lection2 = new Lection();
        lection2.setName("German Nouns");
        lection2.setTheme("Grammar");
        lection2.setReleaseDate(LocalDate.now().plusDays(1));
        lection2.setCourse(course);
        lection2.setDifficultyLevel("Medium");
        lection2.setSummary("Summary text 2");
        lection2.setAdditionalResources("Resources text 2");
        entityManager.persist(lection2);

        entityManager.flush();
    }

    @Test
    public void whenFindByCourseId_thenReturnLections() {
        List<Lection> foundLections = lectionRepository.findByCourseId(course.getId());
        assertThat(foundLections).hasSize(2).contains(lection1, lection2);
    }

    @Test
    public void whenFindAllByReleaseDate_thenReturnLectionsForThatDate() {
        List<Lection> lectionsForToday = lectionRepository.findAllByReleaseDate(LocalDate.now());
        List<Lection> lectionsForTomorrow = lectionRepository.findAllByReleaseDate(LocalDate.now().plusDays(1));
        assertThat(lectionsForToday).hasSize(1).contains(lection1);
        assertThat(lectionsForTomorrow).hasSize(1).contains(lection2);
    }
}