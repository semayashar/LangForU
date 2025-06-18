package LangForU_DevTeam.LangForU.Data_Layer_Tests;

import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.Level;
import LangForU_DevTeam.LangForU.finalexam.FinalExam;
import LangForU_DevTeam.LangForU.finalexam.FinalExamRepository;
import LangForU_DevTeam.LangForU.question.Question; // Импортирайте Question
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Collections; // Импортирайте Collections
import java.util.List; // Импортирайте List
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FinalExamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FinalExamRepository finalExamRepository;

    @Test
    public void whenSaveAndFindById_thenCorrect() {
        // given
        Course course = new Course(null, "French", Level.C1, 200.0f,
                LocalDate.now(), LocalDate.now().plusMonths(5), "Advanced French",
                "Pierre Martin", "Assistant Marie", "Tech Support", "http://example.com/french.jpg", 5, null, null);
        entityManager.persist(course);

        // КОРЕКЦИЯ: Създаваме въпрос и го добавяме към изпита
        Question question = new Question("Translate 'Book'", "Livre");
        FinalExam exam = new FinalExam(course, List.of(question), "French Literature Essay");

        // ВАЖНО: Вашият конструктор вече се грижи за това, но за пълнота,
        // ето как се установява двупосочната връзка
        question.setFinalExam(exam);

        entityManager.persist(exam);
        entityManager.flush();

        // when
        Optional<FinalExam> foundExam = finalExamRepository.findById(exam.getId());

        // then
        assertThat(foundExam.isPresent()).isTrue();
        assertThat(foundExam.get().getName()).isEqualTo(exam.getName());
        assertThat(foundExam.get().getCourse().getLanguage()).isEqualTo("French");
        assertThat(foundExam.get().getExamQuestions()).hasSize(1);
    }
}