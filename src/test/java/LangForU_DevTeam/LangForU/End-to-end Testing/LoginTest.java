import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        // Set the path to your WebDriver executable
        WebDriverManager.chromedriver().setup();
        // Configure ChromeOptions without headless mode
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Uncomment for headless mode

        driver = new ChromeDriver(options);

        // Initialize WebDriverWait with a timeout of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginPasswordTooShort() {
        driver.get("http://localhost:8080/login");

        // Short password
        driver.findElement(By.id("email")).sendKeys("testuser@gmail.com");
        driver.findElement(By.id("password")).sendKeys("short");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-error")));
        assertEquals("Паролата трябва да бъде поне 8 символа.", passwordError.getText(), "Validation message for short password is incorrect.");
    }

    @Test
    public void testLoginEmptyEmail() {
        driver.get("http://localhost:8080/login");

        // Empty email field
        driver.findElement(By.id("email")).sendKeys("");
        driver.findElement(By.id("password")).sendKeys("validPassword123");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-error")));
        assertEquals("Моля, напишете имейл.", emailError.getText(), "Validation message for empty email is incorrect.");
    }

    @Test
    public void testLoginEmptyPassword() {
        driver.get("http://localhost:8080/login");

        // Empty password field
        driver.findElement(By.id("email")).sendKeys("testuser@gmail.com");
        driver.findElement(By.id("password")).sendKeys("");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-error")));
        assertEquals("Паролата е задължителна!", passwordError.getText(), "Validation message for empty password is incorrect.");
    }

    @Test
    public void testLoginInvalidEmail() {
        driver.get("http://localhost:8080/login");

        // Invalid email format
        driver.findElement(By.id("email")).sendKeys("invalid-email");
        driver.findElement(By.id("password")).sendKeys("validPassword123");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-error")));
        assertEquals("Хайде, не се опитвайте с невалиден имейл!", emailError.getText(), "Validation message for invalid email is incorrect.");
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        driver.get("http://localhost:8080/login");

        // Invalid credentials
        driver.findElement(By.id("email")).sendKeys("invalidemail@domain.com");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        // Wait for the error message to be visible on the page
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'Грешен имейл или парола')]")));

        // Assert that the error message is displayed
        assertTrue(errorMessage.isDisplayed(), "Error message for invalid credentials was not shown.");
    }

    @Test
    public void testLoginWithValidCredentials() {
        driver.get("http://localhost:8080/login");

        // Valid credentials
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Admin123");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        // Wait for successful redirection (e.g., to the profile page)
        wait.until(ExpectedConditions.urlContains("/profile"));

        // Assert that the user is redirected to the profile page
        assertTrue(driver.getCurrentUrl().contains("/profile"), "User was not redirected to the profile page.");

        // Logout
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
        logoutButton.click();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}