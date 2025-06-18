import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import java.time.Duration;

public class UserProfileAndInteractionsTests {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // TODO: Add login logic for a standard user
    }

    // Преглед на собствен профил и курсове.
    @Test
    public void testViewOwnProfileAndCourses() {
        // TODO: Implement test logic
    }

    // Преглед на курс заявки и статуси.
    @Test
    public void testViewCourseRequestsAndStatuses() {
        // TODO: Implement test logic
    }

    // Преглед на резултати от финални изпити.
    @Test
    public void testViewFinalExamResults() {
        // TODO: Implement test logic
    }

    // Изтриване на потребителски акаунт (ако функционалността съществува).
    @Test
    public void testDeleteUserAccount() {
        // TODO: Implement test logic
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}