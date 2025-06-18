import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationTest {

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
    public void testRegisterFormValidData() {
        driver.get("http://localhost:8080/register");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerForm")));

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        try {
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/registrationSuccess"));
            assertEquals("http://localhost:8080/registrationSuccess", driver.getCurrentUrl());
        } catch (TimeoutException e) {
            fail("Redirection failed. Current URL: " + driver.getCurrentUrl());
        }
    }

    @Test
    public void testRequiredFieldsValidation() {
        driver.get("http://localhost:8080/register");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-error")));
        assertEquals("Моля, въведете вашия имейл адрес.", emailError.getText(), "Validation message for required email is missing or incorrect.");
    }

    @Test
    public void testEmptyAddressField() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys(""); // Empty address

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement addressError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address-error")));
        assertEquals("Моля, въведете адрес.", addressError.getText(), "Validation message for empty address is incorrect.");
    }

    @Test
    public void testInvalidEmailFormat() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("email")).sendKeys("invalid-email");
        driver.findElement(By.id("password")).click(); // Click something else to trigger validation

        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-error")));
        assertEquals("Моля, въведете валиден имейл адрес (напр. example@mail.com).", emailError.getText(), "Validation message for invalid email is incorrect.");
    }

    @Test
    public void testAddressWithSpecialCharacters() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица, София.");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        try {
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/registrationSuccess"));
            assertEquals("http://localhost:8080/registrationSuccess", driver.getCurrentUrl());
        } catch (TimeoutException e) {
            fail("Redirection failed. Current URL: " + driver.getCurrentUrl());
        }
    }

    @Test
    public void testPasswordLengthValidation() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("password")).sendKeys("short");
        driver.findElement(By.id("email")).click();

        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-error")));
        assertEquals("Паролата трябва да съдържа поне 8 символа.", passwordError.getText(), "Validation message for short password is incorrect.");
    }

    @Test
    public void testPasswordWithoutUppercase() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("email")).click();

        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-error")));
        assertEquals("Паролата трябва да съдържа поне една малка и една главна буква.", passwordError.getText(), "Validation message for missing uppercase letter is incorrect.");
    }

    @Test
    public void testAgeBelowMinimum() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2015"); // User below minimum age
        driver.findElement(By.id("email")).click();

        WebElement ageError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dateOfBirth-error")));
        assertEquals("Необходимо е да имате навършени поне 14 години.", ageError.getText(), "Validation message for age below minimum is incorrect.");
    }

    @Test
    public void testInvalidNameWithSpecialCharacters() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("name")).sendKeys("Тест@123"); // Invalid name with special characters
        driver.findElement(By.id("email")).click();

        WebElement nameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name-error")));
        assertEquals("Името трябва да бъде изписано с кирилски букви.", nameError.getText(), "Validation message for invalid name is incorrect.");
    }


    @Test
    public void testInvalidDateOfBirthFormat() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("dateOfBirth")).sendKeys("2000-01-01"); // Incorrect format
        driver.findElement(By.id("email")).click();

        WebElement dateError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dateOfBirth-error")));
        assertEquals("Моля, използвайте формат dd/mm/yyyy.", dateError.getText(), "Validation message for invalid date is incorrect.");
    }

    @Test
    public void testInvalidNameFormat() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("name")).sendKeys("Test Name123"); // Invalid name with numbers
        driver.findElement(By.id("email")).click();

        WebElement nameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name-error")));
        assertEquals("Името трябва да бъде изписано с кирилски букви.", nameError.getText(), "Validation message for invalid name is incorrect.");
    }

    @Test
    public void testAgeValidationAt14() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2011"); // User is exactly 14
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        try {
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/registrationSuccess"));
            assertEquals("http://localhost:8080/registrationSuccess", driver.getCurrentUrl());
        } catch (TimeoutException e) {
            fail("Redirection failed for 14-year-old user. Current URL: " + driver.getCurrentUrl());
        }
    }

    @Test
    public void testAgeValidationAt13() {
        driver.get("http://localhost:8080/register");

        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2012"); // User is 13
        driver.findElement(By.id("email")).click();

        WebElement ageError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dateOfBirth-error")));
        assertEquals("Необходимо е да имате навършени поне 14 години.", ageError.getText(), "Validation message for age is incorrect.");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}