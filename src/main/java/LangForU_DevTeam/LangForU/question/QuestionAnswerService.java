package LangForU_DevTeam.LangForU.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервизен клас, който капсулира бизнес логиката, свързана с отговорите на потребителите на въпроси.
 * В момента предоставя основна функционалност за запис на отговори.
 */
@Service
public class QuestionAnswerService {

    /**
     * Репозитори за достъп до данните на записаните отговори.
     * Инжектира се автоматично от Spring чрез field injection (@Autowired).
     */
    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    /**
     * Запазва (създава или обновява) отговор на въпрос в базата данни.
     * Делегира извикването към save метода на JpaRepository.
     *
     * @param questionAnswer Обектът {@link QuestionAnswer}, който трябва да бъде запазен.
     */
    public void save(QuestionAnswer questionAnswer) {
        questionAnswerRepository.save(questionAnswer);
    }
}