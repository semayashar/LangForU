package LangForU_DevTeam.LangForU.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозитори интерфейс за управление на {@link Blog} ентитети.
 * Предоставя CRUD операции и дефинира персонализирани заявки за достъп до данни, свързани с блога.
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * Търси блог публикации, чието име (заглавие) съдържа подадения низ, без значение от регистъра (case-insensitive).
     * Резултатът е пагиниран.
     * @param name Низ за търсене в заглавието на публикацията.
     * @param pageable Обект, съдържащ информация за пагинация (номер на страница, размер).
     * @return {@link Page} с намерените блог публикации.
     */
    Page<Blog> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Намира последните 5 публикувани блог публикации, подредени по дата в низходящ ред.
     * @return Списък с най-много 5 блог публикации.
     */
    List<Blog> findTop5ByOrderByDateDesc();

    /**
     * Намира всички блог публикации, които принадлежат към дадена категория.
     * Използва JPQL заявка с оператор 'MEMBER OF' за търсене в колекция.
     * @param category Името на категорията за филтриране.
     * @param pageable Обект за пагинация.
     * @return {@link Page} с намерените публикации.
     */
    @Query("SELECT b FROM Blog b WHERE :category MEMBER OF b.categories")
    Page<Blog> findByCategory(@Param("category") String category, Pageable pageable);

    /**
     * Намира всички блог публикации, които са маркирани с даден таг.
     * Използва JPQL заявка с оператор 'MEMBER OF' за търсене в колекция.
     * @param tagName Името на тага за филтриране.
     * @param pageable Обект за пагинация.
     * @return {@link Page} с намерените публикации.
     */
    @Query("SELECT b FROM Blog b WHERE :tagName MEMBER OF b.tags")
    Page<Blog> findByTag(@Param("tagName") String tagName, Pageable pageable);

    /**
     * Намира първата блог публикация с ID, по-малко от подаденото, подредена по ID в низходящ ред.
     * Използва се за намиране на "предишна" публикация.
     * @param id ID на текущата публикация.
     * @return {@link Optional}, съдържащ предишната публикация, ако съществува.
     */
    Optional<Blog> findFirstByIdLessThanOrderByIdDesc(Long id);

    /**
     * Намира първата блог публикация с ID, по-голямо от подаденото, подредена по ID във възходящ ред.
     * Използва се за намиране на "следваща" публикация.
     * @param id ID на текущата публикация.
     * @return {@link Optional}, съдържащ следващата публикация, ако съществува.
     */
    Optional<Blog> findFirstByIdGreaterThanOrderByIdAsc(Long id);

    /**
     * Извлича топ 5 (въпреки LIMIT 6) най-популярни категории по брой публикации.
     * Използва се Native SQL заявка.
     * @return Списък от масиви Object[], където всеки масив съдържа [име на категория, брой публикации].
     */
    @Query(value = "SELECT bc.categories, COUNT(b.id) FROM blog b JOIN blog_categories bc ON b.id = bc.blog_id GROUP BY bc.categories ORDER BY COUNT(b.id) DESC LIMIT 6", nativeQuery = true)
    List<Object[]> findTop5Categories();

    /**
     * Извлича топ 5 (въпреки LIMIT 10) най-популярни тагове по брой публикации.
     * Използва се Native SQL заявка.
     * @return Списък от масиви Object[], където всеки масив съдържа [име на таг, брой публикации].
     */
    @Query(value = "SELECT bt.tags, COUNT(b.id) FROM blog b JOIN blog_tags bt ON b.id = bt.blog_id GROUP BY bt.tags ORDER BY COUNT(b.id) DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTop5Tags();

}