package com.example;

import java.time.Duration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminFunctionalitiesTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://localhost:8080";
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() throws Exception {
       WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    private void login(String email, String password) {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginForm")).submit();
    }

    @Test
    public void testUserPropertiesChange() throws Exception {
        login("langforu.softdev@gmail.com", "Admin123");

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Таблици"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("registered-users"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Избери"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Активирай Акаунт"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Деактивирай Акаунт"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Изпрати заявка за 'АДМИН'"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Премахни потребител"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button"))).click();
    }

    @Test
    public void testConfirmPayment() throws Exception {
        login("langforu.softdev@gmail.com", "Admin123");

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Таблици"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pending-users"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("confirmButton0"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button"))).click();
    }

    @Test
    public void testAnswerRequest() throws Exception {
        login("langforu.softdev@gmail.com", "Admin123");

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Таблици"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='contact-requests']/div"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Избери"))).click();

        WebElement middleContent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("middleContent")));
        middleContent.clear();
        middleContent.sendKeys("Здравейте! Това е тестов случай. Благодаря за вниманието!");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='sendEmailForm']/button"))).click();

        // Repeatable actions for replying to requests
        middleContent.clear();
        middleContent.sendKeys("Здравейте, Това е тестов случай.");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='sendEmailForm']/button"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button"))).click();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }
}
