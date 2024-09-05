package LangForU_Development.LangForU.courses;
import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.appuser.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Course a " +
            "SET a.enabled = TRUE WHERE a.id = ?1")
    int enableCourse(Long id);


    @Transactional
    @Modifying
    @Query("UPDATE Course a SET a.enabled = FALSE WHERE a.id = :id")
    int disableCourse(@Param("id") Long id);

    List<Course> findCoursesByEnabled(boolean enabled);
}
