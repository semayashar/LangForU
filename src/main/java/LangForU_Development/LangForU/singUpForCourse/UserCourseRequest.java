package LangForU_Development.LangForU.singUpForCourse;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.courses.Course;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class UserCourseRequest {

    @SequenceGenerator(
            name = "user_course_requests_sequence",
            sequenceName = "user_course_requests_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_course_requests_sequence"
    )
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Course course;

    @NotNull
    private String PIN;

    @NotNull
    private LocalDateTime madeRequest;

    private LocalDateTime confirmedRequest;

    @NotNull
    private Boolean confirmed = false;

    @Column(unique = true)
    private String codeIBAN;

    @NotNull
    private String citizenship;

    @Builder
    public UserCourseRequest(AppUser user, Course course, String PIN, LocalDateTime madeRequest, String codeIBAN, String citizenship) {
        this.user = user;
        this.course = course;
        this.PIN = PIN;
        this.madeRequest = madeRequest;
        this.codeIBAN = codeIBAN;
        this.citizenship = citizenship;
    }
}
