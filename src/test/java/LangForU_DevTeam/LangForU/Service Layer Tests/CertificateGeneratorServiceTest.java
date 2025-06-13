package LangForU_DevTeam.LangForU.certificate;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.lections.Lection;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестове за класа CertificateGeneratorService.
 * Поради зависимостта от файловата система за шрифтове/изображения,
 * тези тестове са от тип "smoke test", които проверяват дали генерирането
 * преминава успешно, без да предизвиква грешки.
 */
class CertificateGeneratorServiceTest {

    private CertificateGeneratorService certificateGeneratorService;

    @BeforeEach
    void setUp() {
        // Инициализираме сервиза преди всеки тест.
        // Не използваме @InjectMocks, тъй като класът няма Spring зависимости.
        certificateGeneratorService = new CertificateGeneratorService();
    }

    @Test
    void generateCertificateAsBytes_withValidDirectData_shouldReturnValidPdfByteArray() throws Exception {
        // Arrange
        String fullName = "Иван Иванов";
        String egn = "0011223344";
        String courseName = "Английски език";
        LocalDate startDate = LocalDate.of(2025, 1, 10);
        LocalDate endDate = LocalDate.of(2025, 4, 10);
        int totalHours = 40;

        // Act
        byte[] pdfBytes = certificateGeneratorService.generateCertificateAsBytes(
                fullName, egn, courseName, startDate, endDate, totalHours
        );

        // Assert
        // 1. Проверяваме дали резултатът не е null.
        assertNotNull(pdfBytes);
        // 2. Проверяваме дали масивът има съдържание.
        assertTrue(pdfBytes.length > 0);
        // 3. Проверяваме "магическите байтове" - дали файлът започва със сигнатурата за PDF.
        String pdfMagicNumber = new String(pdfBytes, 0, 5);
        assertEquals("%PDF-", pdfMagicNumber);
    }

    @Test
    void generateCertificateAsBytes_fromUserCourseRequest_shouldReturnValidPdfByteArray() {
        // Arrange
        // Създаваме пълна структура от тестови обекти, за да симулираме реален UserCourseRequest
        AppUser user = new AppUser();
        user.setName("Мария Петрова");

        Course course = new Course();
        course.setLanguage("Немски език");
        course.setStartDate(LocalDate.of(2025, 2, 1));
        course.setEndDate(LocalDate.of(2025, 5, 1));
        course.setLections(List.of(new Lection(), new Lection())); // 2 лекции = 2 учебни часа според логиката

        UserCourseRequest request = new UserCourseRequest();
        request.setUser(user);
        request.setCourse(course);
        request.setPIN("9988776655");

        // Act
        byte[] pdfBytes = certificateGeneratorService.generateCertificateAsBytes(request, 85);

        // Assert
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        String pdfMagicNumber = new String(pdfBytes, 0, 5);
        assertEquals("%PDF-", pdfMagicNumber);
    }

    @Test
    void generateCertificateAsBytes_fromRequestWithNullData_shouldReturnNull() {
        // Arrange
        // Симулираме грешка, като подаваме заявка с липсващи данни (напр. user е null),
        // което ще предизвика NullPointerException вътре в метода.
        UserCourseRequest incompleteRequest = new UserCourseRequest();
        incompleteRequest.setCourse(new Course()); // User е null

        // Act
        // Методът трябва да "хване" грешката и да върне null, както е написано в catch блока му.
        byte[] pdfBytes = certificateGeneratorService.generateCertificateAsBytes(incompleteRequest, 85);

        // Assert
        assertNull(pdfBytes);
    }
}