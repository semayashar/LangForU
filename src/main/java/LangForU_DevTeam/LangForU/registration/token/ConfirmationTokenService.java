package LangForU_DevTeam.LangForU.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Сервизен клас, който капсулира бизнес логиката за управление на токени
 * за потвърждение на потребителска регистрация.
 */
@Service
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета за dependency injection.
public class ConfirmationTokenService {

    /**
     * Репозитори за достъп до данните на токените за потвърждение.
     */
    private final ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Запазва (създава или обновява) токен за потвърждение в базата данни.
     *
     * @param token Обектът {@link ConfirmationToken}, който трябва да бъде запазен.
     */
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    /**
     * Извлича токен за потвърждение по неговия уникален низ.
     *
     * @param token Низът на токена, който се търси.
     * @return {@link Optional}, съдържащ токена, ако е намерен.
     */
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    /**
     * Задава текущия час и дата като време на потвърждение за даден токен.
     * Ефективно маркира токена като "използван" и потвърждава регистрацията на потребителя.
     * Операцията се изпълнява в рамките на трансакция.
     *
     * @param token Низът на токена, който трябва да бъде потвърден.
     * @return Броят на обновените записи (обикновено 1 или 0).
     */
    @Transactional
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    /**
     * Изтрива всички токени за потвърждение, свързани с даден потребител.
     *
     * @param userId ID на потребителя, чиито токени ще бъдат изтрити.
     */
    public void deleteTokensByUserId(Long userId) {
        confirmationTokenRepository.deleteByUserId(userId);
    }
}