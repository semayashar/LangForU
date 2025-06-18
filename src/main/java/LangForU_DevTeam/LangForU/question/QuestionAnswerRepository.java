// LangForU_DevTeam.LangForU.question.QuestionAnswerRepository.java
package LangForU_DevTeam.LangForU.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import List

/**
 * Репозитори интерфейс за управление на {@link QuestionAnswer} ентитети.
 * Чрез наследяването на {@link JpaRepository}, този интерфейс автоматично получава
 * пълен набор от стандартни CRUD (Create, Read, Update, Delete) операции,
 * без да е необходимо да се пишат имплементации.
 */
@Repository // Маркира интерфейса като Spring компонент за достъп до данни.
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
    // Няма нужда от дефиниране на методи тук, тъй като се използват
    // вградените от JpaRepository, като save(), findById(), findAll(), deleteById() и др.

    /**
     * Намира всички отговори на въпроси, свързани с конкретен въпрос.
     * @param questionId ID на въпроса.
     * @return Списък ({@link List}) от отговорите за дадения въпрос.
     */
    List<QuestionAnswer> findByQuestionId(Long questionId);
}