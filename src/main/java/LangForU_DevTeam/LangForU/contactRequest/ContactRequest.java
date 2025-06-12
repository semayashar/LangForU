package LangForU_DevTeam.LangForU.contactRequest;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Клас-ентитет (Entity), който представлява заявка за контакт, изпратена чрез формата за контакт на уебсайта.
 * Съхранява информацията, подадена от потребителя.
 */
@Entity // JPA: Посочва, че този клас е ентитет.
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи, изискван от JPA.
@Table(name = "contactRequests") // JPA: Указва името на таблицата в базата данни.
public class ContactRequest {

    /**
     * Уникален идентификатор (ID) на заявката за контакт.
     * Генерира се автоматично от базата данни.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Име на лицето, изпратило заявката.
     * Полето не може да бъде празно.
     */
    @NotNull
    private String name;

    /**
     * Имейл адрес на лицето, изпратило заявката.
     * Полето не може да бъде празно и трябва да е във валиден имейл формат.
     */
    @NotNull
    @Email
    private String email;

    /**
     * Тема на съобщението.
     * Полето не може да бъде празно.
     */
    @NotNull
    private String subject;

    /**
     * Текстът на съобщението.
     * Полето не може да бъде празно и има максимална дължина от 3000 символа.
     */
    @NotNull
    @Column(length = 3000)
    private String message;

    /**
     * Времеви маркер (timestamp), показващ кога е изпратена заявката.
     */
    private LocalDateTime submittedAt;

    /**
     * Персонализиран конструктор за създаване на нова заявка за контакт.
     * Автоматично задава времето на изпращане на текущия момент.
     *
     * @param name    Името на подателя.
     * @param email   Имейлът на подателя.
     * @param subject Темата на съобщението.
     * @param message Текстът на съобщението.
     */
    public ContactRequest(String name, String email, String subject, String message) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.submittedAt = LocalDateTime.now();
    }
}