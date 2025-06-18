import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomePageTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        // Set the path to your WebDriver executable
        WebDriverManager.chromedriver().setup();
        // Configure ChromeOptions without headless mode
        ChromeOptions options = new ChromeOptions();

        driver = new ChromeDriver(options);

        // Initialize WebDriverWait with a timeout of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testNavigationButtons() {
        // Navigate to the home page
        driver.get("http://localhost:8080/");

        // Wait for the page to load
        wait.until(ExpectedConditions.titleContains("LangForU | Начало"));

        // Define navigation items and their expected URLs
        Map<String, String> navigationItems = Map.ofEntries(
                Map.entry("nav-home", "http://localhost:8080/index"),
                Map.entry("nav-courses", "http://localhost:8080/courses"),
                Map.entry("nav-about", "http://localhost:8080/about"),
                Map.entry("nav-sevi", "http://localhost:8080/Sevi"),
                Map.entry("nav-blog", "http://localhost:8080/blog"),
                Map.entry("nav-contact", "http://localhost:8080/contact")
        );

        for (Map.Entry<String, String> entry : navigationItems.entrySet()) {
            String buttonId = entry.getKey();
            String expectedUrl = entry.getValue();

            try {
                // Find the navigation button
                WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(buttonId)));

                // Click the button
                button.click();

                // Wait for the URL to change or load
                wait.until(ExpectedConditions.urlToBe(expectedUrl));

                // Assert that the URL is as expected
                assertEquals(expectedUrl, driver.getCurrentUrl());

                // Navigate back to the home page before the next iteration
                driver.get("http://localhost:8080/");
                wait.until(ExpectedConditions.titleContains("LangForU | Начало"));
            } catch (Exception e) {
                System.out.println("Test failed for button ID: " + buttonId);
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCourseCourseByLanguageButtons() {
        // Navigate to the home page
        driver.get("http://localhost:8080/");

        // Wait for the page to load
        wait.until(ExpectedConditions.titleContains("LangForU | Начало"));

        // Define course buttons and their expected URLs (the expected URL includes a query parameter for the language)
        Map<String, String> courseItems = Map.ofEntries(
                Map.entry("english-course-link", "http://localhost:8080/courses?searchLanguage=Английски"),
                Map.entry("german-course-link", "http://localhost:8080/courses?searchLanguage=Немски"),
                Map.entry("italian-course-link", "http://localhost:8080/courses?searchLanguage=Италиански"),
                Map.entry("spanish-course-link", "http://localhost:8080/courses?searchLanguage=Испански"),
                Map.entry("russian-course-link", "http://localhost:8080/courses?searchLanguage=Руски"),
                Map.entry("japanese-course-link", "http://localhost:8080/courses?searchLanguage=Японски")
        );

        for (Map.Entry<String, String> entry : courseItems.entrySet()) {
            String buttonId = entry.getKey();
            String expectedUrl = entry.getValue();

            try {
                // Find the course button by its ID
                WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(buttonId)));

                // Click the button
                button.click();

                // Wait for the URL to change or load
                wait.until(ExpectedConditions.urlContains("courses"));

                // Get the current URL and decode the searchLanguage parameter
                String currentUrl = driver.getCurrentUrl();
                String decodedUrl = URLDecoder.decode(currentUrl, StandardCharsets.UTF_8);

                // Decode the expected URL
                String decodedExpectedUrl = URLDecoder.decode(expectedUrl, StandardCharsets.UTF_8);

                // Assert that the decoded URLs match
                assertEquals(decodedExpectedUrl, decodedUrl);

                // Navigate back to the home page before the next iteration
                driver.get("http://localhost:8080/");
                wait.until(ExpectedConditions.titleContains("LangForU | Начало"));
            } catch (Exception e) {
                System.out.println("Test failed for course button with ID: " + buttonId);
                e.printStackTrace();
            }
        }
    }

    // Helper method to extract the searchLanguage parameter from the URL
    private String getSearchLanguageFromUrl(String url) {
        String searchLanguageParam = "searchLanguage=";
        int startIndex = url.indexOf(searchLanguageParam) + searchLanguageParam.length();
        int endIndex = url.indexOf("&", startIndex);
        if (endIndex == -1) {
            endIndex = url.length();
        }
        return url.substring(startIndex, endIndex);
    }

    @Test
    public void testHeroButtonRedirection() {
        // Navigate to the home page
        driver.get("http://localhost:8080/");

        // Wait for the hero section to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("hero__caption")));

        // Find the button
        WebElement button = driver.findElement(By.cssSelector(".hero__caption .btn.hero-btn"));

        // Click the button
        button.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/register"));
        assertEquals("http://localhost:8080/register", driver.getCurrentUrl());

    }

    @Test
    public void testHeroButtonRedirectionIfAuthenticated() {

        // Navigate to the login page
        driver.get("http://localhost:8080/login");

        // Wait until the page title contains the expected text and the URL is correct
        wait.until(ExpectedConditions.titleContains("LangForU | Влез в профил"));
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/login"));

        driver.findElement(By.id("email")).sendKeys("semayasharova@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Sema1234");

        // Wait for the submit button to be clickable
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));

        // Click the submit button
        submitButton.click();

        // **КОРЕКЦИЯ:** Wait for the redirection to the profile page before asserting the URL
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/profile"));
        assertEquals("http://localhost:8080/profile", driver.getCurrentUrl());

        // Navigate to the home page
        driver.get("http://localhost:8080/");

        // Wait for the hero section to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("hero__caption")));

        // Find the button
        WebElement button = driver.findElement(By.cssSelector(".hero__caption .btn.hero-btn"));

        // Click the button
        button.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/courses"));
        assertEquals("http://localhost:8080/courses", driver.getCurrentUrl());

        // Logout
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
        logoutButton.click();

        // Wait for successful logout (e.g., redirection to home page)
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/"));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}