package LangForU_DevTeam.LangForU.courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозитори интерфейс за управление на {@link Course} ентитети.
 * Предоставя стандартни CRUD операции чрез наследяване на {@link JpaRepository},
 * както и дефинира персонализирани заявки за търсене и филтриране на курсове.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Намира всички курсове, чието име на език съдържа подадения низ, без значение от регистъра (case-insensitive).
     * Това е "derived query method", чиято имплементация се генерира автоматично от Spring Data JPA.
     *
     * @param language Низ за търсене в името на езика на курса.
     * @return Списък ({@link List}) от намерените курсове.
     */
    List<Course> findByLanguageContainingIgnoreCase(String language);

    /**
     * Намира всички курсове, които все още нямат асоцииран финален изпит (finalExam е NULL).
     *
     * @return Списък ({@link List}) от курсове без финален изпит.
     */
    List<Course> findByFinalExamIsNull();

    /**
     * Custom JPQL query to find all courses that do not have an associated final exam (finalExam is NULL).
     * This is an alternative to findByFinalExamIsNull() if you prefer explicit JPQL.
     *
     * @return Списък ({@link List}) от курсове без финален изпит.
     */
    @Query("SELECT c FROM Course c WHERE c.finalExam IS NULL")
    List<Course> findCoursesWithoutFinalExamJPQL();

    /**
     * Намира "активни" курсове, използвайки Native SQL заявка, специфична за PostgreSQL.
     * Заявката намира курсове, за които текущата дата попада в разширен времеви прозорец:
     * от 2 седмици преди началната дата на курса до 2 седмици след крайната му дата.
     *
     * @param now Текущата дата, която се подава като параметър.
     * @return Списък ({@link List}) от активните курсове.
     */
    @Query(value = "SELECT * FROM course c WHERE :now BETWEEN (c.start_date - INTERVAL '2 weeks') AND (c.end_date + INTERVAL '2 weeks')",
            nativeQuery = true)
    List<Course> findActiveCourses(@Param("now") LocalDate now);

    /**
     * Намира курсове, чието описание съдържа подадения низ, без значение от регистъра.
     * Използва JPQL заявка с функциите LOWER и CONCAT за гъвкаво търсене.
     *
     * @param description Низ за търсене в описанието на курса.
     * @return Списък ({@link List}) от намерените курсове.
     */
    @Query("SELECT c FROM Course c WHERE LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Course> findByDescriptionContaining(@Param("description") String description);
}