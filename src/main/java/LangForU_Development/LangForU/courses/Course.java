package LangForU_Development.LangForU.courses;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.lections.Exercise;
import LangForU_Development.LangForU.lections.Lection;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Course {

    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String language;

    @NotNull
    @Pattern(regexp = "A1|A2|B1|B2|C1|C2|HSK1|HSK2|HSK3|HSK4")
    @Enumerated(EnumType.STRING)
    private Level level;

    @PositiveOrZero
    private float price;

    @FutureOrPresent
    private LocalDate startDate;

    @Future
    private LocalDate endDate;

    @ManyToMany(mappedBy = "courses")
    private List<AppUser> students;

    private String description;
    private String mainInstructorName;
    private String assistantInstructorName;
    private String technicianName;

    private String pictureUrl;

    private boolean enabled;

    private int rating;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    private List<Lection> lections;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    public long getDuration(){
        return ChronoUnit.WEEKS.between(startDate, endDate);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", level=" + level +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", students=" + students +
                ", description='" + description + '\'' +
                ", mainInstructorName='" + mainInstructorName + '\'' +
                ", assistantInstructorName='" + assistantInstructorName + '\'' +
                ", technicianName='" + technicianName + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", enabled=" + enabled +
                ", rating=" + rating +
                ", lections=" + lections +
                '}';
    }
}
