package LangForU_DevTeam.LangForU.coment;

import LangForU_DevTeam.LangForU.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозитори интерфейс за управление на {@link Blog_Comment} ентитети.
 * Наследява JpaRepository, което предоставя стандартни CRUD операции
 * и позволява дефинирането на персонализирани заявки.
 */
@Repository
public interface Blog_Comment_Repository extends JpaRepository<Blog_Comment, Long> {

    /**
     * Намира всички коментари, свързани с определена блог публикация.
     * Това е "derived query method" - Spring Data JPA автоматично генерира
     * имплементацията на базата на името на метода.
     *
     * @param blog Обектът {@link Blog}, за който се търсят коментари.
     * @return Списък ({@link List}) от {@link Blog_Comment}, принадлежащи на дадения блог.
     */
    List<Blog_Comment> findByBlog(Blog blog);
}