package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозитори интерфейс за управление на {@link ExamResult} ентитети.
 * Предоставя стандартни CRUD операции и дефинира персонализирани методи
 * за търсене на резултати от изпити.
 */
@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

    /**
     * Намира всички резултати, свързани с конкретен финален изпит.
     *
     * @param finalExamId ID на финалния изпит.
     * @return Списък ({@link List}) от резултатите за дадения изпит.
     */
    List<ExamResult> findByFinalExamId(Long finalExamId);

    /**
     * Намира конкретен резултат за даден изпит и даден потребител.
     * Тъй като се очаква един потребител да има само един резултат за един изпит,
     * методът връща {@link Optional}.
     *
     * @param finalExamId ID на финалния изпит.
     * @param userId      ID на потребителя.
     * @return {@link Optional}, съдържащ резултата, ако съществува, или празен, ако не съществува.
     */
    Optional<ExamResult> findByFinalExamIdAndUserId(Long finalExamId, Long userId);

    /**
     * Намира всички резултати от изпити за конкретен потребител.
     *
     * @param user Обектът {@link AppUser}, чиито резултати се търсят.
     * @return Списък ({@link List}) от всички резултати на потребителя.
     */
    List<ExamResult> findByUser(AppUser user);
}