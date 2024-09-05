package LangForU_Development.LangForU.singUpForCourse;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.appuser.AppUserRepository;
import LangForU_Development.LangForU.courses.Course;
import LangForU_Development.LangForU.courses.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserCourseRequestService {

    @Autowired
    private UserCourseRequestRepository userCourseRequestRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public boolean isUserEnrolled(Long userId, Long courseId) {
        return userCourseRequestRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    @Transactional
    public UserCourseRequest createRequest(Long userId, Long courseId, String pin, String citizenship) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if the user is already signed up for this course
        if (userCourseRequestRepository.existsByUserAndCourse(user, course)) {
            throw new IllegalStateException("You are already signed up for this course.");
        }

        UserCourseRequest request = UserCourseRequest.builder()
                .user(user)
                .course(course)
                .PIN(pin)
                .madeRequest(LocalDateTime.now())
                .codeIBAN(null)
                .citizenship(citizenship)
                .build();

        return userCourseRequestRepository.save(request);
    }

    public UserCourseRequest findById(Long id) {
        return userCourseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserCourseRequest not found with ID: " + id));
    }

    private String generateUniqueCodeIBAN() {
        return UUID.randomUUID().toString();
    }

    public List<UserCourseRequest> getAllUnconfirmedUsers() {
        return userCourseRequestRepository.findByConfirmedFalse();
    }

    public void confirmUserCourseRequest(Long requestID) {
        UserCourseRequest request = userCourseRequestRepository.findById(requestID)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request != null && !request.getConfirmed()) {
            request.setConfirmed(true);
            request.setConfirmedRequest(LocalDateTime.now());

            AppUser user = request.getUser();
            user.getCourses().add(request.getCourse());

            appUserRepository.save(user);
            userCourseRequestRepository.save(request);
        } else {
            throw new RuntimeException("Request not found or already confirmed");
        }
    }
}
