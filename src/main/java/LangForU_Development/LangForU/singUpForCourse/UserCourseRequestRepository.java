package LangForU_Development.LangForU.singUpForCourse;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCourseRequestRepository extends JpaRepository<UserCourseRequest, Long> {
    UserCourseRequest findByUserAndCourse(AppUser user, Course course);
    Optional<UserCourseRequest> findById(Long id);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    List<UserCourseRequest> findByConfirmedFalse();
    boolean existsByUserAndCourse(AppUser user, Course course);
}
