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

        WebElement registerForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerForm")));

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

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

        assertTrue(driver.getPageSource().contains("required"), "Validation message for required fields is missing.");
    }

    @Test
    public void testInvalidEmailFormat() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Хайде, не се опитвайте да ни надиграете с невалиден имейл!", emailError.getText(), "Validation message for invalid email is incorrect.");
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
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

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
    public void testInvalidPhoneNumberFormat() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("123abc456");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement phoneError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Телефонният номер може да съдържа само цифри и един '+'.", phoneError.getText(), "Validation message for invalid phone number is incorrect.");
    }

    @Test
    public void testPasswordWithoutUppercase() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("password123"); // No uppercase letter
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Паролата трябва да съдържа поне една главна и една малка буква.", passwordError.getText(), "Validation message for missing uppercase letter is incorrect.");
    }

    @Test
    public void testAgeBelowMinimum() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2015"); // User below minimum age
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement ageError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Трябва да сте поне 14 години.", ageError.getText(), "Validation message for age below minimum is incorrect.");
    }

    @Test
    public void testInvalidNameWithSpecialCharacters() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест@123"); // Invalid name with special characters
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement nameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Името трябва да съдържа само кирилски букви.", nameError.getText(), "Validation message for invalid name is incorrect.");
    }


    @Test
    public void testInvalidDateOfBirthFormat() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("2000-01-01"); // Incorrect format
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement dateError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Моля, въведете валидна дата в формат dd/mm/yyyy.", dateError.getText(), "Validation message for invalid date is incorrect.");
    }


    @Test
    public void testPhoneNumberValidation() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("invalid-phone");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement phoneError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Телефонният номер може да съдържа само цифри и един '+'.", phoneError.getText(), "Validation message for invalid phone number is incorrect.");
    }

    @Test
    public void testPasswordLengthValidation() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("short"); // Too short password
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Паролата трябва да бъде поне 8 символа, с поне една главна и една малка буква.", passwordError.getText(), "Validation message for short password is incorrect.");
    }

    @Test
    public void testInvalidNameFormat() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Test Name123"); // Invalid name with numbers
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement nameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Името трябва да съдържа само кирилски букви.", nameError.getText(), "Validation message for invalid name is incorrect.");
    }

    @Test
    public void testAgeValidation() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2018");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement ageError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Трябва да сте поне 14 години.", ageError.getText(), "Validation message for age is incorrect.");
    }

    @Test
    public void testAgeValidationAt14() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2011");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

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
    public void testAgeValidationAt13() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2012");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys("+359895093716");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement ageError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Трябва да сте поне 14 години.", ageError.getText(), "Validation message for age is incorrect.");
    }

    @Test
    public void testEmptyPhoneNumberField() {
        driver.get("http://localhost:8080/register");

        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(randomEmail);
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("name")).sendKeys("Тест Име");
        driver.findElement(By.id("dateOfBirth")).sendKeys("01/01/2000");
        driver.findElement(By.id("address")).sendKeys("Тестова улица 123");
        driver.findElement(By.id("phoneNumber")).sendKeys(""); // Empty phone number

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement phoneError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Кажете ни вашия телефонен номер!", phoneError.getText(), "Validation message for empty phone number is incorrect.");
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

        WebElement addressError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
        assertEquals("Как ще ви намерим без адрес?!", addressError.getText(), "Validation message for empty address is incorrect.");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
