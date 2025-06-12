package LangForU_DevTeam.LangForU.lections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервизен клас, който капсулира бизнес логиката за управление на лекции.
 * Служи като посредник между контролерите и {@link LectionRepository}.
 */
@Service
public class LectionService {

    /**
     * Репозитори за достъп до данните на лекциите.
     * Инжектира се автоматично от Spring чрез field injection (@Autowired).
     */
    @Autowired
    private LectionRepository lectionRepository;

    /**
     * Намира лекция по нейния уникален идентификатор (ID).
     *
     * @param id ID на търсената лекция.
     * @return Намереният обект {@link Lection}.
     * @throws RuntimeException ако лекция с такова ID не бъде намерена.
     */
    public Lection findById(Long id) {
        return lectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Лекцията не е намерена"));
    }

    /**
     * Намира всички лекции, насрочени за публикуване на определена дата.
     *
     * @param releaseDate Датата, за която се търсят лекции.
     * @return Списък ({@link List}) от намерените лекции.
     */
    public List<Lection> findByReleaseDate(LocalDate releaseDate) {
        return lectionRepository.findAllByReleaseDate(releaseDate);
    }

    /**
     * Запазва (създава или обновява) лекция в базата данни.
     *
     * @param lection Обектът {@link Lection}, който трябва да бъде запазен.
     */
    public void save(Lection lection) {
        lectionRepository.save(lection);
    }

    /**
     * Извлича всички лекции, които принадлежат към определен курс.
     *
     * @param courseId ID на курса, чиито лекции се търсят.
     * @return Списък ({@link List}) от лекциите за дадения курс.
     */
    public List<Lection> getLectionsByCourseId(Long courseId) {
        return lectionRepository.findByCourseId(courseId);
    }

    /**
     * Изтрива лекция от базата данни по нейния ID.
     *
     * @param id ID на лекцията, която трябва да бъде изтрита.
     */
    public void deleteLectionById(Long id) {
        lectionRepository.deleteById(id);
    }

    /**
     * Извлича списък с абсолютно всички лекции от базата данни.
     *
     * @return Списък ({@link List}) от всички лекции.
     */
    public List<Lection> getAllLections() {
        return lectionRepository.findAll();
    }
}