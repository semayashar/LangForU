package LangForU_DevTeam.LangForU.registration.admin;

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
 * Клас-ентитет (Entity), който представлява токен за потвърждение на администраторска роля.
 * Когато се изпрати заявка даден потребител да стане администратор, се генерира такъв токен,
 * който се изпраща по имейл за потвърждение.
 */
@Getter // Lombok: Автоматично генерира get-методи.
@Setter // Lombok: Автоматично генерира set-методи.
@NoArgsConstructor // Lombok: Генерира конструктор без аргументи, изискван от JPA.
@Entity // JPA: Посочва, че този клас е ентитет и се мапва към таблица в базата данни.
public class AdminConfirmationToken {

    /**
     * Уникален идентификатор (ID) на токена.
     * Генерира се автоматично от базата данни чрез sequence генератор.
     */
    @SequenceGenerator(
            name = "admin_confirmation_token_sequence",
            sequenceName = "admin_confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_confirmation_token_sequence"
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
    @NotNull(message = "Дата и час на създаване не може да бъде празна.")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Времеви маркер, показващ кога токенът изтича и става невалиден.
     */
    @NotNull(message = "Дата и час на изтичане не може да бъде празна.")
    @Future(message = "Датата на изтичане трябва да бъде в бъдещето.")
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Времеви маркер, показващ кога е потвърден токенът.
     * Стойността е null, докато токенът не бъде използван.
     */
    private LocalDateTime confirmedAt;

    /**
     * Потребителят, за когото се отнася този токен за потвърждение.
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    @NotNull(message = "Потребителят не може да бъде празен.")
    private AppUser appUser;

    /**
     * Персонализиран конструктор за създаване на нов токен.
     *
     * @param token     Уникалният низ на токена.
     * @param createdAt Време на създаване.
     * @param expiresAt Време на изтичане.
     * @param appUser   Потребителят, свързан с токена.
     */
    public AdminConfirmationToken(String token,
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