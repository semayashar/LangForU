package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestLectionSystem {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String DRIVER_PATH = "C:\\Chrome\\chromedriver-win64\\chromedriver.exe";
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @Order(1)
    public void testLogin() {
        driver.get(BASE_URL + "/index");
        driver.findElement(By.linkText("Вход")).click();
        driver.get(BASE_URL + "/login");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.clear();
        emailField.sendKeys("semayasharova@gmail.com");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("Sema1234");

        driver.findElement(By.id("loginForm")).submit();

        assertTrue(driver.getCurrentUrl().contains("/profile"), "Failed to login successfully");
    }

    @Test
    @Order(2)
    public void testNavigateAndCompleteQuiz() {
        driver.get(BASE_URL + "/profile");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Безплатен курс по Английски език - A1'])[1]/following::a[1]")).click();

        driver.get(BASE_URL + "/courses/course/details/1");
        driver.findElement(By.linkText("Урок 5: Introduction to English / Въведение в Английския език")).click();

        driver.get(BASE_URL + "/lections/view/7");

        // Simplify quiz answers selection by looping through elements
        for (int i = 1; i <= 10; i++) {
            WebElement answer = driver.findElement(By.xpath(String.format("//form[@id='quizForm']/div[%d]/div/div[2]/div/div/label", i)));
            answer.click();
        }

        driver.findElement(By.xpath("//form[@id='quizForm']/button")).click();

        assertTrue(driver.getCurrentUrl().contains("/lections/view/7"), "Failed to complete the quiz successfully");
    }

    @Test
    @Order(3)
    public void testLogout() {
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button")));
        logoutButton.click();

        assertTrue(driver.getCurrentUrl().contains("/login?logout=true"), "Failed to log out successfully");
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}