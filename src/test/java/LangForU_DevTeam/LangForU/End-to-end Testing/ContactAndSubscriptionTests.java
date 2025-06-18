import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import java.time.Duration;

public class ContactAndSubscriptionTests {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Изпращане на съобщение през контактната форма (валидни данни).
    @Test
    public void testContactFormSubmissionWithValidData() {
        // TODO: Implement test logic
    }

    // Опит за изпращане на празна форма – очаквани валидационни грешки.
    @Test
    public void testEmptyContactFormSubmissionShowsValidationErrors() {
        // TODO: Implement test logic
    }

    // Абониране за бюлетин с валиден имейл.
    @Test
    public void testNewsletterSubscriptionWithValidEmail() {
        // TODO: Implement test logic
    }

    // Абониране с вече съществуващ имейл – предупреждение.
    @Test
    public void testSubscriptionWithExistingEmailShowsWarning() {
        // TODO: Implement test logic
    }

    // Абониране с невалиден имейл – грешка.
    @Test
    public void testSubscriptionWithInvalidEmailShowsError() {
        // TODO: Implement test logic
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}