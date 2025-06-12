package LangForU_DevTeam.LangForU.blog;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.coment.Blog_Comment;
import LangForU_DevTeam.LangForU.like.Blog_Like;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Клас-ентитет (Entity), който представлява блог публикация в системата.
 * Съдържа информация за заглавието, текста, автора, коментари, харесвания и др.
 */
@Getter // Lombok: Автоматично генерира get-методи за всички полета.
@Setter // Lombok: Автоматично генерира set-методи за всички полета.
@AllArgsConstructor // Lombok: Автоматично генерира конструктор с всички полета.
@NoArgsConstructor // Lombok: Автоматично генерира конструктор без аргументи.
@EqualsAndHashCode // Lombok: Автоматично генерира equals() и hashCode() методи.
@Entity // JPA: Посочва, че този клас е ентитет и се мапва към таблица в базата данни.
public class Blog {

    /**
     * Уникален идентификатор (ID) на блог публикацията.
     * Генерира се автоматично от базата данни (IDENTITY стратегия).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заглавие на блог публикацията. Не може да бъде null.
     */
    @Column(name = "title", nullable = false)
    private String name;

    /**
     * Кратко обяснение или резюме на публикацията.
     * Максимална дължина 1000 символа. Не може да бъде null.
     */
    @Column(nullable = false, length = 1000)
    private String shortExplanation;

    /**
     * Основен текст на блог публикацията.
     * Максимална дължина 10000 символа. Не може да бъде null.
     */
    @Column(nullable = false, length = 10000)
    private String blogText;

    /**
     * Дата на публикуване. Не може да бъде null.
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Път до изображението, свързано с публикацията. Може да бъде null.
     */
    @Column(nullable = true)
    private String image;

    /**
     * Списък с категории, към които принадлежи публикацията.
     * Съхранява се в отделна таблица, свързана с тази.
     */
    @ElementCollection
    private List<String> categories;

    /**
     * Списък с тагове, описващи публикацията.
     * Съхранява се в отделна таблица, свързана с тази.
     */
    @ElementCollection
    private List<String> tags;

    /**
     * Списък с коментари към тази публикация.
     * Връзка тип "едно към много".
     * CascadeType.ALL - всички операции (запис, изтриване) върху блога се отразяват и на коментарите.
     * orphanRemoval = true - ако коментар бъде премахнат от този списък, той ще бъде изтрит от базата данни.
     */
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Blog_Comment> comments = new ArrayList<>();

    /**
     * Множество от харесвания към тази публикация.
     * Използва Set, за да се гарантира, че един потребител не може да хареса публикацията повече от веднъж.
     * FetchType.EAGER - харесванията се зареждат веднага заедно с блога.
     */
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Blog_Like> likes = new HashSet<>();

    /**
     * Авторът на публикацията.
     * Връзка тип "много към едно" с AppUser.
     * FetchType.LAZY - авторът ще бъде зареден от базата данни само когато е изрично извикан.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private AppUser author;

    /**
     * Помощен метод, който връща броя на коментарите.
     * @return int - общият брой коментари.
     */
    public int getCommentsCount() {
        return comments != null ? comments.size() : 0;
    }

    /**
     * Помощен метод, който връща броя на харесванията.
     * @return int - общият брой харесвания.
     */
    public int getLikesCount() {
        return likes.size();
    }

}