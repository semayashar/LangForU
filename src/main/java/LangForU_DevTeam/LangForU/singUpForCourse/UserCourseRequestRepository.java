package LangForU_DevTeam.LangForU.singUpForCourse;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Репозитори интерфейс за управление на {@link UserCourseRequest} ентитети.
 * Предоставя стандартни CRUD операции и дефинира персонализирани методи
 * за търсене и управление на заявки за записване в курсове.
 */
public interface UserCourseRequestRepository extends JpaRepository<UserCourseRequest, Long> {

    /**
     * Намира заявка за курс по ID на потребителя и ID на курса.
     * @param userId ID на потребителя.
     * @param courseId ID на курса.
     * @return {@link Optional}, съдържащ заявката, ако съществува.
     */
    Optional<UserCourseRequest> findByUserIdAndCourseId(Long userId, Long courseId);

    /**
     * Намира заявка за курс по обекти на потребител и курс.
     * @param user Обект {@link AppUser}.
     * @param course Обект {@link Course}.
     * @return Намереният обект {@link UserCourseRequest}, или null ако не е намерен.
     */
    UserCourseRequest findByUserAndCourse(AppUser user, Course course);

    /**
     * Намира заявка по нейното ID.
     * Забележка: Този метод е стандартен за JpaRepository и не е нужно да се декларира повторно.
     * @param id ID на заявката.
     * @return {@link Optional}, съдържащ заявката, ако съществува.
     */
    @Override
    Optional<UserCourseRequest> findById(Long id);

    /**
     * Проверява дали съществува заявка за даден потребител и даден курс по техните ID-та.
     * Това е по-ефективно от извличането на целия обект.
     * @param userId ID на потребителя.
     * @param courseId ID на курса.
     * @return {@code true} ако заявка съществува, в противен случай {@code false}.
     */
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    /**
     * Намира всички заявки, които все още не са потвърдени от администратор.
     * @return Списък ({@link List}) от непотвърдени заявки.
     */
    List<UserCourseRequest> findByConfirmedFalse();

    /**
     * Проверява дали съществува заявка за даден потребител и даден курс по техните обекти.
     * @param user Обект {@link AppUser}.
     * @param course Обект {@link Course}.
     * @return {@code true} ако заявка съществува, в противен случай {@code false}.
     */
    boolean existsByUserAndCourse(AppUser user, Course course);

    /**
     * Изтрива всички заявки за курсове, направени от конкретен потребител.
     * Полезно е при изтриване на потребителски акаунт, за да се почистят свързаните записи.
     * @param userId ID на потребителя, чиито заявки ще бъдат изтрити.
     */
    @Modifying // Указва на Spring Data, че това е заявка за промяна (DELETE).
    @Transactional // Гарантира, че операцията се изпълнява в трансакция.
    @Query("DELETE FROM UserCourseRequest ucr WHERE ucr.user.id = ?1")
    void deleteByUserId(Long userId);

    /**
     * Намира всички заявки, направени от конкретен потребител.
     * @param userId ID на потребителя.
     * @return Списък ({@link List}) от всички заявки на потребителя.
     */
    List<UserCourseRequest> findAllByUserId(Long userId);
}