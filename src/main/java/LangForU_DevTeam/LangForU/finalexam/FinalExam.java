package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.question.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас-ентитет (Entity), който представлява финален изпит за даден курс.
 * Съдържа информация за името, датата, продължителността на изпита,
 * както и въпросите и темата за есе.
 */
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи, изискван от JPA.

@Entity // JPA: Посочва, че този клас е ентитет.
@Table(name = "final_exams") // JPA: Указва името на таблицата в базата данни.
public class FinalExam {

    /**
     * Уникален идентификатор (ID) на финалния изпит.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Име на финалния изпит.
     */
    @NotNull(message = "Името на финалния изпит не може да бъде празно.")
    @Size(min = 1, message = "Името на финалния изпит не може да бъде празно.")
    @Column(nullable = false, length = 500)
    private String name;

    /**
     * Дата, на която ще се проведе изпитът.
     */
    @NotNull(message = "Дата на изпита не може да бъде празна.")
    @FutureOrPresent(message = "Дата на изпита не може да бъде в миналото.")
    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    /**
     * Продължителност на изпита в минути.
     */
    @NotNull(message = "Продължителността на изпита не може да бъде празна.")
    @Min(value = 1, message = "Продължителността на изпита трябва да бъде поне 1 минута.")
    @Column(name = "duration_in_minutes", nullable = false)
    private int duration;

    /**
     * Курсът, към който принадлежи този финален изпит.
     * Връзка тип "едно към едно". Управлява се от полето 'finalExam' в класа Course.
     */
    @OneToOne(mappedBy = "finalExam")
    private Course course; // Тук не може да се сложи @NotNull, защото Course е собственик на връзката.

    /**
     * Списък с въпросите, включени в изпита.
     * Връзка тип "едно към много". Всички операции с изпита се отразяват и на въпросите.
     */
    @NotNull(message = "Изпитни упражнения не могат да бъдат празни.")
    @Size(min = 1, message = "Трябва да има поне едно изпитно упражнение.")
    @OneToMany(mappedBy = "finalExam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> examQuestions;

    /**
     * Списък с резултатите от изпита, свързани с този финален изпит.
     * При изтриване на финалния изпит, свързаните резултати също ще бъдат изтрити.
     */
    @OneToMany(mappedBy = "finalExam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamResult> examResults = new ArrayList<>(); // Инициализиране по подразбиране

    /**
     * Темата за есе, което е част от изпита.
     */
    @NotNull(message = "Тема за есе не може да бъде празна.")
    @Size(min = 1, message = "Тема за есе не може да бъде празна.")
    @Column(name = "theme_for_essay", nullable = false)
    private String essayTopic;

    /**
     * Персонализиран конструктор за създаване на финален изпит.
     * Автоматично задава името и датата на изпита на базата на свързания курс.
     * Установява двупосочната връзка между изпита и въпросите.
     *
     * @param course        Курсът, за който е изпитът.
     * @param examQuestions Списък с въпроси за изпита.
     * @param essayTopic    Тема за есе.
     */
    public FinalExam(Course course, List<Question> examQuestions, String essayTopic) {
        this.name = course != null ? "Финален изпит: " + course.getLanguage() : "Unnamed Final Exam";
        this.examDate = course != null ? course.getEndDate() : LocalDate.now().plusMonths(1);
        this.duration = 120; // Продължителност по подразбиране.
        this.course = course;
        this.examQuestions = examQuestions != null ? examQuestions : new ArrayList<>();
        // Задава на всеки въпрос връзка към този изпит.
        for (Question ex : this.examQuestions) {
            ex.setFinalExam(this);
        }
        this.essayTopic = essayTopic != null ? essayTopic : "Default Essay Theme";
        this.examResults = new ArrayList<>(); // Инициализиране на новия списък
    }

    /**
     * Проверява дали изпитът е "пуснат" (released).
     * Логиката е обърната: връща true, ако датата на изпита НЕ Е днешната дата.
     *
     * @return {@code true} ако датата на изпита не е днес, в противен случай {@code false}.
     */
    public boolean hasBeenReleased() {
        return !examDate.isEqual(LocalDate.now());
    }
}