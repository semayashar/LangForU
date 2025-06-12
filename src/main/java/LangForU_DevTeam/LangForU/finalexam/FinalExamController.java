package LangForU_DevTeam.LangForU.finalexam;

import LangForU_DevTeam.LangForU.Sevi.ChatGPTService;
import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.certificate.CertificateGeneratorService;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.lections.LectionController;
import LangForU_DevTeam.LangForU.question.Question;
import LangForU_DevTeam.LangForU.question.QuestionRepository;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequest;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * Контролер, който управлява целия жизнен цикъл на финалните изпити.
 * Обработва заявки за създаване, преглед, попълване и оценяване на изпити,
 * както и за генериране на сертификати.
 */
@Controller
@RequestMapping("/final-exams")
@RequiredArgsConstructor
public class FinalExamController {

    private static final int PASSING_SCORE = 30; // Минимален брой точки за успешно преминаване.

    //<editor-fold desc="Dependencies">
    @Autowired
    private final ChatGPTService chatGPTService;
    @Autowired
    private final FinalExamService finalExamService;
    @Autowired
    private final CourseService courseService;
    @Autowired
    private final CertificateGeneratorService certificateGeneratorService;
    @Autowired
    private final UserCourseRequestService userCourseRequestService;
    @Autowired
    private final QuestionRepository questionRepository;
    private final ExamResultRepository examResultRepository;
    //</editor-fold>

    /**
     * Показва детайли за конкретен финален изпит.
     * @param id ID на изпита.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'final-exams/finalExam'.
     */
    @GetMapping("/{id}")
    public String viewFinalExamDetails(@PathVariable Long id, Model model) {
        model.addAttribute("finalExam", finalExamService.getFinalExamById(id));
        return "final-exams/finalExam";
    }

    /**
     * Показва детайли за финален изпит от администраторска гледна точка.
     * @param id ID на изпита.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'final-exams/viewFinalExam'.
     */
    @GetMapping("/admin/view/{id}")
    public String viewAllFinalExamsForAdmin(@PathVariable Long id, Model model) {
        model.addAttribute("finalExam", finalExamService.getFinalExamById(id));
        return "final-exams/viewFinalExam";
    }

    /**
     * Показва списък с всички финални изпити за администратора.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'final-exams/finalExamsList'.
     */
    @GetMapping("/admin/view-all")
    public String viewAllFinalExamsForAdmin(Model model) {
        model.addAttribute("finalExams", finalExamService.getAllFinalExams());
        return "final-exams/finalExamsList";
    }


    /**
     * Показва детайли за финален изпит от потребителска гледна точка.
     * @param id ID на изпита.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'final-exams/viewFinalExam'.
     */
    @GetMapping("/view/{id}")
    public String viewFinalExamDetailsByUser(@PathVariable Long id, Model model) {
        model.addAttribute("finalExam", finalExamService.getFinalExamById(id));
        return "final-exams/viewFinalExam";
    }

    /**
     * Показва формата за добавяне на нов финален изпит.
     * @param model Модел, към който се добавят празен FinalExam обект и списък с курсове без изпит.
     * @return Името на шаблона 'final-exams/addFinalExam'.
     */
    @GetMapping("/add")
    public String showAddFinalExamForm(Model model) {
        model.addAttribute("finalExam", new FinalExam());
        model.addAttribute("courses", courseService.findCoursesWithoutFinalExam());
        return "final-exams/addFinalExam";
    }

    /**
     * Обработва създаването на нов финален изпит.
     * @param finalExam Обект, създаден от данните във формата.
     * @param bindingResult Обект за грешки от валидацията.
     * @param examQuestions Въпросите за изпита, подадени като един низ в специфичен формат.
     * @param courseId ID на курса, към който се добавя изпитът.
     * @param essayTopic Темата за есе.
     * @return Пренасочване към списъка с изпити при успех.
     */
    @PostMapping("/add")
    public String addFinalExam(@ModelAttribute @Valid FinalExam finalExam,
                               BindingResult bindingResult,
                               @RequestParam("examQuestions") String examQuestions,
                               @RequestParam("courseId") Long courseId,
                               @RequestParam("essayTopic") String essayTopic) {
        if (bindingResult.hasErrors()) {
            return "final-exams/addFinalExam";
        }
        Course course = courseService.findCourseById(courseId);
        finalExam.setCourse(course);
        finalExam.setEssayTopic(essayTopic);
        finalExamService.save(finalExam);
        List<Question> questions = parseQuestions(examQuestions, finalExam);
        questionRepository.saveAll(questions);
        return "redirect:/final-exams/list";
    }

    /**
     * Помощен метод за парсване на низ с въпроси в списък от обекти Question.
     * Очакван формат: "въпрос---отговор1=отговор2---верен_отговор;въпрос2---***---верен_отговор_отворен"
     * @param examQuestions Низът с въпроси.
     * @param finalExam Изпитът, към който принадлежат въпросите.
     * @return Списък с обекти {@link Question}.
     */
    public List<Question> parseQuestions(String examQuestions, FinalExam finalExam) {
        List<Question> questions = new ArrayList<>();
        String[] questionEntries = examQuestions.trim().split(";;;")[0].split(";");
        for (String entry : questionEntries) {
            String[] parts = entry.trim().split("---");
            if (parts.length < 2) continue;
            String questionText = parts[0].trim();
            String possibleAnswersPart = parts[1].trim();
            String correctAnswer = parts.length > 2 ? parts[2].trim() : "";
            List<String> possibleAnswers = !possibleAnswersPart.equals("***") ? Arrays.asList(possibleAnswersPart.split("=")) : null;
            Question createdQuestion = (possibleAnswers != null) ? new Question(questionText, possibleAnswers, correctAnswer) : new Question(questionText, correctAnswer);
            createdQuestion.setFinalExam(finalExam);
            questions.add(createdQuestion);
        }
        return questions;
    }

    /**
     * Изтрива финален изпит. Използва GET заявка, което не е препоръчителна практика.
     * @param id ID на изпита за изтриване.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Пренасочване към административния списък с изпити.
     */
    @GetMapping("/delete/{id}")
    public String deleteFinalExamViaGet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            finalExamService.deleteFinalExamById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Финалният изпит е изтрит успешно.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/final-exams";
    }

    /**
     * Обработва попълнен от потребител изпит.
     * Изчислява точки, изпраща есе за оценка към ChatGPT, записва резултата и показва страница с резултати.
     * @param id ID на попълнения изпит.
     * @param answers Карта с отговорите на потребителя.
     * @param essayAnswer Текстът на есето.
     * @param user Текущо логнатият потребител.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'final-exams/finalExamResults'.
     */
    @PostMapping("/submit/{id}")
    public String submitFinalExam(@PathVariable Long id, @RequestParam Map<String, String> answers, @RequestParam String essayAnswer, @AuthenticationPrincipal AppUser user, Model model) {
        FinalExam finalExam = finalExamService.getFinalExamById(id);
        int multipleChoiceScore = 0;
        int openEndedScore = 0;
        for (Question question : finalExam.getExamQuestions()) {
            String userAnswer = answers.get("question_" + question.getId());
            if (question.getPossibleAnswers().isEmpty()) {
                if (userAnswer != null && !userAnswer.trim().isEmpty()) openEndedScore += 2;
            } else {
                if (userAnswer != null && question.getCorrectAnswer().equalsIgnoreCase(userAnswer.trim())) multipleChoiceScore++;
            }
        }
        String prompt = String.format("Моля, оцени следното есе на база 5 критерия: Тема: \"%s\" Есе: %s Върни валиден JSON във формат: {\"съдържание\": число, \"структура\": число, \"стил\": число, \"оригиналност\": число, \"граматика\": число, \"общо\": число, \"коментар\": \"... \"}", finalExam.getEssayTopic(), essayAnswer);
        String rawFeedback = chatGPTService.askChatGPT(prompt);
        Map<String, Object> essayEvaluation = parseEssayFeedback(rawFeedback);
        int essayScore = (int) essayEvaluation.getOrDefault("общо", 0);
        String essayComment = (String) essayEvaluation.getOrDefault("коментар", "Няма налична обратна връзка.");
        int finalScore = multipleChoiceScore + openEndedScore + essayScore;
        boolean passed = finalScore >= PASSING_SCORE;
        finalExamService.submitNewExamResult(id, user, multipleChoiceScore, openEndedScore, essayScore, finalScore, passed, essayComment);
        model.addAttribute("result", new ExamResult(finalExam, user, multipleChoiceScore, openEndedScore, essayScore, essayComment, passed));
        model.addAttribute("essayFeedback", essayComment);
        model.addAttribute("shouldDownload", passed);
        return "final-exams/finalExamResults";
    }

    /**
     * Ендпойнт за изтегляне на сертификат след успешно взет изпит.
     * @param id ID на изпита.
     * @param user Текущо логнатият потребител.
     * @return {@link ResponseEntity}, съдържащ PDF файла на сертификата или съобщение за грешка.
     */
    @GetMapping("/{id}/certificate")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        Optional<ExamResult> optionalResult = examResultRepository.findByFinalExamIdAndUserId(id, user.getId());
        if (optionalResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ByteArrayResource("Няма резултат за този изпит.".getBytes()));
        }
        ExamResult result = optionalResult.get();
        if (!result.isPassed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ByteArrayResource("Нямате достъп до този сертификат.".getBytes()));
        }
        UserCourseRequest request = userCourseRequestService.findByUserAndCourse(user.getId(), result.getFinalExam().getCourse().getId());
        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ByteArrayResource("Не е открита заявка за участие в курса.".getBytes()));
        }
        byte[] pdfBytes = certificateGeneratorService.generateCertificateAsBytes(request, result.getFinalScore());
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ByteArrayResource("Генерирането на сертификата се провали.".getBytes()));
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf").contentType(MediaType.APPLICATION_PDF).contentLength(pdfBytes.length).body(new ByteArrayResource(pdfBytes));
    }

    /**
     * Помощен метод за парсване на JSON отговор от ChatGPT.
     * @param jsonResponse JSON низът, получен от AI услугата.
     * @return Карта (Map) с резултатите от оценката или карта с грешка при неуспешно парсване.
     */
    private Map<String, Object> parseEssayFeedback(String jsonResponse) {
        try {
            return new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {});
        } catch (Exception e) {
            return Map.of("коментар", "Неуспешно извличане на обратна връзка.", "общо", 0);
        }
    }
}