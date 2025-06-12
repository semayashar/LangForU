package LangForU_DevTeam.LangForU.subscriber;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Клас-ентитет (Entity), който представлява абонат за известия (напр. за нови блог публикации).
 * Съхранява имейл адреса на абониралия се потребител.
 */
@Getter             // Lombok: Автоматично генерира get-методи.
@Setter             // Lombok: Автоматично генерира set-методи.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@EqualsAndHashCode  // Lombok: Генерира equals() и hashCode() методи.
@NoArgsConstructor  // Lombok: Генерира конструктор без аргументи, изискван от JPA.
@Entity             // JPA: Посочва, че този клас е ентитет и се мапва към таблица в базата данни.
public class Subscriber {

    /**
     * Уникален идентификатор (ID) на абоната.
     * Генерира се автоматично от базата данни.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имейл адресът на абоната.
     * Трябва да бъде във валиден имейл формат и не може да бъде null.
     */
    @Email
    @NotNull
    private String email;
}