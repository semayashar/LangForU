package LangForU_DevTeam.LangForU.lections;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseService;
import LangForU_DevTeam.LangForU.email.EmailSender;
import LangForU_DevTeam.LangForU.email.EmailTemplateService;
import LangForU_DevTeam.LangForU.question.Question;
import LangForU_DevTeam.LangForU.question.QuestionAnswer;
import LangForU_DevTeam.LangForU.question.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контролер, който управлява всички HTTP заявки, свързани с лекциите.
 * Включва административни CRUD операции, както и потребителски действия
 * като преглед на лекция и попълване на тестове към нея.
 */
@Controller
@RequestMapping("/lections")
public class LectionController {

    //<editor-fold desc="Dependencies">
    @Autowired
    private LectionService lectionService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private QuestionAnswerService questionAnswerService;
    @Autowired
    private EmailSender emailService;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private AppUserService userService;
    //</editor-fold>

    /**
     * Показва формата за добавяне на нова лекция.
     * @param model Модел, към който се добавят празен Lection обект и списък с всички курсове.
     * @return Името на шаблона 'lections/addLection'.
     */
    @GetMapping("/add")
    public String showAddLectionForm(Model model) {
        model.addAttribute("lection", new Lection());
        model.addAttribute("courses", courseService.getAllCourses());
        return "lections/addLection";
    }

    /**
     * Обработва създаването на нова лекция.
     * @param lection Обект, създаден от данните във формата.
     * @param examQuestions Въпросите към лекцията, подадени като един низ в специфичен формат.
     * @param courseId ID на курса, към който се добавя лекцията.
     * @return Пренасочване към административния списък с лекции.
     */
    @PostMapping("/add")
    public String addLection(@ModelAttribute Lection lection,
                             @RequestParam("examQuestions") String examQuestions,
                             @RequestParam("courseId") Long courseId) {
        Course course = courseService.findCourseById(courseId);
        lection.setCourse(course);

        List<Question> questions = parseQuestions(examQuestions, lection);
        lection.setQuestions(questions);

        lectionService.save(lection);
        return "redirect:/admin/lections";
    }

    /**
     * Показва детайли за лекция (администраторски изглед).
     * @param id ID на лекцията.
     * @param model Модел за подаване на данни.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Името на шаблона 'lections/viewLection' или пренасочване при грешка.
     */
    @GetMapping("/{id}")
    public String viewLectionAdmin(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Lection lection = lectionService.findById(id);
            model.addAttribute("lection", lection);
            return "lections/viewLection";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при показването на лекцията.");
            return "redirect:/admin/lections";
        }
    }

    /**
     * Показва детайли за лекция (потребителски изглед).
     * @param id ID на лекцията.
     * @param model Модел за подаване на данни.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Името на шаблона 'lections/lection' или пренасочване при грешка.
     */
    @GetMapping("/view/{id}")
    public String viewLectionUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Lection lection = lectionService.findById(id);
            model.addAttribute("lection", lection);
            return "lections/lection";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при показването на лекцията.");
            return "redirect:/admin/lections";
        }
    }

    /**
     * Показва формата за редактиране на съществуваща лекция.
     * Форматира въпросите от обекти към текстов низ за попълване на формата.
     * @param id ID на лекцията за редактиране.
     * @param model Модел за подаване на данни.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Името на шаблона 'lections/editLection'.
     */
    @GetMapping("/edit/{id}")
    public String showEditLectionForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Lection lection = lectionService.findById(id);
            // Обратно форматиране на въпросите в текстов низ за полето в HTML формата.
            String formattedQuestions = lection.getQuestions().stream()
                    .map(q -> String.join("---",
                            q.getQuestion(),
                            (q.getPossibleAnswers() != null && !q.getPossibleAnswers().isEmpty()) ? String.join("=", q.getPossibleAnswers()) : "***",
                            q.getCorrectAnswer()))
                    .collect(Collectors.joining(";"));

            model.addAttribute("lection", lection);
            model.addAttribute("examQuestions", formattedQuestions);
            model.addAttribute("courses", courseService.getAllCourses());
            return "lections/editLection";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при зареждането на лекцията.");
            return "redirect:/admin/lections";
        }
    }

    /**
     * Обработва редактирането на съществуваща лекция.
     * @param id ID на лекцията.
     * @param lection Обект с новите данни от формата.
     * @param examQuestions Въпросите като текстов низ.
     * @param summary Резюме на лекцията.
     * @param additionalResources Допълнителни ресурси.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Пренасочване към административния списък с лекции.
     */
    @PostMapping("/edit/{id}")
    public String editLection(@PathVariable Long id,
                              @ModelAttribute Lection lection,
                              @RequestParam("examQuestions") String examQuestions,
                              @RequestParam("summary") String summary,
                              @RequestParam("additionalResources") String additionalResources,
                              RedirectAttributes redirectAttributes) {
        try {
            Lection existing = lectionService.findById(id);
            existing.setName(lection.getName());
            existing.setTheme(lection.getTheme());
            existing.setVideoUrl(lection.getVideoUrl());
            existing.setDifficultyLevel(lection.getDifficultyLevel());
            existing.setInstructor(lection.getInstructor());
            existing.setReleaseDate(lection.getReleaseDate());
            existing.setCourse(courseService.findCourseById(lection.getCourse().getId()));
            existing.setSummary(summary);
            existing.setAdditionalResources(additionalResources);

            // Изчистване на старите и добавяне на новите/редактирани въпроси.
            List<Question> updatedQuestions = parseQuestions(examQuestions, existing);
            existing.getQuestions().clear();
            existing.getQuestions().addAll(updatedQuestions);

            lectionService.save(existing);
            redirectAttributes.addFlashAttribute("success", "Лекцията беше успешно редактирана!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при записа на лекцията.");
        }
        return "redirect:/admin/lections";
    }

    /**
     * Изтрива лекция по ID. Използва GET заявка.
     * @param id ID на лекцията за изтриване.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Пренасочване към административния списък с лекции.
     */
    @GetMapping("/delete/{id}")
    public String deleteLection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            lectionService.deleteLectionById(id);
            redirectAttributes.addFlashAttribute("success", "Лекцията беше изтрита успешно!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при изтриването на лекцията.");
        }
        return "redirect:/admin/lections";
    }

    /**
     * REST-подобен ендпойнт за обработка на отговори от тест към лекция.
     * @param userAnswers Карта с отговорите на потребителя.
     * @return {@link ResponseEntity}, съдържащ JSON с резултатите или съобщение за грешка.
     */
    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<?> submitAnswers(@RequestParam Map<String, String> userAnswers) {
        try {
            Long lectionId = Long.valueOf(userAnswers.get("lectionId"));
            Lection lection = lectionService.findById(lectionId);
            AppUser currentUser = userService.getCurrentUser();
            List<Map<String, Object>> questionsResult = new ArrayList<>();

            for (Question question : lection.getQuestions()) {
                String userAnswerText = userAnswers.get("question_" + question.getId());
                if (userAnswerText == null) continue;

                boolean answeredCorrectly = question.getCorrectAnswer().equals(userAnswerText);

                // Записва отговора на потребителя в базата данни.
                QuestionAnswer questionAnswer = new QuestionAnswer(null, currentUser, question, userAnswerText, answeredCorrectly, LocalDateTime.now());
                questionAnswerService.save(questionAnswer);

                questionsResult.add(Map.of("id", question.getId(), "answeredCorrectly", answeredCorrectly));
            }
            return ResponseEntity.ok(Map.of("questions", questionsResult));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Помощен метод за парсване на низ с въпроси в списък от обекти Question.
     * Очакван формат: "въпрос---отговор1=отговор2---верен_отговор;въпрос2---***---верен_отговор_отворен"
     * @param examQuestions Низът с въпроси.
     * @param lection Лекцията, към която принадлежат въпросите.
     * @return Списък с обекти {@link Question}.
     */
    public List<Question> parseQuestions(String examQuestions, Lection lection) {
        List<Question> questions = new ArrayList<>();
        String[] questionEntries = examQuestions.trim().split(";;;")[0].split(";");
        for (String entry : questionEntries) {
            String[] parts = entry.trim().split("---");
            if (parts.length < 2) continue;
            String questionText = parts[0].trim();
            String possibleAnswersPart = parts[1].trim();
            String correctAnswer = parts.length > 2 ? parts[2].trim() : "";
            List<String> possibleAnswers = !possibleAnswersPart.equals("***") ? Arrays.asList(possibleAnswersPart.split("=")) : null;
            Question question = (possibleAnswers != null) ? new Question(questionText, possibleAnswers, correctAnswer) : new Question(questionText, correctAnswer);
            question.setLection(lection);
            questions.add(question);
        }
        return questions;
    }
}