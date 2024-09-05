package LangForU_Development.LangForU.lections;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @ElementCollection
    @CollectionTable(name = "exercise_possible_answers", joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "possible_answer")
    private List<String> possibleAnswers; // Multiple-choice options, can be null for open-ended questions

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "user_answer")
    private String userAnswer;

    @Column(name = "is_answered")
    private boolean isAnswered;

    @Column(name = "is_answered_correctly")
    private boolean isAnsweredCorrectly;

    // Constructors for multiple-choice and open-ended questions
    public Exercise(String question, List<String> possibleAnswers, String correctAnswer) {
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswer = correctAnswer;
        this.isAnswered = false;
        this.isAnsweredCorrectly = false;
    }

    public Exercise(String question, String correctAnswer) {
        this.question = question;
        this.possibleAnswers = null;
        this.correctAnswer = correctAnswer;
        this.isAnswered = false;
        this.isAnsweredCorrectly = false;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", userAnswer='" + userAnswer + '\'' +
                ", isAnswered=" + isAnswered +
                ", isAnsweredCorrectly=" + isAnsweredCorrectly +
                '}';
    }
}