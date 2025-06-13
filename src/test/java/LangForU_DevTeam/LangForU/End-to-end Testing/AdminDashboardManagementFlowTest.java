package LangForU_DevTeam.LangForU.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminDashboardManagementFlowTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("email")).sendKeys("admin@test.bg");
        driver.findElement(By.name("password")).sendKeys("Admin12345");
        driver.findElement(By.id("loginBtn")).click();
        wait.until(ExpectedConditions.urlContains("/admin/dashboard"));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testCreateEditDeleteCourse() {
        driver.get("http://localhost:8080/admin/dashboard");
        driver.findElement(By.linkText("Управление на курсове")).click();
        driver.findElement(By.id("addCourseBtn")).click();

        driver.findElement(By.name("title")).sendKeys("Тестов курс");
        driver.findElement(By.name("description")).sendKeys("Описание на тестов курс");
        driver.findElement(By.id("saveCourseBtn")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Тестов курс"));

        driver.findElement(By.cssSelector(".course-row:last-child .edit-btn")).click();
        WebElement titleField = driver.findElement(By.name("title"));
        titleField.clear();
        titleField.sendKeys("Обновен курс");
        driver.findElement(By.id("saveCourseBtn")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Обновен курс"));

        driver.findElement(By.cssSelector(".course-row:last-child .delete-btn")).click();
        driver.switchTo().alert().accept();
        Assertions.assertFalse(driver.getPageSource().contains("Обновен курс"));
    }

    @Test
    @Order(2)
    public void testUserActivationDeactivation() {
        driver.get("http://localhost:8080/admin/users");
        driver.findElement(By.cssSelector(".user-row[data-email='ivan@test.bg'] .deactivate-btn")).click();
        driver.switchTo().alert().accept();
        Assertions.assertTrue(driver.getPageSource().contains("деактивиран успешно"));

        driver.findElement(By.cssSelector(".user-row[data-email='ivan@test.bg'] .activate-btn")).click();
        driver.switchTo().alert().accept();
        Assertions.assertTrue(driver.getPageSource().contains("активиран успешно"));
    }
}