package LangForU_DevTeam.LangForU.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlogInteractionFlowTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("email")).sendKeys("ivan@test.bg");
        driver.findElement(By.name("password")).sendKeys("Test12345");
        driver.findElement(By.id("loginBtn")).click();
        wait.until(ExpectedConditions.urlContains("/profile"));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testBlogLikeAndComment() {
        driver.get("http://localhost:8080/blog");
        driver.findElement(By.cssSelector(".blog-post a.read-more")).click();

        WebElement likeBtn = driver.findElement(By.id("likeButton"));
        int initialLikes = Integer.parseInt(driver.findElement(By.id("likeCount")).getText());
        likeBtn.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("likeCount"), String.valueOf(initialLikes + 1)));

        WebElement commentField = driver.findElement(By.name("comment"));
        commentField.sendKeys("Много интересна публикация!");
        driver.findElement(By.id("submitComment")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.className("comment-section"), "Много интересна публикация!"));
        Assertions.assertTrue(driver.getPageSource().contains("Много интересна публикация!"));
    }
}