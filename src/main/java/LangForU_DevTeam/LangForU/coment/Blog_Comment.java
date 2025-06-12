package LangForU_DevTeam.LangForU.coment;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.blog.Blog;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Клас-ентитет (Entity), който представлява коментар към блог публикация.
 * Свързва потребител (автор) с блог публикация и съдържа текста на коментара.
 */
@Getter // Lombok: Автоматично генерира get-методи за всички полета.
@Setter // Lombok: Автоматично генерира set-методи за всички полета.
@NoArgsConstructor // Lombok: Автоматично генерира конструктор без аргументи.
@AllArgsConstructor // Lombok: Автоматично генерира конструктор с всички полета.
@EqualsAndHashCode // Lombok: Автоматично генерира equals() и hashCode() методи.
@Entity // JPA: Посочва, че този клас е ентитет и се мапва към таблица в базата данни.
public class Blog_Comment {

    /**
     * Уникален идентификатор (ID) на коментара.
     * Генерира се автоматично от базата данни.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Блог публикацията, към която е направен този коментар.
     * Връзка тип "много към едно" (много коментари към един блог).
     * FetchType.LAZY означава, че информацията за блога няма да се зарежда, докато не бъде изрично извикана.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false) // Свързва се с колона 'blog_id', която не може да е null.
    private Blog blog;

    /**
     * Потребителят, който е написал коментара.
     * Връзка тип "много към едно" (много коментари от един потребител).
     * FetchType.LAZY означава, че информацията за потребителя няма да се зарежда, докато не бъде изрично извикана.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Свързва се с колона 'user_id', която не може да е null.
    private AppUser user;

    /**
     * Текстовото съдържание на коментара.
     * Не може да бъде null, с максимална дължина 1000 символа.
     */
    @Column(nullable = false, length = 1000)
    private String commentText;

    /**
     * Времеви маркер (timestamp), показващ кога е публикуван коментарът.
     */
    private LocalDateTime commentedAt;

}