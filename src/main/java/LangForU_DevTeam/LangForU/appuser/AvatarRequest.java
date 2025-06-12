package LangForU_DevTeam.LangForU.appuser;


import lombok.Getter;
import lombok.Setter;


/**
 * Data Transfer Object (DTO), използван за пренасяне на данни при заявка за смяна на аватар (профилна снимка).
 * Този клас капсулира пътя до новата снимка.
 */
@Setter // Lombok: Автоматично генерира set-метод за полето 'profilePicture'.
@Getter // Lombok: Автоматично генерира get-метод за полето 'profilePicture'.
public class AvatarRequest {

    /**
     * Пътят до новата профилна снимка.
     * Очаква се да бъде URL или път до файл, който завършва с валидно разширение за изображение.
     */
    private String profilePicture;

    /**
     * Конструктор по подразбиране.
     */
    public AvatarRequest() {
    }

    /**
     * Валидира дали предоставеният път до профилната снимка е валиден.
     * @return {@code true}, ако пътят не е празен и завършва на .jpg, .jpeg, .png или .gif, в противен случай връща {@code false}.
     */
    public boolean isValid() {
        // Проверява дали полето е null или се състои само от празни символи.
        if (profilePicture == null || profilePicture.trim().isEmpty()) {
            return false;
        }

        // Използва регулярен израз, за да провери дали низът завършва с едно от често срещаните разширения за изображения.
        return profilePicture.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$");
    }
}