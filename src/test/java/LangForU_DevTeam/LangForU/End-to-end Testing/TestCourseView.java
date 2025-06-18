package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class TestCourseView {

    private WebDriver driver;
    private WebDriverWait wait;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeEach
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Chrome\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @Test
    public void testCourseView() throws Exception {
        // Navigate to the index page and click on the 'Courses' link
        driver.get("http://localhost:8080/index");
        driver.findElement(By.linkText("Курсове")).click();

        // Go to the courses page and select the first course
        driver.get("http://localhost:8080/courses");
        driver.findElement(By.id("course1")).click();

        // Start the course by clicking on 'Start Now'
        driver.findElement(By.linkText("Започни сега!")).click();

        // Navigate to the login page and enter login credentials
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("semayasharova@gmail.com");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Sema1234");
        driver.findElement(By.id("loginForm")).submit();

        // Go to profile page after successful login
        driver.get("http://localhost:8080/profile");

        // Navigate back to courses and select the course again
        driver.findElement(By.linkText("Курсове")).click();
        driver.get("http://localhost:8080/courses");
        driver.findElement(By.id("course8")).click();

        // Attempt to sign up for the course again
        driver.findElement(By.id("signupButton")).click();
        driver.findElement(By.id("pin")).clear();
        driver.findElement(By.id("pin")).sendKeys("0243000000");
        driver.findElement(By.xpath("//form[@id='signupForm']/div[2]/label[2]")).click();
        driver.findElement(By.id("acceptPolicy")).click();
        driver.findElement(By.id("signupButton")).click();

        // Check if redirected to /signedup/successfully or the alert is displayed
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.equals("http://localhost:8080/signedup/successfully")) {
            // Pass the test as the user was redirected successfully
            System.out.println("Test passed: Redirected to /signedup/successfully.");
        } else {
            // Assert that the user is already signed up for this course
            String alertText = closeAlertAndGetItsText();
            assertEquals("You are already signed up for this course.", alertText);
        }

        // Go back to the courses page
        driver.findElement(By.linkText("Курсове")).click();
    }

    @Test
    public void testSingUpCourse() throws Exception {
        // Navigate to the index page and click on 'Login'
        driver.get("http://localhost:8080/index");
        driver.findElement(By.linkText("Вход")).click();

        // Navigate to login page and enter login credentials
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("semayasharova@gmail.com");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Sema1234");
        driver.findElement(By.id("loginForm")).submit();

        // Navigate to profile page after successful login
        driver.get("http://localhost:8080/profile");

        // Navigate to courses page and select the course
        driver.findElement(By.id("navigation")).click();
        driver.findElement(By.linkText("Курсове")).click();
        driver.get("http://localhost:8080/courses");
        driver.findElement(By.id("course7")).click();

        // Attempt to sign up for the selected course
        driver.findElement(By.id("signupButton")).click();
        driver.findElement(By.id("pin")).clear();
        driver.findElement(By.id("pin")).sendKeys("0243000000");

        // Accept terms and conditions
        driver.findElement(By.xpath("//form[@id='signupForm']/div[2]/label[2]")).click();
        driver.findElement(By.xpath("//form[@id='signupForm']/div[3]/label")).click();

        // Finalize the sign-up process
        driver.findElement(By.id("signupButton")).click();

        // You can add assertions here to verify if sign-up was successful, e.g., checking for success messages or redirects.
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
