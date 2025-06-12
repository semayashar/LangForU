package LangForU_DevTeam.LangForU.coment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервизен клас, който капсулира бизнес логиката, свързана с коментарите към блог публикации.
 * В момента предоставя основна функционалност за запис на коментари.
 */
@Service
public class Blog_Comment_Service {

    /**
     * Репозитори за достъп до данните на коментарите.
     * Инжектира се автоматично от Spring чрез field injection (@Autowired).
     */
    @Autowired
    private Blog_Comment_Repository commentRepository;

    /**
     * Запазва (създава или обновява) коментар в базата данни.
     * Делегира извикването към save метода на JpaRepository.
     *
     * @param comment Обектът {@link Blog_Comment}, който трябва да бъде запазен.
     */
    public void save(Blog_Comment comment) {
        commentRepository.save(comment);
    }
}