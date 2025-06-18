package com.example;

import java.time.Duration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.fail;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseTest {
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
    @Order(1)
    public void testAddCourse() throws Exception {
        driver.get("http://localhost:8080/index");
        driver.findElement(By.linkText("Вход")).click();
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("loginForm")).submit();
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Добави")).click();
        driver.get("http://localhost:8080/admin/dashboard");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.xpath("//a[@id='add-course']/div/span")).click();
        driver.get("http://localhost:8080/courses/add");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.id("language")).click();
        driver.findElement(By.id("language")).clear();
        driver.findElement(By.id("language")).sendKeys("Test Course");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[2]/div")).click();
        driver.findElement(By.xpath("//form[@id='courseForm']/div[2]/div/ul/li[2]")).click();
        driver.findElement(By.id("price")).click();
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("179.99");
        driver.findElement(By.id("startDate")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sat'])[1]/following::span[15]")).click();
        driver.findElement(By.id("startDate")).clear();
        driver.findElement(By.id("startDate")).sendKeys("12/01/2025");
        driver.findElement(By.id("endDate")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sat'])[2]/following::span[29]")).click();
        driver.findElement(By.id("endDate")).clear();
        driver.findElement(By.id("endDate")).sendKeys("26/01/2025");
        driver.findElement(By.id("mainInstructorName")).click();
        driver.findElement(By.id("mainInstructorName")).clear();
        driver.findElement(By.id("mainInstructorName")).sendKeys("Test Person");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[7]/label")).click();
        driver.findElement(By.id("assistantInstructorName")).click();
        driver.findElement(By.id("assistantInstructorName")).clear();
        driver.findElement(By.id("assistantInstructorName")).sendKeys("Test Person");
        driver.findElement(By.id("technicianName")).click();
        driver.findElement(By.id("technicianName")).clear();
        driver.findElement(By.id("technicianName")).sendKeys("Test Person");
        driver.findElement(By.id("pictureUrl")).click();
        driver.findElement(By.id("pictureUrl")).clear();
        driver.findElement(By.id("pictureUrl")).sendKeys("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqQXCfw2Ulfrfe1xG2NGkSe7FOnT0h9AEjcQ&s");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[10]/div")).click();
        driver.findElement(By.xpath("//form[@id='courseForm']/div[10]/div/ul/li[6]")).click();
        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("Test course.");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[12]/button")).click();
        driver.get("http://localhost:8080/courses");
        driver.findElement(By.name("searchLanguage")).click();
        driver.findElement(By.name("searchLanguage")).clear();
        driver.findElement(By.name("searchLanguage")).sendKeys("Test");
        driver.findElement(By.xpath("//form[@id='coursesSearchForm']/button")).click();
        driver.findElement(By.id("course1")).click();
        driver.get("http://localhost:8080/courses/view/25");
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }

    @Test
    @Order(2)
    public void testUpdateCourse() throws InterruptedException {
        driver.get("http://localhost:8080/index");
        driver.findElement(By.linkText("Вход")).click();
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("loginForm")).submit();
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Добави")).click();
        driver.get("http://localhost:8080/admin/dashboard");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)

        driver.findElement(By.id("courses")).click();
        driver.get("http://localhost:8080/admin/courses");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Преглед'])[26]/following::a[1]")).click();
        driver.get("http://localhost:8080/courses/edit/26");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)

        // Update course details
        driver.findElement(By.id("language")).clear();
        driver.findElement(By.id("language")).sendKeys("Test Course Update");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[2]/div")).click();
        driver.findElement(By.xpath("//form[@id='courseForm']/div[2]/div/ul/li[4]")).click();
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("99.05");
        driver.findElement(By.id("endDate")).clear();
        driver.findElement(By.id("endDate")).sendKeys("27/01/2025");
        driver.findElement(By.id("mainInstructorName")).clear();
        driver.findElement(By.id("mainInstructorName")).sendKeys("Test Person 1");
        driver.findElement(By.id("assistantInstructorName")).clear();
        driver.findElement(By.id("assistantInstructorName")).sendKeys("Test Person 2");
        driver.findElement(By.id("technicianName")).clear();
        driver.findElement(By.id("technicianName")).sendKeys("Test Person 3");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[10]/div")).click();
        driver.findElement(By.xpath("//form[@id='courseForm']/div[10]/div/ul/li[4]")).click();
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("Test course. Updated.");
        driver.findElement(By.xpath("//form[@id='courseForm']/div[12]/button")).click();

        // Verify updates
        driver.get("http://localhost:8080/courses");
        driver.findElement(By.name("searchLanguage")).clear();
        driver.findElement(By.name("searchLanguage")).sendKeys("Test");
        driver.findElement(By.id("coursesSearchForm")).submit();
        driver.findElement(By.id("course2")).click();

        // Logout
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }

    @Test
    @Order(3)
    public void testDeleteCourse() throws InterruptedException {
        // Login
        driver.get("http://localhost:8080/login?logout=true");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("loginForm")).submit();

        // Navigate to admin dashboard and courses
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Добави")).click();
        driver.get("http://localhost:8080/admin/dashboard");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)

        driver.findElement(By.xpath("//img[@alt='Courses']")).click();
        driver.get("http://localhost:8080/admin/courses");

        // Delete the specified course
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Редактарине'])[26]/following::a[1]")).click();

        // Logout
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
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

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}
