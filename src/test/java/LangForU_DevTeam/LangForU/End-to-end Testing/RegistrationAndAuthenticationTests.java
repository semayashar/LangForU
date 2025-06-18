import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationAndAuthenticationTests {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    // Тестови данни
    private static final String UNIQUE_EMAIL = "test.user." + System.currentTimeMillis() + "@example.com";
    private static final String VALID_PASSWORD = "Password123";
    private static final String USER_FULL_NAME = "Тест Потребител";
    private static final String DATE_OF_BIRTH = "10/10/2000";
    private static final String ADDRESS = "гр. Тестов, ул. Примерна 1";

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void waitForPreloaderToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("preloader-active")));
        } catch (TimeoutException e) {
            // Игнорираме грешката, ако прелоудърът вече е изчезнал.
        }
    }

    private void clearAndSendKeys(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Test
    @Order(1)
    public void testSuccessfulRegistrationRequest() {
        driver.get(BASE_URL + "/register");
        waitForPreloaderToDisappear();
        clearAndSendKeys(By.id("email"), UNIQUE_EMAIL);
        clearAndSendKeys(By.id("password"), VALID_PASSWORD);
        clearAndSendKeys(By.id("name"), USER_FULL_NAME);
        clearAndSendKeys(By.id("dateOfBirth"), DATE_OF_BIRTH);
        clearAndSendKeys(By.id("address"), ADDRESS);
        driver.findElement(By.xpath("//button[text()='Регистрация']")).click();
        wait.until(ExpectedConditions.urlContains("/registrationSuccess"));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h3")));
        assertEquals("Успешна заявка за регистрация", successMessage.getText());
    }

    @Test
    @Order(2)
    public void testRegistrationWithExistingEmailShouldFail() {
        driver.get(BASE_URL + "/register");
        waitForPreloaderToDisappear();
        clearAndSendKeys(By.id("email"), UNIQUE_EMAIL);
        clearAndSendKeys(By.id("password"), VALID_PASSWORD);
        clearAndSendKeys(By.id("name"), "Друг Потребител");
        clearAndSendKeys(By.id("dateOfBirth"), DATE_OF_BIRTH);
        clearAndSendKeys(By.id("address"), ADDRESS);
        driver.findElement(By.xpath("//button[text()='Регистрация']")).click();
        waitForPreloaderToDisappear();
        WebElement errorTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Възникна грешка')]")));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(text(), 'Имейлът вече е зает.')]")));
        assertNotNull(errorTitle);
        assertNotNull(errorMessage);
    }

    @Test
    @Order(3)
    public void testRegistrationWithInvalidEmailShouldShowValidationError() {
        driver.get(BASE_URL + "/register");
        waitForPreloaderToDisappear();
        clearAndSendKeys(By.id("email"), "invalid-email");
        driver.findElement(By.id("name")).click();
        WebElement validationError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='email']/following-sibling::label[contains(@class, 'error-message')]")
        ));
        assertEquals("Моля, въведете валиден имейл адрес (напр. example@mail.com).", validationError.getText());
    }

    @Test
    @Order(4)
    public void testRegistrationWithMissingRequiredFieldsShouldFail() {
        driver.get(BASE_URL + "/register");
        waitForPreloaderToDisappear();
        clearAndSendKeys(By.id("email"), "another.test" + System.currentTimeMillis() + "@example.com");
        clearAndSendKeys(By.id("name"), USER_FULL_NAME);
        driver.findElement(By.xpath("//button[text()='Регистрация']")).click();
        WebElement validationError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='password']/following-sibling::label[contains(@class, 'error-message')]")
        ));
        assertEquals("Моля, въведете парола, за да продължите.", validationError.getText());
    }

    @Test
    @Order(5)
    public void testRegistrationWithWeakPasswordShouldShowValidationError() {
        driver.get(BASE_URL + "/register");
        waitForPreloaderToDisappear();
        clearAndSendKeys(By.id("email"), "weakpass.test." + System.currentTimeMillis() + "@example.com");
        clearAndSendKeys(By.id("password"), "weakpass");
        driver.findElement(By.id("name")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='password']/following-sibling::label[contains(@class, 'error-message')]")
        ));
        assertEquals("Паролата трябва да съдържа поне една малка и една главна буква.", error.getText());
    }

    @Test
    @Order(6)
    public void testLoginWithIncorrectPasswordShouldShowError() {
        driver.get(BASE_URL + "/login");
        waitForPreloaderToDisappear();
        clearAndSendKeys(By.id("email"), UNIQUE_EMAIL);
        clearAndSendKeys(By.id("password"), "WrongPassword123");
        driver.findElement(By.xpath("//button[contains(text(), 'Влез в профила')]")).click();
        // КОРЕКЦИЯ: По-устойчив локатор за грешката при вход
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(., 'Грешен имейл или парола')]")));
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    @Order(7)
    public void testLoginWithValidCredentialsAndLogout() {
        driver.get(BASE_URL + "/login");
        waitForPreloaderToDisappear();

        clearAndSendKeys(By.id("email"), "langforu.softdev@gmail.com");
        clearAndSendKeys(By.id("password"), "Admin123"); // увери се, че е с латинско 'A'

        driver.findElement(By.xpath("//button[contains(text(), 'Влез в профила')]")).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlToBe(BASE_URL + "/"),
                ExpectedConditions.urlToBe(BASE_URL + "/profile")
        ));
        waitForPreloaderToDisappear();

        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button")));
        logoutButton.click();

        wait.until(ExpectedConditions.urlContains("/login?logout"));
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@href='/login' and contains(text(),'Вход')]")
        ));
        assertTrue(loginButton.isDisplayed());
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}