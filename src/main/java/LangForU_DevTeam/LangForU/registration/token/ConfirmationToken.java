package LangForU_DevTeam.LangForU.registration.token;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Клас-ентитет (Entity), който представлява токен за потвърждение на регистрация.
 * Този токен се генерира при регистрация на нов потребител и се изпраща на неговия имейл,
 * за да верифицира, че имейл адресът е реален и принадлежи на потребителя.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    /**
     * Уникален идентификатор (ID) на токена.
     * Генерира се автоматично от базата данни чрез sequence генератор.
     */
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

    /**
     * Самият токен - уникален низ от символи (обикновено UUID).
     */
    @NotNull(message = "Токенът не може да бъде празен.")
    @Size(min = 36, max = 36, message = "Токенът трябва да бъде 36 символа.")
    @Column(nullable = false)
    private String token;

    /**
     * Времеви маркер, показващ кога е създаден токенът.
     */
    @NotNull(message = "Дата на създаване не може да бъде празна.")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Времеви маркер, показващ кога токенът изтича и става невалиден.
     */
    @NotNull(message = "Дата на изтичане не може да бъде празна.")
    @Future(message = "Датата на изтичане трябва да бъде в бъдещето.")
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Времеви маркер, показващ кога е потвърден токенът.
     * Стойността е null, докато токенът не бъде използван.
     */
    private LocalDateTime confirmedAt;

    /**
     * Потребителят, за чиято регистрация се отнася този токен.
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    @NotNull(message = "Потребителят не може да бъде празен.")
    private AppUser appUser;

    /**
     * Персонализиран конструктор за създаване на нов токен за потвърждение.
     *
     * @param token     Уникалният низ на токена.
     * @param createdAt Време на създаване.
     * @param expiresAt Време на изтичане.
     * @param appUser   Потребителят, свързан с токена.
     */
    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }

    /**
     * Метод за валидация, който се изпълнява автоматично от JPA преди запис или обновяване.
     * Проверява дали датата на изтичане е след датата на създаване.
     */
    @PrePersist
    @PreUpdate
    void validateDates() {
        if (createdAt != null && expiresAt != null && expiresAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("Дата на изтичане не може да бъде преди датата на създаване.");
        }
    }
}