package LangForU_DevTeam.LangForU.courses;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.finalexam.FinalExam;
import LangForU_DevTeam.LangForU.lections.Lection;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас-ентитет (Entity), който представлява езиков курс в системата.
 * Съдържа цялата информация за курса, включително език, ниво, цена, дати,
 * лекции, студенти и финален изпит.
 */
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи.
@EqualsAndHashCode // Lombok: Генерира equals() и hashCode() методи.
@Entity // JPA: Посочва, че този клас е ентитет и се мапва към таблица в базата данни.
public class Course {

    /**
     * Уникален идентификатор (ID) на курса.
     * Генерира се автоматично от базата данни чрез sequence генератор.
     */
    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    private Long id;

    /**
     * Езикът, на който се провежда курсът (напр. "Английски").
     */
    @NotNull(message = "Езикът не може да бъде празен.")
    @Size(min = 2, max = 200, message = "Езикът трябва да бъде между 2 и 100 символа.")
    private String language;

    /**
     * Нивото на курса съгласно Общата европейска езикова рамка (CEFR).
     * Съхранява се като текст (String) в базата данни.
     */
    @NotNull(message = "Нивото не може да бъде празно.")
    @Enumerated(EnumType.STRING)
    private Level level;

    /**
     * Цената на курса.
     */
    @NotNull(message = "Цената не може да бъде празна.")
    @PositiveOrZero(message = "Цената трябва да бъде нула или положително число.")
    private Float price;

    /**
     * Начална дата на курса.
     */
    @NotNull(message = "Дата на начало не може да бъде празна.")
    @FutureOrPresent(message = "Дата на начало трябва да бъде в бъдеще или настоящето.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") // Формат за JSON сериализация.
    private LocalDate startDate;

    /**
     * Крайна дата на курса.
     */
    @NotNull(message = "Дата на край не може да бъде празна.")
    @Future(message = "Дата на край трябва да бъде в бъдещето.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    /**
     * Списък със студентите, записани за този курс.
     * Връзка тип "много към много" с AppUser. Връзката се управлява от полето 'courses' в AppUser.
     */
    @ManyToMany(mappedBy = "courses")
    @ToString.Exclude // Изключва полето от toString() метода, за да се избегне рекурсия.
    private List<AppUser> students = new ArrayList<>();

    /**
     * Подробно описание на курса.
     */
    @NotNull(message = "Описание не може да бъде празно.")
    @Column(nullable = false, length = 10000)
    private String description;

    /**
     * Име на основния преподавател.
     */
    @NotNull
    @Size(max = 100, message = "Името не може да бъде по-дълго от 100 символа.")
    private String mainInstructorName;

    /**
     * Име на асистента.
     */
    @NotNull
    @Size(max = 100)
    private String assistantInstructorName;

    /**
     * Име на техническото лице.
     */
    @NotNull
    @Size(max = 100)
    private String technicianName;

    /**
     * URL адрес на изображението за курса.
     */
    @NotNull(message = "URL на снимка не може да бъде празен.")
    private String pictureUrl;

    /**
     * Рейтинг на курса (вероятно от потребители). Стойност по подразбиране е 0.
     */
    @Column(nullable = false)
    private Integer rating = 0;

    /**
     * Списък с лекциите, включени в курса.
     * Връзка тип "едно към много". Всички операции с курса се отразяват и на лекциите.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
    @ToString.Exclude
    private List<Lection> lections = new ArrayList<>();

    /**
     * Финалният изпит, асоцииран с този курс.
     * Връзка тип "едно към едно".
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "final_exam_id", unique = true)
    private FinalExam finalExam;

    /**
     * Персонализиран конструктор за създаване на обект Course.
     */
    public Course(Long id, String language, Level level, Float price, LocalDate startDate, LocalDate endDate,
                  String description, String mainInstructorName, String assistantInstructorName,
                  String technicianName, String pictureUrl, Integer rating, List<Lection> lections, FinalExam finalExam) {
        this.id = id;
        this.language = language;
        this.level = level;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.mainInstructorName = mainInstructorName;
        this.assistantInstructorName = assistantInstructorName;
        this.technicianName = technicianName;
        this.pictureUrl = pictureUrl;
        this.rating = rating;
        this.lections = lections != null ? lections : new ArrayList<>();
        this.finalExam = finalExam;
    }

    /**
     * Метод за валидация, който се изпълнява преди запис (persist) или обновяване (update) на ентитета.
     * Проверява дали крайната дата е след началната.
     */
    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Крайната дата не може да бъде преди началната.");
        }
    }

    /**
     * Изчислява продължителността на курса в седмици.
     * @return Продължителност в седмици.
     */
    public long getDurationInWeeks() {
        return ChronoUnit.WEEKS.between(startDate, endDate);
    }

    /**
     * Форматира цената като текст с валутен символ.
     * @return Форматирана цена (напр. "100.00 лв.").
     */
    public String getFormattedPrice() {
        return String.format("%.2f лв.", price);
    }

    /**
     * Форматира началната дата.
     * @return Дата във формат "dd/MM/yyyy".
     */
    public String getFormattedStartDate() {
        return startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Форматира крайната дата.
     * @return Дата във формат "dd/MM/yyyy".
     */
    public String getFormattedEndDate() {
        return endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Форматира името на нивото за по-добро изобразяване.
     * @return Форматирано ниво (напр. "B_1" става "B-1").
     */
    public String getFormattedLevel() {
        return level != null ? level.name().replace("_", "-") : "N/A";
    }

    /**
     * Помощен метод, който връща продължителността в седмици.
     * @return Продължителност в седмици.
     */
    public long getDuration() {
        return getDurationInWeeks();
    }

    /**
     * Пренаписан toString() метод за текстово представяне на обекта.
     * @return String представяне на курса.
     */
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", level=" + level +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", students=" + (students != null ? students.size() : 0) + // Променено, за да не се печатат обектите
                ", description='" + description + '\'' +
                ", mainInstructorName='" + mainInstructorName + '\'' +
                ", assistantInstructorName='" + assistantInstructorName + '\'' +
                ", technicianName='" + technicianName + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", rating=" + rating +
                ", lections=" + (lections != null ? lections.size() : 0) + // Променено, за да не се печатат обектите
                ", finalExam=" + (finalExam != null ? finalExam.getId() : "null") + // Променено, за да не се печата целият обект
                '}';
    }

}