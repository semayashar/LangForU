package com.example;

import java.time.Duration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BlogTestCase {
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
    public void testBlogInteraction() throws Exception {
        driver.get("http://localhost:8080/index");
        driver.findElement(By.linkText("Блог")).click();
        driver.get("http://localhost:8080/blog");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Блог'])[3]/following::h2[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Английски'])[1]/following::span[3]")).click();
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("semayasharova@gmail.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Sema1234");
        driver.findElement(By.id("loginForm")).submit();
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Блог")).click();
        driver.get("http://localhost:8080/blog");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Блог'])[3]/following::h2[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Английски'])[1]/following::span[3]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Английски'])[1]/following::span[3]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Like'])[1]/following::h4[1]")).click();
        driver.findElement(By.id("commentText")).click();
        driver.findElement(By.id("commentText")).clear();
        driver.findElement(By.id("commentText")).sendKeys("Това е тестов коментар!");
        driver.findElement(By.xpath("//form[@id='commentForm']/div[2]/button")).click();
        driver.findElement(By.id("commentText")).click();
        driver.findElement(By.xpath("//form[@id='commentForm']/div[2]/button")).click();
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }

    @Test
    @Order(2)
    public void testBlogAdd() throws Exception {
        driver.get("http://localhost:8080/login?logout=true");
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("loginForm")).submit();
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Таблици")).click();
        driver.get("http://localhost:8080/admin/dashboard");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.xpath("//a[@id='add-blog']/div/span")).click();
        driver.get("http://localhost:8080/blog/add");
        driver.findElement(By.id("name")).click();
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Тестов Блог");
        driver.findElement(By.id("shortExplanation")).clear();
        driver.findElement(By.id("shortExplanation")).sendKeys("Кратко описание на Тестов Блог.");
        driver.findElement(By.id("blogText")).clear();
        driver.findElement(By.id("blogText")).sendKeys("Блог (на английски: blog), съкращение от Уеблог...");
        driver.findElement(By.id("image")).clear();
        driver.findElement(By.id("image")).sendKeys("https://cdn.prod.website-files.com/60f171dc609537e6e45cfbe7/60f171dc609537edb55d0ffc_Kakvo-e-blog-i-kak-mozhem-da-si-napravim-sobstven-takyv-1-612x408.jpeg");
        driver.findElement(By.id("categories")).clear();
        driver.findElement(By.id("categories")).sendKeys("Категория");
        driver.findElement(By.id("tags")).clear();
        driver.findElement(By.id("tags")).sendKeys("таг, таг две");
        driver.findElement(By.xpath("//form[@id='blogForm']/div[7]/button")).click();
        driver.get("http://localhost:8080/admin/blogs");
        driver.findElement(By.linkText("Блог")).click();
        driver.get("http://localhost:8080/blog");
        driver.findElement(By.name("query")).click();
        driver.findElement(By.name("query")).clear();
        driver.findElement(By.name("query")).sendKeys("Тестов");
        driver.findElement(By.xpath("//form[@id='blogSearchForm']/button")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Блог'])[3]/following::h2[1]")).click();
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }

    @Test
    @Order(3)
    public void testBlogUpdate() throws Exception {
        driver.get("http://localhost:8080/login?logout=true");
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("loginForm")).submit();
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Таблици")).click();
        driver.get("http://localhost:8080/admin/dashboard");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.id("blogs")).click();
        driver.get("http://localhost:8080/admin/blogs");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Преглед'])[4]/following::a[1]")).click();
        driver.get("http://localhost:8080/blog/edit/6");
        driver.findElement(By.id("name")).click();
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Тестов Блог 1");
        driver.findElement(By.id("shortExplanation")).click();
        driver.findElement(By.id("shortExplanation")).clear();
        driver.findElement(By.id("shortExplanation")).sendKeys("Кратко описание на Тестов Блог. Редактирано.");
        driver.findElement(By.id("blogText")).click();
        driver.findElement(By.id("blogText")).clear();
        driver.findElement(By.id("blogText")).sendKeys("Блог (на английски: blog), съкращение от Уеблог (на английски: weblog, буквално „уеб дневник“), е дискусионен или информационен уебсайт...");
        driver.findElement(By.id("categories")).click();
        driver.findElement(By.id("categories")).clear();
        driver.findElement(By.id("categories")).sendKeys("Категория, Тест");
        driver.findElement(By.id("tags")).click();
        driver.findElement(By.id("tags")).clear();
        driver.findElement(By.id("tags")).sendKeys("Таг едно, Таг две, Тест");
        driver.findElement(By.xpath("//form[@id='blogForm']/div[7]")).click();
        driver.findElement(By.xpath("//form[@id='blogForm']/div[7]/button")).click();
        driver.get("http://localhost:8080/blog");
        driver.findElement(By.name("query")).click();
        driver.findElement(By.name("query")).clear();
        driver.findElement(By.name("query")).sendKeys("Тестов");
        driver.findElement(By.xpath("//form[@id='blogSearchForm']/button")).click();
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }


    @Test
    @Order(4)
    public void testBlogDelete() throws Exception {
        driver.get("http://localhost:8080/login?logout=true");
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("loginForm")).submit();
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Таблици")).click();
        driver.get("http://localhost:8080/admin/dashboard");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.id("blogs")).click();
        driver.get("http://localhost:8080/admin/blogs");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Редактирай'])[4]/following::button[1]")).click();
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
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
