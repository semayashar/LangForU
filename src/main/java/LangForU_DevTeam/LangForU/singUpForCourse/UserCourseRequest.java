package LangForU_DevTeam.LangForU.singUpForCourse;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.courses.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Клас-ентитет (Entity), който представлява заявка от потребител за записване в курс.
 * Този запис съществува, докато заявката не бъде потвърдена от администратор.
 * След потвърждение, потребителят се добавя към списъка със студенти на курса.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"})) // Гарантира, че един потребител не може да направи повече от една заявка за един и същи курс.
public class UserCourseRequest {

    /**
     * Уникален идентификатор (ID) на заявката.
     * Генерира се автоматично от базата данни чрез sequence генератор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_course_requests_seq")
    @SequenceGenerator(name = "user_course_requests_seq", sequenceName = "user_course_requests_sequence", allocationSize = 1)
    private Long id;

    /**
     * Потребителят, който прави заявката за записване.
     * Връзка тип "много към едно".
     */
    @ManyToOne(cascade = CascadeType.REMOVE) // При изтриване на заявка, свързаният потребител не се изтрива, но ако се изтрие потребител, заявката може да се изтрие.
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Потребителят не може да бъде празен.")
    private AppUser user;

    /**
     * Курсът, за който се записва потребителят.
     * Връзка тип "много към едно".
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Операциите се отразяват и на свързания курс.
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Курсът не може да бъде празен.")
    private Course course;

    /**
     * ЕГН на потребителя, необходимо за издаване на сертификат.
     */
    @NotNull(message = "ЕГН не може да бъде празен.")
    @Pattern(regexp = "\\d{10}", message = "ЕГН трябва да бъде 10 цифри.")
    private String PIN;

    /**
     * Времеви маркер, показващ кога е направена заявката.
     */
    @NotNull(message = "Дата на заявката не може да бъде празна.")
    private LocalDateTime madeRequest;

    /**
     * Времеви маркер, показващ кога заявката е потвърдена от администратор.
     * Стойността е null, докато заявката не бъде потвърдена.
     */
    private LocalDateTime confirmedRequest;

    /**
     * Булев флаг, който показва дали заявката е потвърдена.
     * Стойност по подразбиране е 'false'.
     */
    @NotNull(message = "Полето за потвърждение не може да бъде празно.")
    private Boolean confirmed = false;

    /**
     * Уникален код, вероятно използван за проследяване или плащане на заявката.
     * Името 'codeIBAN' може да е подвеждащо, тъй като форматът е на UUID.
     */
    @NotNull(message = "Кодът не може да бъде празен.")
    @Pattern(regexp = "[a-fA-F0-9\\-]{36}", message = "Кодът трябва да бъде валиден UUID.")
    @Column(unique = true)
    private String codeIBAN;

    /**
     * Гражданство на потребителя.
     */
    @NotNull(message = "Гражданството не може да бъде празно.")
    @Size(min = 2, message = "Гражданството трябва да съдържа поне 2 символа.")
    private String citizenship;

    /**
     * Персонализиран конструктор за създаване на нова заявка за курс.
     * @param user Потребителят.
     * @param course Курсът.
     * @param PIN ЕГН на потребителя.
     * @param madeRequest Време на създаване на заявката.
     * @param codeIBAN Уникален код на заявката.
     * @param citizenship Гражданство.
     */
    public UserCourseRequest(AppUser user, Course course, String PIN, LocalDateTime madeRequest, String codeIBAN, String citizenship) {
        this.user = user;
        this.course = course;
        this.PIN = PIN;
        this.madeRequest = madeRequest;
        this.codeIBAN = codeIBAN;
        this.citizenship = citizenship;
    }
}