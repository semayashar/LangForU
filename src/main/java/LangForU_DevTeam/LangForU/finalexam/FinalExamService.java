package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseRepository;
import LangForU_DevTeam.LangForU.question.Question;
import LangForU_DevTeam.LangForU.question.QuestionAnswer;
import LangForU_DevTeam.LangForU.question.QuestionRepository;
import LangForU_DevTeam.LangForU.question.QuestionAnswerRepository; // Add this import
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinalExamService {

    private final FinalExamRepository finalExamRepository;
    private final ExamResultRepository examResultRepository;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository; // Inject QuestionAnswerRepository

    public FinalExam createFinalExam(FinalExam finalExam) {
        return finalExamRepository.save(finalExam);
    }

    @Transactional
    public void deleteFinalExamById(Long id) {
        System.out.println("DEBUG: Attempting to delete FinalExam with ID: " + id);

        FinalExam finalExam = finalExamRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("DEBUG: FinalExam with ID: " + id + " not found.");
                    return new EntityNotFoundException("Финален изпит с ID: " + id + " не е намерен.");
                });

        System.out.println("DEBUG: Found FinalExam: " + finalExam.getName());

        // --- Handle Course relationship ---
        Course course = finalExam.getCourse();
        if (course != null) {
            System.out.println("DEBUG: Clearing finalExam from associated Course ID: " + course.getId());
            course.setFinalExam(null);
            finalExam.setCourse(null);
            courseRepository.save(course);
            System.out.println("DEBUG: Course updated.");
        }

        // --- Handle ExamResult relationship (with cascade configured in FinalExam) ---
        List<ExamResult> associatedExamResults = examResultRepository.findByFinalExamId(finalExam.getId());
        if (!associatedExamResults.isEmpty()) {
            System.out.println("DEBUG: Deleting " + associatedExamResults.size() + " associated ExamResult(s) for FinalExam ID: " + id);
            examResultRepository.deleteAll(associatedExamResults);
            finalExam.getExamResults().clear();
            System.out.println("DEBUG: Associated ExamResults deleted.");
        } else {
            System.out.println("DEBUG: No associated ExamResults found for FinalExam ID: " + id);
        }

        // --- Handle Question relationship and its associated QuestionAnswers ---
        if (finalExam.getExamQuestions() != null && !finalExam.getExamQuestions().isEmpty()) {
            System.out.println("DEBUG: Processing " + finalExam.getExamQuestions().size() + " exam questions for FinalExam ID: " + id);
            for (Question question : finalExam.getExamQuestions()) {
                // Find and delete all QuestionAnswer entities associated with this question
                List<QuestionAnswer> associatedQuestionAnswers = questionAnswerRepository.findByQuestionId(question.getId()); // Assuming you add this method to your QuestionAnswerRepository
                if (!associatedQuestionAnswers.isEmpty()) {
                    System.out.println("DEBUG: Deleting " + associatedQuestionAnswers.size() + " associated QuestionAnswer(s) for Question ID: " + question.getId());
                    questionAnswerRepository.deleteAll(associatedQuestionAnswers);
                }
            }
            // Now clear the questions collection, which, with orphanRemoval=true, will trigger deletion of Questions
            System.out.println("DEBUG: Clearing examQuestions collection for FinalExam ID: " + id);
            finalExam.getExamQuestions().clear();
            System.out.println("DEBUG: ExamQuestions collection cleared.");
        } else {
            System.out.println("DEBUG: No examQuestions found for FinalExam ID: " + id);
        }

        System.out.println("DEBUG: Attempting finalExamRepository.delete(finalExam)");
        finalExamRepository.delete(finalExam);
        System.out.println("DEBUG: FinalExam deleted successfully (if transaction commits).");
    }

    public List<FinalExam> getAllFinalExams() {
        return finalExamRepository.findAll();
    }

    public FinalExam getFinalExamById(Long id) {
        return finalExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Финален изпит с ID: " + id + " не е намерен."));
    }

    public Optional<ExamResult> getExamResultByExamIdAndUserId(Long examId, Long userId) {
        return examResultRepository.findByFinalExamIdAndUserId(examId, userId);
    }

    @Transactional
    public void submitNewExamResult(Long examId, AppUser user, int multipleChoiceScore, int openEndedScore, int essayScore, int finalScore, boolean passed, String essayFeedback) {
        Optional<ExamResult> existingResultOpt = examResultRepository.findByFinalExamIdAndUserId(examId, user.getId());
        existingResultOpt.ifPresent(examResultRepository::delete);

        FinalExam finalExam = finalExamRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Финален изпит не е намерен."));

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

    public void save(FinalExam finalExam) {
        createFinalExam(finalExam);
    }
}