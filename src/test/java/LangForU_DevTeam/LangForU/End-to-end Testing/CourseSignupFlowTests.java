import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseSignupFlowTests {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    private static final String USER_EMAIL = "semayasharova@gmail.com";
    private static final String USER_PASSWORD = "Sema1234";

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) driver.quit();
    }

    private void login(String email, String password) {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginForm")).submit();
    }

    private void logout() {
        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button")));
        logoutBtn.click();
        wait.until(ExpectedConditions.urlContains("/login?logout"));
    }

    private void clearAndType(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }
    @Test
    @Order(1)
    public void testLoginAndCourseSignupFlowSlow() throws InterruptedException {
        login(USER_EMAIL, USER_PASSWORD);
        Thread.sleep(1000);

        wait.until(ExpectedConditions.urlContains("/profile"));
        Thread.sleep(1000);

        driver.findElement(By.linkText("Избери")).click();
        Thread.sleep(1000);

        wait.until(ExpectedConditions.urlContains("/courses"));
        Thread.sleep(1000);

        driver.findElement(By.id("course1")).click();
        Thread.sleep(1000);

        wait.until(ExpectedConditions.urlContains("/course/details"));
        Thread.sleep(1000);

        driver.findElement(By.id("signupButton")).click();
        Thread.sleep(1000);

        clearAndType(By.id("pin"), "0243065296");
        Thread.sleep(1000);

        driver.findElement(By.xpath("//form[@id='signupForm']//label[contains(text(),'Общи')]")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("signupButton")).click();
        Thread.sleep(1000);

        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'успешно')]")));
        assertTrue(successMsg.isDisplayed());

        Thread.sleep(2000); // Extra wait at the end to observe result
    }

}
