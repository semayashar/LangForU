package LangForU_DevTeam.LangForU.like;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.blog.Blog;
import LangForU_DevTeam.LangForU.blog.BlogService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервизен клас, който капсулира бизнес логиката за управление на харесванията на блог публикации.
 */
@Service
public class Blog_Like_Service {

    private final Blog_Like_Repository blogLikeRepository;
    private final BlogService blogService;

    /**
     * Конструктор за инжектиране на зависимости.
     * @param blogLikeRepository Репозитори за харесвания.
     * @param blogService Сервиз за управление на блогове.
     * Анотацията {@link Lazy} се използва, за да се разреши циклична зависимост
     * (circular dependency) между Blog_Like_Service и BlogService.
     * Това означава, че инстанцията на BlogService ще бъде създадена едва когато
     * бъде извикана за първи път, а не при стартиране на приложението.
     */
    public Blog_Like_Service(Blog_Like_Repository blogLikeRepository, @Lazy BlogService blogService) {
        this.blogLikeRepository = blogLikeRepository;
        this.blogService = blogService;
    }

    /**
     * Запазва ново харесване (like) в базата данни.
     * След като запише харесването, презаписва и самия блог. Това може да е необходимо
     * задействане на други процеси, като например обновяване на кеш или индекси.
     *
     * @param like Обектът {@link Blog_Like}, който да бъде запазен.
     */
    public void save(Blog_Like like) {
        blogLikeRepository.save(like);

        Blog blog = like.getBlog();
        blogService.save(blog);
    }

    /**
     * Изтрива харесване (like) от базата данни.
     * Подобно на 'save', след изтриването презаписва и самия блог.
     *
     * @param like Обектът {@link Blog_Like}, който да бъде изтрит.
     */
    public void delete(Blog_Like like) {
        blogLikeRepository.delete(like);
        Blog blog = like.getBlog();
        blogService.save(blog);
    }

    /**
     * Намира запис за харесване по даден блог и потребител.
     * Делегира извикването към репозиторито.
     *
     * @param blog Блог публикацията.
     * @param user Потребителят.
     * @return {@link Optional}, съдържащ {@link Blog_Like}, ако е намерен.
     */
    public Optional<Blog_Like> findByBlogAndUser(Blog blog, AppUser user) {
        return blogLikeRepository.findByBlogAndUser(blog, user);
    }

    /**
     * Преброява харесванията за дадена блог публикация.
     * Делегира извикването към репозиторито.
     *
     * @param blogId ID на блог публикацията.
     * @return Броят на харесванията.
     */
    public long countLikesByBlogId(Long blogId) {
        return blogLikeRepository.countByBlogId(blogId);
    }
}