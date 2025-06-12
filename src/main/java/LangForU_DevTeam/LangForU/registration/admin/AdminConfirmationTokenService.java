package LangForU_DevTeam.LangForU.registration.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Сервизен клас, който капсулира бизнес логиката за управление на токени
 * за потвърждение на администраторска роля.
 */
@Service
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета за dependency injection.
public class AdminConfirmationTokenService {

    /**
     * Репозитори за достъп до данните на токените.
     */
    private final AdminConfirmationTokenRepository adminConfirmationTokenRepository;

    /**
     * Запазва (създава или обновява) токен за потвърждение в базата данни.
     *
     * @param token Обектът {@link AdminConfirmationToken}, който трябва да бъде запазен.
     */
    public void saveAdminConfirmationToken(AdminConfirmationToken token) {
        adminConfirmationTokenRepository.save(token);
    }

    /**
     * Извлича токен за потвърждение по неговия уникален низ.
     *
     * @param token Низът на токена, който се търси.
     * @return {@link Optional}, съдържащ токена, ако е намерен.
     */
    public Optional<AdminConfirmationToken> getToken(String token) {
        return adminConfirmationTokenRepository.findByToken(token);
    }

    /**
     * Задава текущия час и дата като време на потвърждение за даден токен.
     * Ефективно маркира токена като "използван".
     *
     * @param token Низът на токена, който трябва да бъде потвърден.
     * @return Броят на обновените записи (обикновено 1 или 0).
     */
    public int setConfirmedAt(String token) {
        return adminConfirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}