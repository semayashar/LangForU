package LangForU_DevTeam.LangForU.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервизен клас, който капсулира бизнес логиката за управление на курсове.
 * Служи като посредник между контролерите и {@link CourseRepository}.
 */
@Service
public class CourseService {

    /**
     * Репозитори за достъп до данните на курсовете.
     * Инжектира се автоматично от Spring чрез field injection (@Autowired).
     */
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Намира курс по неговия уникален идентификатор (ID).
     * Операцията се изпълнява в трансакция само за четене.
     *
     * @param id ID на търсения курс.
     * @return Намереният обект {@link Course}.
     * @throws NoSuchElementException ако курс с такова ID не бъде намерен.
     */
    @Transactional(readOnly = true)
    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Курсът не е намерен"));
    }

    /**
     * Запазва (създава или обновява) курс в базата данни.
     * Операцията се изпълнява в рамките на трансакция.
     *
     * @param course Обектът {@link Course}, който трябва да бъде запазен.
     */
    @Transactional
    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    /**
     * Изтрива курс от базата данни по неговия ID.
     * Операцията се изпълнява в рамките на трансакция.
     *
     * @param id ID на курса, който трябва да бъде изтрит.
     */
    @Transactional
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Извлича списък с абсолютно всички курсове от базата данни.
     *
     * @return Списък ({@link List}) от всички курсове.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Извлича списък само с "активните" курсове.
     * Активността се определя от заявката в репозиторито {@link CourseRepository#findActiveCourses(LocalDate)}.
     *
     * @return Списък ({@link List}) от активните курсове.
     */
    public List<Course> getAllActiveCourses() {
        return courseRepository.findActiveCourses(LocalDate.now());
    }

    /**
     * Търси и връща курсове по име на език. Търсенето не е чувствително към регистъра.
     *
     * @param language Езикът, по който да се търси.
     * @return Списък ({@link List}) от намерените курсове.
     */
    public List<Course> getCoursesByLanguage(String language) {
        return courseRepository.findByLanguageContainingIgnoreCase(language);
    }

    /**
     * Търси и връща курсове по съдържание в описанието им.
     *
     * @param description Низ за търсене в описанието на курсовете.
     * @return Списък ({@link List}) от намерените курсове.
     */
    public List<Course> getCoursesByDescription(String description) {
        return courseRepository.findByDescriptionContaining(description);
    }

    /**
     * Намира и връща всички курсове, които все още нямат добавен финален изпит.
     *
     * @return Списък ({@link List}) от курсове без финален изпит.
     */
    public List<Course> findCoursesWithoutFinalExam() {
        return courseRepository.findCoursesWithoutFinalExamJPQL();
    }
}