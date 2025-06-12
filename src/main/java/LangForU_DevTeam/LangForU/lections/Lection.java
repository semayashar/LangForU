package LangForU_DevTeam.LangForU.lections;

import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.question.Question;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас-ентитет (Entity), който представлява лекция или урок в рамките на даден курс.
 * Съдържа информация за темата, видео съдържание, дата на публикуване и други ресурси.
 */
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи, изискван от JPA.

@Entity // JPA: Посочва, че този клас е ентитет.
@Table(name = "lections") // JPA: Указва името на таблицата в базата данни.
public class Lection {

    /**
     * Уникален идентификатор (ID) на лекцията.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Име/Заглавие на лекцията.
     */
    @NotNull(message = "Името на лекцията не може да бъде празно.")
    @Column(nullable = false)
    private String name;

    /**
     * Основна тема на лекцията.
     */
    @NotNull(message = "Темата не може да бъде празна.")
    @Column(nullable = false)
    private String theme;

    /**
     * URL адрес на видеото към лекцията.
     */
    @Column(name = "video_url")
    private String videoUrl;

    /**
     * Ниво на трудност на лекцията (напр. "Начинаещ", "Напреднал").
     */
    @NotNull(message = "Нивото на трудност не може да бъде празно.")
    @Column(name = "difficulty_level", nullable = false)
    private String difficultyLevel;

    /**
     * Дата, на която лекцията става достъпна за потребителите.
     */
    @NotNull(message = "Дата на излъчване не може да бъде празна.")
    @FutureOrPresent(message = "Дата на излъчване трябва да бъде в бъдеще или настоящето.")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate releaseDate;

    /**
     * Име на преподавателя, водещ лекцията.
     */
    private String instructor;

    /**
     * Списък с въпроси (тест) към лекцията.
     * Връзка тип "едно към много".
     * @JsonManagedReference се използва за правилна сериализация в JSON, за да се избегне безкрайна рекурсия.
     */
    @OneToMany(mappedBy = "lection", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>();

    /**
     * Допълнителни ресурси към лекцията (линкове, файлове и др.).
     */
    @Column(name = "additional_resource", length = 10000, nullable = false)
    private String additionalResources;

    /**
     * Резюме на съдържанието на лекцията.
     */
    @NotNull(message = "Резюмето не може да бъде празно.")
    @Column(name = "lection_summary", length = 25000, nullable = false)
    private String summary;

    /**
     * Курсът, към който принадлежи тази лекция.
     * Връзка тип "много към едно".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Метод за валидация, който се изпълнява автоматично от JPA преди запис или обновяване.
     * Проверява дали датата на лекцията е в рамките на периода на курса.
     */
    @PrePersist
    @PreUpdate
    private void validateReleaseDate() {
        if (course == null) {
            throw new IllegalArgumentException("Лекцията трябва да бъде свързана с курс.");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Дата на излъчване не може да бъде празна.");
        }
        if (releaseDate.isBefore(course.getStartDate()) || releaseDate.isAfter(course.getEndDate())) {
            throw new IllegalArgumentException("Датата на излъчване на лекцията трябва да е в рамките на началната и крайната дата на курса.");
        }
    }

    /**
     * Проверява дали лекцията е достъпна към текущия момент.
     * @return {@code true} ако датата на излъчване е днес или е минала, в противен случай {@code false}.
     */
    public boolean hasBeenReleased() {
        return !releaseDate.isAfter(LocalDate.now());
    }

    /**
     * Пренаписан toString() метод за текстово представяне на обекта, полезен при дебъгване.
     * @return String представяне на лекцията.
     */
    @Override
    public String toString() {
        return "Lection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theme='" + theme + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", releaseDate=" + releaseDate +
                ", instructor='" + instructor + '\'' +
                ", additionalResources='" + additionalResources + '\'' +
                ", summary='" + summary + '\'' +
                ", courseId=" + (course != null ? course.getId() : null) +
                '}';
    }
}