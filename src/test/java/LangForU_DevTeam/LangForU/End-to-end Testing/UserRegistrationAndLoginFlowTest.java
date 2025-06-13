package LangForU_DevTeam.LangForU.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRegistrationAndLoginFlowTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String registeredEmail;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testUserRegistrationAndActivation() {
        driver.get("http://localhost:8080/");
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Регистрация"))).click();

        String name = "Иван Петров";
        registeredEmail = "ivan" + System.currentTimeMillis() + "@test.bg";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(name);
        driver.findElement(By.id("email")).sendKeys(registeredEmail);
        driver.findElement(By.id("password")).sendKeys("Test12345");
        driver.findElement(By.id("dateOfBirth"))
                .sendKeys("01/01/2000");
        driver.findElement(By.id("gender")).sendKeys("Мъжки");
        driver.findElement(By.id("address")).sendKeys("ул. Тестова 1");

        driver.findElement(By.cssSelector("form#registerForm button[type='submit']")).click();

        // Симулация на активация по имейл
        driver.get("http://localhost:8080/activate?token=someValidToken");
        wait.until(ExpectedConditions.urlContains("/activation-success"));
        Assertions.assertTrue(driver.getPageSource().contains("успешна активация"));
    }

    @Test
    @Order(2)
    public void testLoginWithActivatedUser() {
        driver.get("http://localhost:8080/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(registeredEmail);
        driver.findElement(By.id("password")).sendKeys("Test12345");
        driver.findElement(By.cssSelector("form#loginForm button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/profile"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/profile"));
        Assertions.assertTrue(driver.findElement(By.tagName("body")).getText().contains("Иван"));
    }
}