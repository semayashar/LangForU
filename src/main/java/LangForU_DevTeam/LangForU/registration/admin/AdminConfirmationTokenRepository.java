package LangForU_DevTeam.LangForU.registration.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Репозитори интерфейс за управление на {@link AdminConfirmationToken} ентитети.
 * Предоставя стандартни CRUD операции и дефинира персонализирани методи
 * за работа с токени за потвърждение на администраторска роля.
 */
@Repository
@Transactional(readOnly = true) // По подразбиране всички методи са само за четене, освен ако не е указано друго.
public interface AdminConfirmationTokenRepository extends JpaRepository<AdminConfirmationToken, Long> {

    /**
     * Намира токен за потвърждение по неговия уникален низ.
     *
     * @param token Низът на токена, който се търси.
     * @return {@link Optional}, съдържащ токена, ако е намерен, или празен, ако не е.
     */
    Optional<AdminConfirmationToken> findByToken(String token);

    /**
     * Обновява времето на потвърждение (`confirmedAt`) за даден токен.
     * По този начин токенът се маркира като "използван".
     *
     * @param token       Низът на токена, който да бъде обновен.
     * @param confirmedAt Времевият маркер, който да бъде записан като време на потвърждение.
     * @return Броят на обновените записи (обикновено 1 или 0).
     */
    @Transactional // Презаписва readOnly=true от нивото на класа, тъй като тази операция променя данни.
    @Modifying   // Указва на Spring Data, че това е заявка за промяна (UPDATE/DELETE), а не SELECT.
    @Query("UPDATE AdminConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}