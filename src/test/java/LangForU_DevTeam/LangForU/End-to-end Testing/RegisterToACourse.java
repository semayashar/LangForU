import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)  // This specifies that tests will be ordered by the @Order annotation
public class RegisterToACourse {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
       WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @Order(1)  // Specifies that this test will run first
    public void testChangeAvatar() throws InterruptedException {
        driver.get("http://localhost:8080/login");
        wait.until(ExpectedConditions.titleContains("LangForU | Влез"));
        assertEquals("http://localhost:8080/login", driver.getCurrentUrl());

        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Admin123");
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/profile"));
        assertEquals("http://localhost:8080/profile", driver.getCurrentUrl());

        WebElement changeProfilePictureButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changeAvatarButton")));
        changeProfilePictureButton.click();

        WebElement image = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("avatar2")));
        image.click();

        WebElement uploadImageButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveAvatarButton")));
        uploadImageButton.click();

        // Logout
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
        logoutButton.click();
    }

    //Before starting this test you need to change the id of the selected course. Right now is course4.
    @Test
    @Order(2)
    public void testSignUpToACourse() throws InterruptedException {
        driver.get("http://localhost:8080/login");
        wait.until(ExpectedConditions.titleContains("LangForU | Влез"));
        assertEquals("http://localhost:8080/login", driver.getCurrentUrl());

        driver.findElement(By.id("email")).sendKeys("semayasharova@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Sema1234");
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/profile"));
        assertEquals("http://localhost:8080/profile", driver.getCurrentUrl());

        WebElement coursesList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-courses")));
        coursesList.click();

        WebElement course = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("course4")));
        course.click();

        WebElement registerButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupButton")));
        registerButton.click();

        String randomPin = String.format("%010d", (long) (Math.random() * 1_000_000_0000L));
        driver.findElement(By.id("pin")).sendKeys(randomPin);
        driver.findElement(By.id("bulgarianCitizen")).click();
        driver.findElement(By.id("acceptPolicy")).click();

        WebElement signupButton = driver.findElement(By.id("signupButton"));
        signupButton.click();

        wait.until(ExpectedConditions.urlContains("http://localhost:8080/courses/signedup/successfully"));
        assertTrue(driver.getCurrentUrl().contains("http://localhost:8080/courses/signedup/successfully"));

        // Logout
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
        logoutButton.click();
    }

    @Test
    @Order(3)  // Specifies that this test will run last
    public void testAcceptCourseRequest() throws InterruptedException {
        driver.get("http://localhost:8080/login");
        wait.until(ExpectedConditions.titleContains("LangForU | Влез"));
        assertEquals("http://localhost:8080/login", driver.getCurrentUrl());

        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Admin123");
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        WebElement coursesList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-admin-tables")));
        coursesList.click();

        WebElement course = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pending-users")));
        course.click();

        WebElement acceptButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmButton0")));
        acceptButton.click();

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
