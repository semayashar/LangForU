import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactFormValidationTests {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Chrome\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8080/contact"); // Adjust URL if necessary
    }

    @Test
    public void testEmptyFormSubmission() {
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Wait for the error messages to become visible
        WebElement nameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='name'].error")));
        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='email'].error")));
        WebElement subjectError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='subject'].error")));
        WebElement messageError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='message'].error")));

        // Assert the error messages are displayed and match the expected values
        assertTrue(nameError.isDisplayed(), "Name error message not displayed");
        assertTrue(nameError.getText().trim().equals("Хайде, имаш име, нали?") || nameError.getText().trim().equals("Името ти трябва да е поне 2 символа"), "Name error message mismatch");

        assertTrue(emailError.isDisplayed(), "Email error message not displayed");
        assertTrue(emailError.getText().trim().equals("Без имейл, без съобщение") || emailError.getText().trim().equals("Моля, въведете валиден имейл"), "Email error message mismatch");

        assertTrue(subjectError.isDisplayed(), "Subject error message not displayed");
        assertTrue(subjectError.getText().trim().equals("Хайде, имаш тема, нали?") || subjectError.getText().trim().equals("Темата трябва да е поне 4 символа"), "Subject error message mismatch");

        assertTrue(messageError.isDisplayed(), "Message error message not displayed");
        assertTrue(messageError.getText().trim().equals("Мм... да, трябва да напишеш нещо, за да изпратиш заявката.") || messageError.getText().trim().equals("Това ли е всичко? Наистина?"), "Message error message mismatch");
    }

    @Test
    public void testFormWithInvalidInput() {
        WebElement nameField = driver.findElement(By.id("name"));
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement subjectField = driver.findElement(By.id("subject"));
        WebElement messageField = driver.findElement(By.id("message"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // Clear fields to avoid any pre-existing text
        nameField.clear();
        emailField.clear();
        subjectField.clear();
        messageField.clear();

        // Enter invalid data into the fields
        nameField.sendKeys("A"); // Too short, should trigger minlength error
        emailField.sendKeys("invalid-email"); // Invalid email format
        subjectField.sendKeys("123"); // Too short, should trigger minlength error
        messageField.sendKeys("Short msg"); // Too short, should trigger minlength error

        submitButton.click();

        // Explicitly wait for the error messages to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='name'].error")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='email'].error")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='subject'].error")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='message'].error")));

        // Verify the error messages are visible and correct
        WebElement nameError = driver.findElement(By.cssSelector("label[for='name'].error"));
        WebElement emailError = driver.findElement(By.cssSelector("label[for='email'].error"));
        WebElement subjectError = driver.findElement(By.cssSelector("label[for='subject'].error"));
        WebElement messageError = driver.findElement(By.cssSelector("label[for='message'].error"));

        // Assert the error messages
        assertTrue(nameError.getText().trim().contains("Името ти трябва да е поне 2 символа"));
        assertTrue(emailError.getText().trim().contains("Моля, въведете валиден имейл"));
        assertTrue(subjectError.getText().trim().contains("Темата трябва да е поне 4 символа"));
        assertTrue(messageError.getText().trim().contains("Това ли е всичко? Наистина?"));
    }


    @Test
    public void testRedirectToSuccessPage() {
        WebElement nameField = driver.findElement(By.id("name"));
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement subjectField = driver.findElement(By.id("subject"));
        WebElement messageField = driver.findElement(By.id("message"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // Enter valid data into the form
        nameField.sendKeys("Иван Петров");
        emailField.sendKeys("ivan.petrov@example.com");
        subjectField.sendKeys("Запитване за продукт");
        messageField.sendKeys("Здравейте, искам да попитам за наличността на продуктите.");

        submitButton.click();

        // Wait for the page to redirect (check for the success parameter in the URL)
        wait.until(ExpectedConditions.urlContains("success=true"));

        // Verify the URL contains the success query parameter
        assertTrue(driver.getCurrentUrl().contains("success=true"), "URL does not contain 'success=true'");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
