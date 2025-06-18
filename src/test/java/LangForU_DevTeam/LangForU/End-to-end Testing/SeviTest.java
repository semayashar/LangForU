package com.example;

import java.time.Duration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

class SeviTest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Chrome\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    void testSevi() throws Exception {
        driver.get("http://localhost:8080/index");
        driver.findElement(By.linkText("Вход")).click();
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("semayasharova@gmail.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Sema1234");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.get("http://localhost:8080/profile");
        driver.get("http://localhost:8080/Sevi/chat");

        // Send first message and verify assistant response
        sendMessageAndVerifyResponse("Здравей!");

        // Send second message and verify assistant response
        sendMessageAndVerifyResponse("Имам проблеми с глаголите в английския език.");

        // Start a new chat and verify assistant's welcome message
        driver.findElement(By.id("new-chat-btn")).click();
        Thread.sleep(2000); // Wait for the new chat to initialize
        verifyAssistantMessageExists();

        // Send another message in the new chat and verify assistant response
        sendMessageAndVerifyResponse("Коя си ти?");

        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }

    // Helper method to send a message and verify the assistant's response
    private void sendMessageAndVerifyResponse(String userMessage) throws InterruptedException {
        driver.findElement(By.id("userInput")).click();
        driver.findElement(By.id("userInput")).clear();
        driver.findElement(By.id("userInput")).sendKeys(userMessage);
        driver.findElement(By.xpath("//form[@id='chat-form']/button")).click();
        Thread.sleep(5000); // Wait for response
        verifyAssistantMessageExists();
    }

    // Helper method to verify if an assistant's response exists in the chat box
    private void verifyAssistantMessageExists() {
        // Locate the last assistant message in the chat box
        WebElement lastAssistantMessage = driver.findElement(By.cssSelector("#chat-box .assistant-message:last-child .chat-bubble"));
        assertNotNull(lastAssistantMessage, "No assistant response found in the chat box.");
        assertFalse(lastAssistantMessage.getText().isEmpty(), "Assistant response is empty.");
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
