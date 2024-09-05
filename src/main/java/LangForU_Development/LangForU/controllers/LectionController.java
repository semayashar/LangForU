package LangForU_Development.LangForU.controllers;

import LangForU_Development.LangForU.courses.CourseService;
import LangForU_Development.LangForU.lections.Exercise;
import LangForU_Development.LangForU.lections.Lection;
import LangForU_Development.LangForU.lections.LectionService;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lections")
public class LectionController {

    @Autowired
    private LectionService lectionService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listLections(Model model) {
        model.addAttribute("lections", lectionService.findAll());
        return "lections/listLection";
    }

    @GetMapping("/add")
    public String showAddLectionForm(Model model) {
        model.addAttribute("lection", new Lection());
        model.addAttribute("courses", courseService.getAllCourses()); // Provide list of courses
        return "lections/addLection";
    }

    @PostMapping("/add")
    public String addLection(@ModelAttribute Lection lection, @RequestParam("exercisesText") String exercisesText) {
        List<Exercise> exercises = parseExercises(exercisesText);
        lection.setExercises(exercises);
        lectionService.saveLection(lection);
        return "redirect:/lections";
    }

    @GetMapping("/{id}")
    public String viewLectionAdmin(@PathVariable Long id, Model model) {
        Lection lection = lectionService.findById(id);
        model.addAttribute("lection", lection);
        return "lections/viewLection";
    }

    @GetMapping("/view/{id}")
    public String viewLectionUser(@PathVariable Long id, Model model) {
        Lection lection = lectionService.findById(id);
        model.addAttribute("lection", lection);
        return "user/lection";
    }

    @GetMapping("/edit/{id}")
    public String showEditLectionForm(@PathVariable Long id, Model model) {
        Lection lection = lectionService.findById(id);
        model.addAttribute("lection", lection);
        model.addAttribute("courses", courseService.getAllCourses()); // Provide list of courses
        return "lections/addLection";
    }

    @PostMapping("/edit/{id}")
    public String editLection(@PathVariable Long id, @ModelAttribute Lection lection, @RequestParam("exercisesText") String exercisesText) {
        List<Exercise> exercises = parseExercises(exercisesText);
        lection.setExercises(exercises);
        lectionService.saveLection(lection);
        return "redirect:/lections";
    }

    @GetMapping("/delete/{id}")
    public String deleteLection(@PathVariable Long id) {
        lectionService.deleteById(id);
        return "redirect:/lections";
    }

    @PostMapping("/submit")
    public ResponseEntity<List<Exercise>> submitAnswers(@RequestParam Map<String, String> allParams) {
        Long lectionId = Long.parseLong(allParams.get("lectionId"));
        Lection lection = lectionService.findById(lectionId);

        int correctAnswers = 0;

        for (Exercise exercise : lection.getExercises()) {
            String userAnswer = allParams.get("exercise_" + exercise.getId());
            String correctAnswer = exercise.getCorrectAnswer();

            // Print values for debugging
            System.out.println("Exercise ID: " + exercise.getId());
            System.out.println("User Answer: [" + userAnswer + "]"); // Enclosed in brackets for clarity
            System.out.println("Correct Answer: [" + correctAnswer + "]"); // Enclosed in brackets for clarity
            System.out.println("Correct Answers: [" + correctAnswers + "]"); // Enclosed in brackets for clarity

            boolean isCorrect = correctAnswer != null && userAnswer != null
                    && correctAnswer.trim().equalsIgnoreCase(userAnswer.trim());
            exercise.setUserAnswer(userAnswer);
            exercise.setAnswered(true);
            exercise.setAnsweredCorrectly(isCorrect);

            if (isCorrect) {
                correctAnswers++;
            }
        }

        lectionService.saveLection(lection);

        // Return the list of exercises with their answer status
        return ResponseEntity.ok(lection.getExercises());
    }


    /**
     * Parses a string of exercises formatted with questions, possible answers, and correct answers.
     *
     * The format of the input string is:
     * - Each question is followed by possible answers separated by '='.
     * - The correct answer is placed after the possible answers, separated by '---'.
     * - The end of the list of questions is marked by ';;;'.
     *
     * Example Input:
     * "What movie did you watch last month?---a comedy=a thriller=a drama---a drama;
     * Did you travel anywhere this summer?---yes=no---no;
     * Did you travel anywhere this winter?---***---no;
     * ;;;"
     *
     * Example Output:
     * [
     *     new Exercise("What movie did you watch last month?", ["a comedy", "a thriller", "a drama"], "a drama"),
     *     new Exercise("Did you travel anywhere this summer?", ["yes", "no"], "no"),
     *     new Exercise("Did you travel anywhere this winter?", null, "no")
     * ]
     *
     * - For each entry, the method:
     *   1. Splits the entry into question, possible answers, and correct answer based on '---'.
     *   2. Trims and processes the possible answers:
     *      - If possible answers are not '***', splits them by '=' to create a list.
     *      - If possible answers are '***', sets the possible answers list to null.
     *   3. Creates an `Exercise` object with the question, possible answers (if any), and the correct answer.
     *   4. Adds the `Exercise` object to the list of exercises.
     *
     * @param exercisesText The input string containing the exercises in the specified format.
     * @return A list of `Exercise` objects parsed from the input string.
     */
    public List<Exercise> parseExercises(String exercisesText) {
        List<Exercise> exercises = new ArrayList<>();

        // Trim the input text to handle any leading or trailing whitespace
        exercisesText = exercisesText.trim();

        // Split the text based on the end delimiter ";;;". We only take the part before ";;;"
        String[] exerciseEntries = exercisesText.split(";;;")[0].split(";");

        for (String entry : exerciseEntries) {
            // Trim each entry to remove extra whitespace
            String[] parts = entry.trim().split("---");

            // Check if we have at least the question and one other part
            if (parts.length < 2) {
                System.err.println("Skipping invalid exercise entry: " + entry);
                continue; // Skip this entry if it does not meet the requirements
            }

            String question = parts[0].trim();
            String possibleAnswersPart = parts[1].trim();
            String correctAnswer = parts.length > 2 ? parts[2].trim() : "";

            List<String> possibleAnswers = null;

            // Handle possible answers based on the presence of "***"
            if (!possibleAnswersPart.equals("***")) {
                possibleAnswers = Arrays.asList(possibleAnswersPart.split("="));
            }

            Exercise exercise;
            if (possibleAnswers != null) {
                exercise = new Exercise(question, possibleAnswers, correctAnswer); // Assuming difficulty level is "Medium"
            } else {
                exercise = new Exercise(question, correctAnswer);
            }

            exercises.add(exercise);
        }

        return exercises;
    }

}
