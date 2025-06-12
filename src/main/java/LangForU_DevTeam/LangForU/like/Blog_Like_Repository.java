package LangForU_DevTeam.LangForU.like;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозитори интерфейс за управление на {@link Blog_Like} ентитети.
 * Предоставя стандартни CRUD операции и дефинира персонализирани методи
 * за търсене и броене на харесвания.
 */
@Repository
public interface Blog_Like_Repository extends JpaRepository<Blog_Like, Long> {

    /**
     * Намира запис за "харесване" по дадена блог публикация и потребител.
     * Тъй като се очаква да има най-много един такъв запис (поради уникалното ограничение в
     * ентитета), методът връща {@link Optional}.
     *
     * @param blog Обектът {@link Blog}, за който се търси харесване.
     * @param user Обектът {@link AppUser}, който е харесал.
     * @return {@link Optional}, съдържащ обекта {@link Blog_Like}, ако е намерен, или празен, ако не е.
     */
    Optional<Blog_Like> findByBlogAndUser(Blog blog, AppUser user);

    /**
     * Преброява колко харесвания има дадена блог публикация по нейното ID.
     * Тази JPQL заявка е по-ефективна от извличането на всички обекти и броенето им в Java,
     * тъй като операцията се извършва директно в базата данни.
     *
     * @param blogId ID на блог публикацията, чиито харесвания се броят.
     * @return Броят на харесванията като long.
     */
    @Query("SELECT COUNT(bl) FROM Blog_Like bl WHERE bl.blog.id = :blogId")
    long countByBlogId(@Param("blogId") Long blogId);

}