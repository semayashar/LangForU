import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class SubscribeToBlogNotification {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        // Set the path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Chrome\\chromedriver-win64\\chromedriver.exe");

        // Configure ChromeOptions
        ChromeOptions options = new ChromeOptions();
        // Uncomment for headless mode if required
        // options.addArguments("--headless");

        driver = new ChromeDriver(options);

        // Initialize WebDriverWait with a timeout of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testSubscribeToBlog() {
        // Navigate to the blog page
        driver.get("http://localhost:8080/blog");

        // Fill in the email field
        WebElement emailInput = driver.findElement(By.id("emailInput"));
        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys("test" + System.currentTimeMillis() + "@example.com");

        // Submit the subscription form
        WebElement subscribeButton = driver.findElement(By.xpath("//form[@id='newsletterForm']/button"));
        subscribeButton.click();

        // Wait for the success message to appear
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successMessage")));

        // Assert the success message is displayed
        assertTrue(successMessage.isDisplayed(), "Success message should be displayed after subscription");
    }

    @Test
    public void testSubscribeWithoutEmail() {
        // Navigate to the blog page
        driver.get("http://localhost:8080/blog");

        // Find the email input field
        WebElement emailInput = driver.findElement(By.id("emailInput"));

        // Ensure the email field is empty
        emailInput.clear();

        // Click the subscribe button without entering an email
        WebElement subscribeButton = driver.findElement(By.xpath("//form[@id='newsletterForm']/button"));
        subscribeButton.click();

        // Wait for the error message <label> element to appear
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("label[for='emailInput'].error.validation-message.text-danger")
        ));

        // Assert the error message is displayed
        assertTrue(errorMessage.isDisplayed(), "Error message should be displayed when email is not entered");

        // Assert the error message text is correct
        assertEquals("Моля, напишете имейл.", errorMessage.getText(), "Error message text should match expected");
    }

    @Test
    public void testSubscribeWithInvalidEmail() {
        // Navigate to the blog page
        driver.get("http://localhost:8080/blog");

        // Find the email input field
        WebElement emailInput = driver.findElement(By.id("emailInput"));

        // Enter an invalid email address
        emailInput.clear();
        emailInput.sendKeys("invalid-email");

        // Click the subscribe button
        WebElement subscribeButton = driver.findElement(By.xpath("//form[@id='newsletterForm']/button"));
        subscribeButton.click();

        // Wait for the error message <label> element to appear for invalid email
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("label[for='emailInput'].error.validation-message.text-danger")
        ));

        // Assert the error message is displayed
        assertTrue(errorMessage.isDisplayed(), "Error message should be displayed for invalid email");

        // Assert the error message text is correct for invalid email
        assertEquals("Моля, напишете валиден имейл.", errorMessage.getText(), "Error message text should match expected for invalid email");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
