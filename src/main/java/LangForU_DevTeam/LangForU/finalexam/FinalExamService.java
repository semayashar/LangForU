package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseRepository;
import LangForU_DevTeam.LangForU.question.Question;
import LangForU_DevTeam.LangForU.question.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервизен клас, който капсулира бизнес логиката за управление на финални изпити и резултати от тях.
 */
@Service
@RequiredArgsConstructor // Lombok: Генерира конструктор със всички финални полета, улеснявайки dependency injection.
public class FinalExamService {

    //<editor-fold desc="Dependencies">
    private final FinalExamRepository finalExamRepository;
    private final ExamResultRepository examResultRepository;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    //</editor-fold>

    /**
     * Създава и запазва нов финален изпит в базата данни.
     * @param finalExam Обектът {@link FinalExam}, който да бъде запазен.
     * @return Запазеният обект {@link FinalExam}.
     */
    public FinalExam createFinalExam(FinalExam finalExam) {
        return finalExamRepository.save(finalExam);
    }

    /**
     * Изтрива финален изпит по неговия ID.
     * Преди изтриването, методът премахва двупосочните връзки към курса и въпросите,
     * за да се избегнат проблеми с целостта на данните (integrity constraints).
     * @param id ID на изпита за изтриване.
     */
    @Transactional
    public void deleteFinalExamById(Long id) {
        FinalExam finalExam = finalExamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Финален изпит с ID: " + id + " не е намерен."));

        // Премахване на връзката от свързания курс.
        Course course = finalExam.getCourse();
        if (course != null) {
            course.setFinalExam(null);
            finalExam.setCourse(null);
            courseRepository.save(course);
        }

        // Премахване на връзките от свързаните въпроси.
        if (finalExam.getExamQuestions() != null) {
            for (Question question : finalExam.getExamQuestions()) {
                question.setFinalExam(null);
            }
            finalExam.getExamQuestions().clear();
        }

        // Изтриване на самия изпит.
        finalExamRepository.delete(finalExam);
    }

    /**
     * Извлича списък с всички финални изпити.
     * @return Списък ({@link List}) от всички {@link FinalExam}.
     */
    public List<FinalExam> getAllFinalExams() {
        return finalExamRepository.findAll();
    }

    /**
     * Намира финален изпит по неговия ID.
     * @param id ID на търсения изпит.
     * @return Намереният обект {@link FinalExam}.
     * @throws RuntimeException ако изпит с такова ID не съществува.
     */
    public FinalExam getFinalExamById(Long id) {
        return finalExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Финален изпит с ID: " + id + " не е намерен."));
    }

    /**
     * Извлича резултата от изпит за конкретен потребител и конкретен изпит.
     * @param examId ID на изпита.
     * @param userId ID на потребителя.
     * @return {@link Optional}, съдържащ {@link ExamResult}, ако е намерен.
     */
    public Optional<ExamResult> getExamResultByExamIdAndUserId(Long examId, Long userId) {
        return examResultRepository.findByFinalExamIdAndUserId(examId, userId);
    }

    /**
     * Записва нов резултат от изпит.
     * Ако вече съществува резултат за същия потребител и същия изпит, старият се изтрива,
     * което позволява повторно явяване.
     * @param examId ID на изпита.
     * @param user Потребителят, който се явява.
     * @param multipleChoiceScore Точки от затворени въпроси.
     * @param openEndedScore Точки от отворени въпроси.
     * @param essayScore Точки от есе.
     * @param finalScore Общ резултат (не се използва директно, тъй като се изчислява в конструктора на ExamResult).
     * @param passed Дали изпитът е взет успешно.
     * @param essayFeedback Обратна връзка за есето.
     */
    @Transactional
    public void submitNewExamResult(Long examId, AppUser user, int multipleChoiceScore, int openEndedScore, int essayScore, int finalScore, boolean passed, String essayFeedback) {
        // Проверка за съществуващ резултат и изтриването му.
        Optional<ExamResult> existingResultOpt = examResultRepository.findByFinalExamIdAndUserId(examId, user.getId());
        existingResultOpt.ifPresent(examResultRepository::delete);

        FinalExam finalExam = finalExamRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Финален изпит не е намерен."));

        // Създаване на нов обект за резултат.
        ExamResult newResult = new ExamResult(
                finalExam,
                user,
                multipleChoiceScore,
                openEndedScore,
                essayScore,
                essayFeedback,
                passed
        );

        examResultRepository.save(newResult);
    }

    /**
     * Увиващ (wrapper) метод за запазване на финален изпит.
     * Преизползва метода createFinalExam.
     * @param finalExam Обектът {@link FinalExam} за запис.
     */
    public void save(FinalExam finalExam) {
        createFinalExam(finalExam);
    }
}