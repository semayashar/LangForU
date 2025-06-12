package LangForU_DevTeam.LangForU.appuser;

import LangForU_DevTeam.LangForU.courses.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Клас-ентитет (Entity), който представлява потребител в системата.
 * Имплементира интерфейса UserDetails на Spring Security за целите на автентикация и оторизация.
 * Използва Lombok за автоматично генериране на boilerplate код като getters, setters и конструктори.
 */
@Getter // Lombok: Автоматично генерира get-методи за всички полета.
@Setter // Lombok: Автоматично генерира set-методи за всички полета.
@AllArgsConstructor // Lombok: Автоматично генерира конструктор с всички полета като аргументи.
@EqualsAndHashCode // Lombok: Автоматично генерира equals() и hashCode() методи.
@NoArgsConstructor // Lombok: Автоматично генерира конструктор без аргументи.
@Entity // JPA: Посочва, че този клас е ентитет и се мапва към таблица в базата данни.
public class AppUser implements UserDetails {

    /**
     * Уникален идентификатор (ID) на потребителя.
     * Генерира се автоматично от базата данни чрез sequence генератор.
     */
    @Id // JPA: Посочва, че това поле е първичен ключ (primary key).
    @SequenceGenerator( // JPA: Конфигурира генератор на поредни номера в базата данни.
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue( // JPA: Посочва, че стойността на полето се генерира автоматично.
            strategy = GenerationType.SEQUENCE, // Стратегията използва дефинирания по-горе sequence.
            generator = "user_sequence"
    )
    private Long id;

    /**
     * Път до профилната снимка на потребителя.
     * Има стойност по подразбиране, ако не е зададена друга.
     */
    @Column(name = "profile_picture") // JPA: Мапва полето към колона с име 'profile_picture'.
    private String profilePicture = "/img/avatars/user.png";

    /**
     * Имейл адрес на потребителя.
     * Използва се и като потребителско име за вход в системата.
     * Трябва да бъде уникален за всеки потребител.
     */
    @NotNull(message = "Имейлът не може да бъде празен.") // Validation: Полето не може да е null.
    @Email(message = "Невалиден формат на имейл.") // Validation: Проверява дали стойността е валиден имейл адрес.
    @Column(unique = true) // JPA: Стойността в тази колона трябва да е уникална.
    private String email;

    /**
     * Парола на потребителя.
     * Трябва да е с дължина поне 8 символа.
     */
    @NotNull(message = "Паролата не може да бъде празна.") // Validation: Полето не може да е null.
    @Size(min = 8, message = "Паролата трябва да бъде поне 8 символа.") // Validation: Ограничава минималната дължина.
    private String password;

    /**
     * Име на потребителя.
     */
    @NotNull(message = "Името не може да бъде празно.") // Validation: Полето не може да е null.
    @Size(min = 1, message = "Името не може да бъде празно.") // Validation: Изисква поне един символ.
    private String name;

    /**
     * Дата на раждане на потребителя.
     * Трябва да е дата в миналото.
     */
    @NotNull(message = "Датата на раждане не може да бъде празна.") // Validation: Полето не може да е null.
    @Past(message = "Датата на раждане трябва да бъде в миналото.") // Validation: Проверява дали датата е в миналото.
    private LocalDate dateOfBirth;

    /**
     * Пол на потребителя.
     */
    @NotNull(message = "Полето за пол не може да бъде празно.") // Validation: Полето не може да е null.
    @Size(min = 1, message = "Полето за пол не може да бъде празно.") // Validation: Изисква поне един символ.
    private String gender;

    /**
     * Адрес на потребителя.
     */
    @NotNull(message = "Адресът не може да бъде празен.") // Validation: Полето не може да е null.
    @Size(min = 1, message = "Адресът не може да бъде празен.") // Validation: Изисква поне един символ.
    private String address;

    /**
     * Роля на потребителя в системата (напр. USER, ADMIN).
     * Съхранява се като текст (String) в базата данни.
     */
    @Enumerated(EnumType.STRING) // JPA: Посочва, че енумерацията трябва да се съхрани като String.
    private AppUserRole appUserRole;

    /**
     * Флаг, който показва дали акаунтът на потребителя е активиран.
     * По подразбиране е 'false' (неактивиран).
     */
    private Boolean enabled = false;

    /**
     * Списък с курсовете, за които потребителят е записан.
     * Това е връзка тип "много към много" (Many-to-Many) с ентитета Course.
     * FetchType.EAGER зарежда курсовете заедно с потребителя.
     * CascadeType.ALL означава, че операции (като запис) върху потребителя ще се отразят и на свързаните курсове.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable( // JPA: Дефинира междинната таблица за връзката "много към много".
            name = "user_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    /**
     * Персонализиран конструктор за създаване на потребител.
     * Позволява изрично задаване на статуса 'enabled'.
     */
    public AppUser(String email, String password, String name, LocalDate dateOfBirth, String gender, String address, AppUserRole appUserRole, Boolean enabled) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.appUserRole = appUserRole;
        this.enabled = enabled;
    }

    /**
     * Персонализиран конструктор за създаване на потребител.
     * По подразбиране задава статуса 'enabled' на 'true'.
     */
    public AppUser(String email, String password, String name, LocalDate dateOfBirth, String gender, String address, AppUserRole appUserRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.appUserRole = appUserRole;
        this.enabled = true;
    }

    /**
     * Метод от интерфейса UserDetails.
     * Връща правата (ролите) на потребителя, които Spring Security използва за оторизация.
     * Ролята се префиксва с "ROLE_" според конвенцията на Spring Security.
     * @return Колекция от правата на потребителя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + appUserRole.name());
        return Collections.singletonList(authority);
    }

    /**
     * Метод от интерфейса UserDetails.
     * Връща потребителското име, което се използва за автентикация.
     * В този случай, това е имейлът на потребителя.
     * @return Имейлът на потребителя.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Метод от интерфейса UserDetails.
     * Показва дали акаунтът на потребителя не е изтекъл.
     * Връща 'true', което означава, че акаунтите не изтичат.
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Метод от интерфейса UserDetails.
     * Показва дали данните за достъп (паролата) на потребителя не са изтекли.
     * Връща 'true', което означава, че паролите не изтичат.
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Метод от интерфейса UserDetails.
     * Показва дали потребителят е активиран или деактивиран.
     * @return Стойността на полето 'enabled'.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}