package LangForU_DevTeam.LangForU.question;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Клас-ентитет (Entity), който съхранява отговора на даден потребител на конкретен въпрос.
 * Този клас записва какво е отговорил потребителят, дали отговорът е верен
 * и кога е даден.
 */
@Entity // JPA: Посочва, че този клас е ентитет.
@Table(name = "Youtubes") // JPA: Указва името на таблицата в базата данни.
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи, изискван от JPA.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
public class QuestionAnswer {

    /**
     * Уникален идентификатор (ID) на записания отговор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Потребителят, който е дал отговора.
     * Връзка тип "много към едно".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    /**
     * Въпросът, на който е отговорено.
     * Връзка тип "много към едно".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    /**
     * Текстът на отговора, даден от потребителя.
     */
    @Column(name = "user_answer", nullable = false, length = 5000)
    private String userAnswer;

    /**
     * Булев флаг, който показва дали даденият отговор е верен.
     */
    @Column(name = "is_answered_correctly", nullable = false)
    private boolean answeredCorrectly;

    /**
     * Времеви маркер (timestamp), показващ кога е даден отговорът.
     */
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}