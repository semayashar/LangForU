package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Lecture Management End-to-End Test")
class LectureTestCase {

    private static WebDriver driver;
    private static WebDriverWait wait;

    // --- Configuration ---
    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_EMAIL = "langforu.softdev@gmail.com";
    private static final String ADMIN_PASSWORD = "Admin123";
    // Use a static, unique name to ensure the tests can find the correct lecture to edit and delete.
    private static final String LECTURE_NAME = "Automated Test Lecture - " + System.currentTimeMillis();

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // Set up the explicit wait once. It can be reused throughout the tests.
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    @BeforeEach
    void setUp() {
        driver.get(BASE_URL);
        // Login before each test to ensure a consistent state
        loginAsAdmin();
    }

    @Test
    @Order(1)
    @DisplayName("Should add a new lecture")
    void testAddLecture() {
        // Navigate to the "Add Lection" form
        waitForElement(By.linkText("Добави")).click();
        waitForElement(By.id("add-lection")).click();

        // --- Fill out the form ---
        type(By.id("name"), LECTURE_NAME);
        type(By.id("theme"), "Automated Test Theme");
        type(By.id("videoUrl"), "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        type(By.id("difficultyLevel"), "A1 (Beginner)");

        // Select language from dropdown
        waitForElement(By.xpath("//form[@id='lectionForm']/div[5]/div/span")).click();
        waitForElement(By.xpath("//form[@id='lectionForm']/div[5]/div/ul/li[2]")).click(); // Select English

        // Select date from calendar
        waitForElement(By.id("releaseDate")).click();
        waitForElement(By.linkText("12")).click();

        type(By.id("instructor"), "Test Instructor");
        type(By.id("additionalResources"), "Automated test resources.");
        type(By.id("summary"), "Automated test summary.");
        type(By.id("exercisesText"), "Automated test exercises.");

        // Submit the form
        waitForElement(By.xpath("//form[@id='lectionForm']/div[12]/button")).click();

        // --- Verification ---
        driver.get(BASE_URL + "/admin/lections");
        boolean isPresent = isElementPresentWithWait(By.xpath(String.format("//td[text()='%s']", LECTURE_NAME)));
        assertTrue(isPresent, "Lecture should be present in the list after creation.");
    }

    @Test
    @Order(2)
    @DisplayName("Should update an existing lecture")
    void testUpdateLecture() {
        driver.get(BASE_URL + "/admin/lections");

        // Find the row for our specific lecture and click "Edit"
        WebElement lectureRow = findLectionRow(LECTURE_NAME);
        lectureRow.findElement(By.linkText("Редактиране")).click();

        // Update a field
        final String updatedSummary = "This summary has been updated by an automated test.";
        type(By.id("summary"), updatedSummary);

        // Save the changes
        waitForElement(By.xpath("//button[normalize-space()='Save Lection']")).click();

        // --- Verification ---
        driver.get(BASE_URL + "/admin/lections");
        // Re-navigate to the view/details page to verify the change
        findLectionRow(LECTURE_NAME).findElement(By.linkText("Преглед")).click();
        String bodyText = waitForElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains(updatedSummary), "The updated summary should be visible on the lecture view page.");
    }

    @Test
    @Order(3)
    @DisplayName("Should delete a lecture")
    void testDeleteLecture() {
        driver.get(BASE_URL + "/admin/lections");

        // Find the specific lecture and click "Delete"
        WebElement lectureRow = findLectionRow(LECTURE_NAME);
        lectureRow.findElement(By.linkText("Изтриване")).click();

        // Optional: If there is a javascript confirmation pop-up, you would accept it.
        // wait.until(ExpectedConditions.alertIsPresent()).accept();

        // --- Verification ---
        // Refresh the page and verify the lecture is gone
        driver.navigate().refresh();
        boolean isPresent = isElementPresentWithWait(By.xpath(String.format("//td[text()='%s']", LECTURE_NAME)));
        assertFalse(isPresent, "Lecture should NOT be present in the list after deletion.");
    }

    @AfterEach
    void tearDown() {
        // Logout after each test
        if (isElementPresentWithWait(By.id("logout-button"))) {
            waitForElement(By.id("logout-button")).click();
        }
    }

    @AfterAll
    static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- Private Helper Methods ---

    /**
     * Logs in the administrator using predefined credentials.
     */
    private void loginAsAdmin() {
        waitForElement(By.linkText("Вход")).click();
        type(By.id("email"), ADMIN_EMAIL);
        type(By.id("password"), ADMIN_PASSWORD);
        waitForElement(By.id("loginForm")).submit();
        // Verify login was successful by checking for the profile link
        assertTrue(isElementPresentWithWait(By.linkText("Профил")), "Login failed, 'Профил' link not found.");
    }

    /**
     * A robust helper to find an element, waiting for it to be clickable.
     * @param locator The By locator of the element.
     * @return The found WebElement.
     */
    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * A helper to safely clear an input field and type text into it.
     * @param locator The By locator of the input field.
     * @param text The text to type.
     */
    private void type(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Checks if an element is present on the page, waiting for a short period.
     * Useful for assertions where an element should (or should not) exist.
     * @param locator The By locator of the element.
     * @return True if the element is found, false otherwise.
     */
    private boolean isElementPresentWithWait(By locator) {
        try {
            // Use a shorter, temporary wait to quickly check for presence
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Finds the table row (tr) corresponding to a given lecture name.
     * @param lectureName The name of the lecture to find.
     * @return The WebElement for the entire table row.
     */
    private WebElement findLectionRow(String lectureName) {
        By locator = By.xpath(String.format("//td[text()='%s']/parent::tr", lectureName));
        return waitForElement(locator);
    }
}