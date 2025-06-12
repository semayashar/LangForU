package LangForU_DevTeam.LangForU.registration;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO), който представлява заявка за регистрация на нов потребител.
 * Този клас се използва за пренасяне на данните, попълнени от потребителя във формата за регистрация,
 * до контролера за по-нататъшна обработка.
 * Съдържа валидационни анотации, които гарантират коректността на подадените данни.
 */
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета.
@NoArgsConstructor  // Lombok: Генерира конструктор без аргументи.
@Getter             // Lombok: Генерира get-методи за всички полета.
@Setter             // Lombok: Генерира set-методи за всички полета.
@ToString           // Lombok: Генерира toString() метод.
public class RegistrationRequest {

    /**
     * Имейл адрес на потребителя.
     */
    @NotEmpty(message = "Имейлът е задължителен.")
    @Email(message = "Моля въведете валиден имейл.")
    private String email;

    /**
     * Парола на потребителя.
     */
    @NotEmpty(message = "Паролата е задължителна.")
    @Size(min = 8, message = "Паролата трябва да бъде поне 8 символа.")
    private String password;

    /**
     * Име на потребителя.
     */
    @NotNull(message = "Името е задължително.")
    @Size(min = 1, message = "Името не може да бъде празно.")
    private String name;

    /**
     * Дата на раждане на потребителя.
     */
    @NotNull(message = "Датата на раждане е задължителна.")
    @Past(message = "Датата на раждане трябва да бъде в миналото.")
    private LocalDate dateOfBirth;

    /**
     * Пол на потребителя.
     */
    private String gender;

    /**
     * Адрес на потребителя.
     */
    @NotNull(message = "Адресът е задължителен.")
    @Size(min = 1, message = "Адресът не може да бъде празен.")
    private String address;
}