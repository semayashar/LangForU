package LangForU_DevTeam.LangForU.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseEnrollmentAndCompletionFlowTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("email")).sendKeys("ivan@test.bg");
        driver.findElement(By.name("password")).sendKeys("Test12345");
        driver.findElement(By.id("loginBtn")).click();
        wait.until(ExpectedConditions.urlContains("/profile"));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testCourseEnrollment() {
        driver.get("http://localhost:8080/courses");
        driver.findElement(By.cssSelector(".course-card a.details-btn")).click();
        driver.findElement(By.id("enrollBtn")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("enrollmentStatus"), "Успешно записване"));
        Assertions.assertTrue(driver.getPageSource().contains("записан успешно"));
    }

    @Test
    @Order(2)
    public void testLessonCompletionAndFinalExam() {
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.cssSelector(".my-courses .course-entry a.view-course")).click();

        // Преминаване през уроци
        for (int i = 1; i <= 3; i++) {
            driver.findElement(By.id("lesson" + i)).click();
            driver.findElement(By.id("exerciseAnswer")).sendKeys("правилен отговор" + i);
            driver.findElement(By.id("submitExercise")).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("exerciseResult"), "Верен отговор"));
        }

        // Финален изпит
        driver.findElement(By.id("finalExamBtn")).click();
        driver.findElement(By.name("q1")).sendKeys("Отговор 1");
        driver.findElement(By.name("q2")).sendKeys("Отговор 2");
        driver.findElement(By.name("essay")).sendKeys("Тестово есе по темата");
        driver.findElement(By.id("submitExam")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("examStatus"), "Успешно завършен"));
        driver.findElement(By.id("downloadCertificate")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Сертификат"));
    }
}