package LangForU_DevTeam.LangForU.lections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозитори интерфейс за управление на {@link Lection} ентитети.
 * Предоставя стандартни CRUD операции и дефинира персонализирани методи
 * за търсене на лекции.
 */
@Repository
public interface LectionRepository extends JpaRepository<Lection, Long> {

    /**
     * Намира всички лекции, които принадлежат към определен курс, по ID на курса.
     * Това е "derived query method" - Spring Data JPA автоматично генерира
     * имплементацията на базата на името на метода.
     *
     * @param courseId ID на курса, чиито лекции се търсят.
     * @return Списък ({@link List}) от {@link Lection} обекти.
     */
    List<Lection> findByCourseId(Long courseId);

    /**
     * Намира всички лекции, които са насрочени за публикуване на определена дата.
     * Използва JPQL заявка с "JOIN FETCH", което е оптимизация. Това кара JPA
     * да зареди и свързаните обекти Course в същата заявка, като по този начин
     * се избягват допълнителни заявки към базата данни (N+1 проблем).
     *
     * @param releaseDate Датата, за която се търсят лекции.
     * @return Списък ({@link List}) от {@link Lection} обекти, заедно с техните курсове.
     */
    @Query("SELECT l FROM Lection l JOIN FETCH l.course WHERE l.releaseDate = :releaseDate")
    List<Lection> findAllByReleaseDate(@Param("releaseDate") LocalDate releaseDate);
}