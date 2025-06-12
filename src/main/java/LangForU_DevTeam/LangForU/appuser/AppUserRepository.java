package LangForU_DevTeam.LangForU.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Репозитори интерфейс за управление на {@link AppUser} ентитети.
 * Използва Spring Data JPA за автоматично генериране на имплементациите на методите.
 * Предоставя CRUD (Create, Read, Update, Delete) операции и дефинира персонализирани заявки.
 */
@Repository // Spring анотация, която маркира интерфейса като компонент за достъп до данни (Data Access Object).
@Transactional(readOnly = true) // По подразбиране всички методи в това репозитори са само за четене, освен ако не е указано друго.
public interface AppUserRepository extends JpaRepository<AppUser, Long> { // Наследява JpaRepository, което осигурява стандартни методи за работа с базата.

    /**
     * Намира потребител по неговия имейл адрес.
     * Spring Data JPA автоматично генерира заявката на базата на името на метода.
     * @param email Имейлът на търсения потребител.
     * @return Optional, съдържащ потребителя, ако е намерен, или празен Optional, ако не е.
     */
    Optional<AppUser> findByEmail(String email);

    /**
     * Активира потребителски акаунт, като задава полето 'enabled' на TRUE.
     * Това е заявка за промяна, която се изпълнява в рамките на трансакция.
     * @param email Имейлът на потребителя, който трябва да бъде активиран.
     * @return Броят на променените записи (обикновено 1 или 0).
     */
    @Modifying // Указва, че заявката променя данни (не е само за четене).
    @Transactional // Указва, че методът трябва да се изпълни в собствена трансакция (запис).
    @Query("UPDATE AppUser a SET a.enabled = TRUE WHERE a.email = ?1") // Персонализирана JPQL заявка за обновяване.
    int enableAppUser(String email);

    /**
     * Деактивира потребителски акаунт, като задава полето 'enabled' на FALSE.
     * @param id ID на потребителя, който трябва да бъде деактивиран.
     * @return Броят на променените записи.
     */
    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.enabled = FALSE WHERE a.id = :id")
    int disableAppUser(@Param("id") Long id); // @Param свързва параметъра на метода с името в заявката.

    /**
     * Променя ролята на потребител.
     * @param id ID на потребителя, чиято роля ще бъде променена.
     * @param role Новата роля, която да бъде зададена (AppUserRole).
     * @return Броят на променените записи.
     */
    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.appUserRole = :role WHERE a.id = :id")
    int updateUserRole(@Param("id") Long id, @Param("role") AppUserRole role);

    /**
     * Намира всички потребители с определена роля и статус на акаунта (активиран/деактивиран).
     * @param role Търсената роля (USER или ADMIN).
     * @param enabled Статус на акаунта (true за активиран, false за деактивиран).
     * @return Списък с потребители, отговарящи на критериите.
     */
    List<AppUser> findByAppUserRoleAndEnabled(AppUserRole role, boolean enabled);

    /**
     * Намира имейл адресите на всички потребители, записани за определен курс.
     * @param courseId ID на курса.
     * @return Списък с имейл адреси (String).
     */
    @Query("SELECT u.email FROM AppUser u JOIN u.courses c WHERE c.id = :courseId")
    List<String> findUserEmailsByCourseId(Long courseId);

    /**
     * Намира всички потребители с определена роля.
     * @param role Търсената роля.
     * @return Списък с потребители с тази роля.
     */
    List<AppUser> findByAppUserRole(AppUserRole role);
}