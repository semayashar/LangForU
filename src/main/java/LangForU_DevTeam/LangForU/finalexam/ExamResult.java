package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Клас-ентитет (Entity), който съхранява резултата на даден потребител от конкретен финален изпит.
 * Включва разбивка на точките по раздели, общ резултат, обратна връзка и статус на преминаване.
 */
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи, изискван от JPA.

@Entity // JPA: Посочва, че този клас е ентитет.
@Table(name = "exam_results") // JPA: Указва името на таблицата в базата данни.
public class ExamResult {

    /**
     * Уникален идентификатор (ID) на резултата от изпита.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Финалният изпит, за който се отнася този резултат.
     * Връзка тип "много към едно" (много резултати могат да принадлежат на един изпит).
     */
    @ManyToOne
    @JoinColumn(name = "final_exam_id", nullable = false)
    private FinalExam finalExam;

    /**
     * Потребителят, който е положил изпита.
     * Връзка тип "много към едно" (много резултати могат да принадлежат на един потребител).
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    /**
     * Точки, получени от въпросите със затворен отговор (multiple choice).
     */
    @Column(name = "multiple_choice_score", nullable = false)
    private int multipleChoiceScore;

    /**
     * Точки, получени от въпросите с отворен отговор.
     */
    @Column(name = "open_ended_score", nullable = false)
    private int openEndedScore;

    /**
     * Точки, получени от есето.
     */
    @Column(name = "essay_score", nullable = false)
    private int essayScore;

    /**
     * Общият брой точки, получен на изпита. Изчислява се автоматично.
     */
    @Column(name = "final_score", nullable = false)
    private int finalScore;

    /**
     * Текстова обратна връзка за есето.
     */
    @Column(name = "essay_feedback", length = 5000)
    private String essayFeedback;

    /**
     * Булев флаг, който показва дали изпитът е преминат успешно.
     */
    @Column(name = "passed", nullable = false)
    private boolean passed;

    /**
     * Персонализиран конструктор за създаване на нов резултат от изпит.
     * Той автоматично изчислява общия резултат (`finalScore`) като сума
     * от точките от отделните компоненти.
     *
     * @param finalExam           Изпитът, за който е резултатът.
     * @param user                Потребителят, положил изпита.
     * @param multipleChoiceScore Точки от въпросите със затворен отговор.
     * @param openEndedScore      Точки от въпросите с отворен отговор.
     * @param essayScore          Точки от есето.
     * @param essayFeedback       Обратна връзка за есето.
     * @param passed              Дали изпитът е преминат успешно.
     */
    public ExamResult(FinalExam finalExam, AppUser user, int multipleChoiceScore, int openEndedScore, int essayScore, String essayFeedback, boolean passed) {
        this.finalExam = finalExam;
        this.user = user;
        this.multipleChoiceScore = multipleChoiceScore;
        this.openEndedScore = openEndedScore;
        this.essayScore = essayScore;
        this.essayFeedback = essayFeedback;
        this.passed = passed;
        // Автоматично изчисляване на крайния резултат.
        this.finalScore = multipleChoiceScore + openEndedScore + essayScore;
    }

}