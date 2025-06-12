package LangForU_DevTeam.LangForU.blog;

import LangForU_DevTeam.LangForU.email.EmailService;
import LangForU_DevTeam.LangForU.email.EmailTemplateService;
import LangForU_DevTeam.LangForU.like.Blog_Like_Service;
import LangForU_DevTeam.LangForU.subscriber.Subscriber;
import LangForU_DevTeam.LangForU.subscriber.SubscriberRepository;
import LangForU_DevTeam.LangForU.subscriber.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервизен клас, който капсулира бизнес логиката, свързана с блог публикациите.
 * Управлява операции като създаване, търсене, филтриране и уведомяване на абонати.
 */
@Service
public class BlogService {

    //<editor-fold desc="Dependencies">
    private final BlogRepository blogRepository;
    private final Blog_Like_Service blogLikeService;
    private final SubscriberService subscriberService;
    private final SubscriberRepository subscriberRepository;
    //</editor-fold>

    /**
     * Конструктор за инжектиране на зависимости.
     * @param blogRepository Репозитори за блог публикации.
     * @param blogLikeService Сервиз за управление на харесвания. Използва @Lazy за предотвратяване на циклични зависимости.
     * @param subscriberService Сервиз за управление на абонати.
     * @param emailService Сервиз за изпращане на имейли (не се използва в класа).
     * @param emailTemplateService Сервиз за имейл шаблони (не се използва в класа).
     * @param subscriberRepository Репозитори за абонати.
     */
    @Autowired
    public BlogService(BlogRepository blogRepository, @Lazy Blog_Like_Service blogLikeService, SubscriberService subscriberService, EmailService emailService, EmailTemplateService emailTemplateService, SubscriberRepository subscriberRepository) {
        this.blogRepository = blogRepository;
        this.blogLikeService = blogLikeService;
        this.subscriberService = subscriberService;
        this.subscriberRepository = subscriberRepository;
    }

    /**
     * Връща списък с всички блог публикации.
     * @return {@link List} от {@link Blog}.
     */
    public List<Blog> findAllBlogs() {
        return blogRepository.findAll();
    }

    /**
     * Намира блог публикация по нейното ID.
     * @param id ID на търсената публикация.
     * @return {@link Blog} обект, ако е намерен, в противен случай null.
     */
    public Blog findBlogById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    /**
     * Запазва нова блог публикация и уведомява всички абонати за нея.
     * @param blog Обектът {@link Blog}, който да бъде запазен.
     * @return Запазеният {@link Blog} обект.
     */
    public Blog saveAndNotifySubscribers(Blog blog) {
        Blog savedBlog = blogRepository.save(blog);
        List<Subscriber> subscribers = new ArrayList<>(subscriberRepository.findAll());

        for (Subscriber subscriber : subscribers) {
            try {
                subscriberService.notifySubscriber(subscriber, blog);
            } catch (Exception e) {
                // Логва грешка, ако уведомлението за конкретен абонат се провали, но продължава със следващите.
                System.err.println("Неуспешно уведомление за абонат: " + subscriber.getEmail());
            }
        }
        return savedBlog;
    }

    /**
     * Запазва (създава или обновява) блог публикация.
     * @param blog Обектът {@link Blog} за запис.
     * @return Запазеният {@link Blog} обект.
     */
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    /**
     * Връща списък с най-новите публикации.
     * @param limit Броят на публикациите, които да бъдат върнати (в момента се игнорира, репозиторито връща топ 5).
     * @return {@link List} от {@link Blog}.
     */
    public List<Blog> getRecentPosts(int limit) {
        return blogRepository.findTop5ByOrderByDateDesc();
    }

    /**
     * Връща пагиниран списък с всички блог публикации, подредени по дата на публикуване.
     * @param currentPage Номер на текущата страница.
     * @param pageSize Брой елементи на страница.
     * @return {@link Page} с блог публикации.
     */
    public Page<Blog> findBlogsPaginated(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("date").descending());
        return blogRepository.findAll(pageable);
    }

    /**
     * Връща блог публикация по ID.
     * @param id ID на търсената публикация.
     * @return {@link Optional} с намерения {@link Blog}.
     */
    public Optional<Blog> getBlogWithCommentsById(Long id) {
        return blogRepository.findById(id);
    }

    /**
     * Намира предишната публикация спрямо текущата (по ID).
     * @param id ID на текущата публикация.
     * @return Предишният {@link Blog} обект или null, ако няма такъв.
     */
    public Blog getPreviousBlogById(Long id) {
        return blogRepository.findFirstByIdLessThanOrderByIdDesc(id).orElse(null);
    }

    /**
     * Намира следващата публикация спрямо текущата (по ID).
     * @param id ID на текущата публикация.
     * @return Следващият {@link Blog} обект или null, ако няма такъв.
     */
    public Blog getNextBlogById(Long id) {
        return blogRepository.findFirstByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }

    /**
     * Търси в блог публикациите по ключова дума в заглавието.
     * @param query Ключова дума за търсене.
     * @param currentPage Номер на текущата страница.
     * @param pageSize Брой елементи на страница.
     * @return {@link Page} с намерените публикации.
     */
    public Page<Blog> searchBlogs(String query, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return blogRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    /**
     * Намира пагиниран списък с публикации от определена категория.
     * @param categoryName Име на категорията.
     * @param currentPage Номер на текущата страница.
     * @param pageSize Брой елементи на страница.
     * @return {@link Page} с публикации от дадената категория.
     */
    public Page<Blog> findBlogsByCategoryPaginated(String categoryName, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return blogRepository.findByCategory(categoryName, pageable);
    }

    /**
     * Намира пагиниран списък с публикации, маркирани с определен таг.
     * @param tagName Име на тага.
     * @param currentPage Номер на текущата страница.
     * @param pageSize Брой елементи на страница.
     * @return {@link Page} с публикации с дадения таг.
     */
    public Page<Blog> findBlogsByTagPaginated(String tagName, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return blogRepository.findByTag(tagName, pageable);
    }

    /**
     * Изтрива блог публикация.
     * @param blog Обектът {@link Blog}, който да бъде изтрит.
     */
    public void delete(Blog blog) {
        blogRepository.delete(blog);
    }

    /**
     * Извлича имената на топ 5 най-използвани категории.
     * @return {@link List} от {@link String} с имената на категориите.
     */
    public List<String> getTop5Categories() {
        List<Object[]> result = blogRepository.findTop5Categories();
        List<String> categories = new ArrayList<>();
        for (Object[] row : result) {
            String categoryName = (String) row[0];
            categories.add(categoryName);
        }
        return categories;
    }

    /**
     * Извлича имената на топ 5 най-използвани тагове.
     * @return {@link List} от {@link String} с имената на таговете.
     */
    public List<String> getTop5Tags() {
        List<Object[]> result = blogRepository.findTop5Tags();
        List<String> tags = new ArrayList<>();
        for (Object[] row : result) {
            String tagName = (String) row[0]; // Променливата е преименувана за яснота.
            tags.add(tagName);
        }
        return tags;
    }
}