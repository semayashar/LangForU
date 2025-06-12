package LangForU_DevTeam.LangForU.like;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.blog.Blog;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Клас-ентитет (Entity), който представлява "харесване" (like) на блог публикация от потребител.
 * Този клас свързва потребител ({@link AppUser}) с блог публикация ({@link Blog})
 * и съдържа времеви маркер за действието.
 */
@Entity // JPA: Посочва, че този клас е ентитет.
@Table(name = "blog_like", uniqueConstraints = {
        // Установява уникално ограничение: комбинацията от blog_id и user_id трябва да е уникална.
        // Това гарантира, че един потребител не може да хареса една и съща публикация повече от веднъж.
        @UniqueConstraint(columnNames = {"blog_id", "user_id"})
})
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи, изискван от JPA.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@EqualsAndHashCode // Lombok: Генерира equals() и hashCode() методи.
public class Blog_Like {

    /**
     * Уникален идентификатор (ID) на самото харесване.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Блог публикацията, която е харесана.
     * Връзка тип "много към едно" (много харесвания могат да сочат към един блог).
     * FetchType.LAZY означава, че информацията за блога ще се зареди само при нужда.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    /**
     * Потребителят, който е направил харесването.
     * Връзка тип "много към едно" (много харесвания могат да бъдат направени от един потребител).
     * FetchType.LAZY означава, че информацията за потребителя ще се зареди само при нужда.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    /**
     * Времеви маркер (timestamp), показващ кога е направено харесването.
     */
    private LocalDateTime likedAt;
}