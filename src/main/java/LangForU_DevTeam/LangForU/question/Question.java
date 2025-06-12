package LangForU_DevTeam.LangForU.question;

import LangForU_DevTeam.LangForU.finalexam.FinalExam;
import LangForU_DevTeam.LangForU.lections.Lection;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас-ентитет (Entity), който представлява въпрос (упражнение).
 * Един въпрос може да бъде част или от лекция ({@link Lection}), или от финален изпит ({@link FinalExam}),
 * но не и от двете едновременно.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "questions")
public class Question {

    /**
     * Уникален идентификатор (ID) на въпроса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текстът на самия въпрос.
     */
    @NotNull(message = "Въпросът не може да бъде празен.")
    @Size(min = 1, message = "Въпросът не може да бъде празен.")
    @Column(nullable = false, length = 5000)
    private String question;

    /**
     * Списък с възможни отговори (за въпроси със затворен отговор).
     * Съхранява се в отделна таблица 'question_possible_answers'.
     * Ако списъкът е празен, се счита, че въпросът е с отворен отговор.
     */
    @ElementCollection
    @CollectionTable(name = "question_possible_answers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "possible_answer", length = 5000)
    private List<String> possibleAnswers = new ArrayList<>();

    /**
     * Правилният отговор на въпроса.
     */
    @NotNull(message = "Правилният отговор не може да бъде празен.")
    @Column(name = "correct_answer", nullable = false, length = 5000)
    private String correctAnswer;

    /**
     * Лекцията, към която принадлежи този въпрос (ако е част от лекция).
     * @JsonBackReference се използва за правилна сериализация в JSON, за да се избегне безкрайна рекурсия.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lection_id")
    @JsonBackReference
    private Lection lection;

    /**
     * Финалният изпит, към който принадлежи този въпрос (ако е част от изпит).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_exam_id")
    @JsonBackReference
    private FinalExam finalExam;

    /**
     * Конструктор за създаване на въпрос със затворен отговор (multiple-choice).
     * @param question Текстът на въпроса.
     * @param possibleAnswers Списък с възможни отговори.
     * @param correctAnswer Правилният отговор.
     */
    public Question(String question, List<String> possibleAnswers, String correctAnswer) {
        this.question = question;
        this.possibleAnswers = (possibleAnswers != null) ? possibleAnswers : new ArrayList<>();
        this.correctAnswer = correctAnswer;
        validateCorrectAnswer();
    }

    /**
     * Конструктор за създаване на въпрос с отворен отговор.
     * @param question Текстът на въпроса.
     * @param correctAnswer Правилният отговор (или примерен такъв).
     */
    public Question(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.possibleAnswers = new ArrayList<>();
    }

    /**
     * Помощен метод, който валидира дали правилният отговор е един от възможните.
     * Извиква се в конструктора за въпроси със затворен отговор.
     */
    private void validateCorrectAnswer() {
        if (!possibleAnswers.isEmpty() && !possibleAnswers.contains(correctAnswer)) {
            throw new IllegalArgumentException("Правилният отговор трябва да бъде един от възможните отговори.");
        }
    }

    /**
     * Метод за валидация, който се изпълнява автоматично от JPA преди запис или обновяване.
     * Гарантира, че въпросът е свързан или с лекция, или с финален изпит, но не и с двете, нито с нито едно.
     */
    @PrePersist
    @PreUpdate
    private void validateLinking() {
        if ((lection == null && finalExam == null) || (lection != null && finalExam != null)) {
            throw new IllegalStateException("Упражнението трябва да бъде свързано или с лекция, или с финален изпит, но не и с двете едновременно.");
        }
    }

    /**
     * Пренаписан toString() метод за текстово представяне на обекта.
     * Отпечатва само ID-тата на свързаните обекти, за да се избегнат проблеми с lazy loading.
     * @return String представяне на въпроса.
     */
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", lection=" + (lection != null ? lection.getId() : null) +
                ", finalExam=" + (finalExam != null ? finalExam.getId() : null) +
                '}';
    }
}