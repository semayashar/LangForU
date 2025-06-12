package LangForU_DevTeam.LangForU.singUpForCourse;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRepository;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.courses.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * Сервизен клас, който капсулира бизнес логиката за управление на заявки
 * за записване в курсове. Обработва създаването, потвърждаването и извличането
 * на тези заявки.
 */
@Service
public class UserCourseRequestService {

    //<editor-fold desc="Dependencies">
    @Autowired
    private UserCourseRequestRepository userCourseRequestRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private CourseRepository courseRepository;
    //</editor-fold>

    /**
     * Създава нова заявка за записване в курс.
     *
     * @param userId      ID на потребителя, който прави заявката.
     * @param courseId    ID на курса, за който се кандидатства.
     * @param pin         ЕГН на потребителя.
     * @param citizenship Гражданство на потребителя.
     * @return Запазеният обект {@link UserCourseRequest}.
     */
    public UserCourseRequest createRequest(Long userId, Long courseId, String pin, String citizenship) {
        // 1. Намира потребителя и курса по техните ID-та.
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Потребител с ID: " + userId + " не е намерен."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс с ID: " + courseId + " не е намерен."));

        // 2. Създава и попълва нов обект за заявка.
        UserCourseRequest request = new UserCourseRequest();
        request.setUser(user);
        request.setCourse(course);
        request.setPIN(pin);
        request.setMadeRequest(LocalDateTime.now());
        request.setConfirmedRequest(null);
        request.setConfirmed(false);
        request.setCodeIBAN(generateUniqueCodeIBAN()); // Генерира уникален код за заявката.
        request.setCitizenship(citizenship);

        // 3. Запазва заявката в базата данни.
        return userCourseRequestRepository.save(request);
    }

    /**
     * Намира заявка по нейния ID.
     *
     * @param id ID на търсената заявка.
     * @return Намереният обект {@link UserCourseRequest}.
     * @throws RuntimeException ако заявка с такова ID не съществува.
     */
    public UserCourseRequest findById(Long id) {
        return userCourseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заявка за курс с ID: " + id + " не е намерена."));
    }

    /**
     * Помощен метод за генериране на уникален код (UUID).
     *
     * @return Уникален низ от символи.
     */
    private String generateUniqueCodeIBAN() {
        return UUID.randomUUID().toString();
    }

    /**
     * Извлича всички заявки, които все още не са потвърдени от администратор.
     *
     * @return Списък ({@link List}) от непотвърдени заявки.
     */
    public List<UserCourseRequest> getAllUnconfirmedUsers() {
        return userCourseRequestRepository.findByConfirmedFalse();
    }

    /**
     * Потвърждава заявка за записване в курс.
     * Тази операция маркира заявката като потвърдена и добавя курса
     * към списъка с курсове на потребителя.
     *
     * @param requestId ID на заявката за потвърждаване.
     */
    public void confirmUserCourseRequest(Long requestId) {
        UserCourseRequest request = userCourseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка с ID: " + requestId + " не е намерена."));

        if (!request.getConfirmed()) {
            request.setConfirmed(true);
            request.setConfirmedRequest(LocalDateTime.now());

            // Добавя курса към списъка с курсове на потребителя.
            AppUser user = request.getUser();
            user.getCourses().add(request.getCourse());

            appUserRepository.save(user); // Запазва обновения потребител.
            userCourseRequestRepository.save(request); // Запазва обновената заявка.
        } else {
            throw new RuntimeException("Заявката вече е потвърдена.");
        }
    }

    /**
     * Изтрива всички заявки за курсове, направени от конкретен потребител.
     *
     * @param userId ID на потребителя, чиито заявки ще бъдат изтрити.
     */
    public void deleteRequestsByUserId(Long userId) {
        userCourseRequestRepository.deleteByUserId(userId);
    }

    /**
     * Проверява дали съществува заявка за даден потребител и курс.
     *
     * @param userId   ID на потребителя.
     * @param courseId ID на курса.
     * @return {@code true} ако заявка съществува, в противен случай {@code false}.
     */
    public boolean existsByUserIdAndCourseId(Long userId, Long courseId) {
        return userCourseRequestRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    /**
     * Извлича всички заявки, направени от конкретен потребител.
     *
     * @param userId ID на потребителя.
     * @return Списък ({@link List}) от всички заявки на потребителя.
     */
    public List<UserCourseRequest> getRequestsByUserId(Long userId) {
        return userCourseRequestRepository.findAllByUserId(userId);
    }

    /**
     * Намира заявка по ID на потребителя и ID на курса.
     *
     * @param userId   ID на потребителя.
     * @param courseId ID на курса.
     * @return Намереният обект {@link UserCourseRequest}.
     * @throws RuntimeException ако заявка не е намерена.
     */
    public UserCourseRequest findByUserAndCourse(Long userId, Long courseId) {
        return userCourseRequestRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new RuntimeException("Няма заявка за потребителя с този курс."));
    }
}