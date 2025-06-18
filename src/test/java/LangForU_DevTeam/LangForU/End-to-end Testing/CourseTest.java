import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CourseTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:8080/";
    private final String ADMIN_EMAIL = "langforu.softdev@gmail.com";
    private final String ADMIN_PASSWORD = "Admin123";
    // Generate a unique name for each test run to ensure isolation
    private final String UNIQUE_COURSE_NAME = "Test Course " + UUID.randomUUID().toString().substring(0, 8);


    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        login();
    }

    /**
     * Helper method to perform login. Avoids code duplication in tests.
     */
    private void login() {
        driver.get(BASE_URL + "login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));
        driver.findElement(By.id("email")).sendKeys(ADMIN_EMAIL);
        driver.findElement(By.id("password")).sendKeys(ADMIN_PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        // Wait for profile page to confirm successful login
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "profile"));
    }

    @Test
    @DisplayName("Should Add, Verify, and then Delete a New Course")
    public void testAddAndVerifyAndDeleteCourse() {
        // 1. Navigate to Add Course Page
        driver.get(BASE_URL + "courses/add");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("courseForm")));

        // 2. Fill out and submit the form
        fillCourseForm(UNIQUE_COURSE_NAME, "199.99");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='courseForm']/div[12]/button"))).click();

        // 3. Verify the course was created successfully
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "admin/courses"));
        // Check for a success message (Assuming one exists. If not, search for the course)
        // For example: assertTrue(driver.getPageSource().contains("Course added successfully"));
        searchForCourse(UNIQUE_COURSE_NAME);
        WebElement createdCourseLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(UNIQUE_COURSE_NAME)));
        assertTrue(createdCourseLink.isDisplayed(), "Course was not found in the list after creation.");
    }

    @Test
    @DisplayName("Should Add, Update, and Verify a Course")
    public void testUpdateAndVerifyCourse() {
        // ARRANGE: Create a course to be updated
        driver.get(BASE_URL + "courses/add");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("courseForm")));
        fillCourseForm(UNIQUE_COURSE_NAME, "199.99");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='courseForm']/div[12]/button"))).click();
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "admin/courses"));

        // ACT: Find the course and navigate to its edit page
        String updatedCourseName = "Updated " + UNIQUE_COURSE_NAME;
        String editButtonXpath = String.format("//td[contains(text(),'%s')]/following-sibling::td/a[contains(@href, 'edit')]", UNIQUE_COURSE_NAME);
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(editButtonXpath)));
        editButton.click();

        // Update the form
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("courseForm")));
        WebElement languageField = driver.findElement(By.id("language"));
        languageField.clear();
        languageField.sendKeys(updatedCourseName);
        WebElement priceField = driver.findElement(By.id("price"));
        priceField.clear();
        priceField.sendKeys("249.50");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[12]/button")).click();

        // ASSERT: Verify the course was updated
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "admin/courses"));
        searchForCourse(updatedCourseName);
        WebElement updatedCourseLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(updatedCourseName)));
        assertTrue(updatedCourseLink.isDisplayed(), "Course was not found with its updated name.");
    }

    /**
     * Helper method to fill out the course creation form to reduce duplication.
     * @param courseName The name of the course.
     * @param price The price of the course.
     */
    private void fillCourseForm(String courseName, String price) {
        driver.findElement(By.id("language")).sendKeys(courseName);
        driver.findElement(By.id("price")).sendKeys(price);
        driver.findElement(By.id("startDate")).sendKeys("01/01/2026");
        driver.findElement(By.id("endDate")).sendKeys("26/01/2026");
        driver.findElement(By.id("mainInstructorName")).sendKeys("Test Instructor");
        driver.findElement(By.id("pictureUrl")).sendKeys("https://via.placeholder.com/150");
        driver.findElement(By.id("description")).sendKeys("This is a test description.");
    }

    /**
     * Helper method to search for a course in the admin list.
     * @param courseName The name of the course to search for.
     */
    private void searchForCourse(String courseName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("searchLanguage")));
        WebElement searchBox = driver.findElement(By.name("searchLanguage"));
        searchBox.clear();
        searchBox.sendKeys(courseName);
        searchBox.submit();
    }


    @AfterEach
    public void tearDown() {
        // Clean up: Find and delete the created course to ensure test isolation
        try {
            driver.get(BASE_URL + "admin/courses");
            searchForCourse(UNIQUE_COURSE_NAME);
            // This dynamic XPath finds the delete button in the same row as the course with the unique name
            String deleteButtonXpath = String.format("//td[contains(text(),'%s')]/following-sibling::td/a[contains(@href, 'delete')]", UNIQUE_COURSE_NAME);
            WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(deleteButtonXpath)));
            deleteButton.click();
            // Optional: Confirm deletion, e.g., by waiting for a success message or ensuring the element is gone.
        } catch (Exception e) {
            // Ignore if the element is not found, it might have been deleted by the test itself
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}