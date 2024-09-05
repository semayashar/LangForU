package LangForU_Development.LangForU.lections;

import LangForU_Development.LangForU.courses.Course;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lections")
public class Lection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String theme;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "difficulty_level", nullable = false)
    private String difficultyLevel;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String instructor;

    @Column(name = "view_count")
    private int viewCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lection_id")
    private List<Exercise> exercises;

    @Column(name = "additional_resource")
    private String additionalResources;

    @Column(name = "lection_summary")
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public void incrementViewCount() {
        this.viewCount++;
    }

    @Override
    public String toString() {
        return "Lection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theme='" + theme + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", releaseDate=" + releaseDate +
                ", instructor='" + instructor + '\'' +
                ", viewCount=" + viewCount +
                ", exercises=" + exercises +
                ", additionalResources=" + additionalResources +
                '}';
    }

    public Lection(String name, String theme, String videoUrl, String difficultyLevel, LocalDate releaseDate, String instructor, List<Exercise> exercises, String additionalResources, String summary, Course course) {
        this.name = name;
        this.theme = theme;
        this.videoUrl = videoUrl;
        this.difficultyLevel = difficultyLevel;
        this.releaseDate = releaseDate;
        this.instructor = instructor;
        this.viewCount = 0;
        this.exercises = exercises;
        this.additionalResources = additionalResources;
        this.summary = summary;
        this.course = course;
    }
}
