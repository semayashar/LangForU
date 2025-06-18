package com.example;

import java.time.Duration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.fail;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LectureTestCase {
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
    public void testLectionAdd() throws Exception {
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
        driver.findElement(By.xpath("//a[@id='add-lection']/div")).click();
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.get("http://localhost:8080/lections/add");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Английски език - Основни правила за изразяване и комуникация (A1)");
        driver.findElement(By.id("theme")).clear();
        driver.findElement(By.id("theme")).sendKeys("Основни изрази и структура на изречения за ежедневна комуникация");
        driver.findElement(By.id("videoUrl")).clear();
        driver.findElement(By.id("videoUrl")).sendKeys("https://www.youtube.com/watch?v=2WKJYekTKBQ");
        driver.findElement(By.id("difficultyLevel")).clear();
        driver.findElement(By.id("difficultyLevel")).sendKeys("A1 (начинаещи)");
        driver.findElement(By.xpath("//form[@id='lectionForm']/div[5]/div/span")).click();
        driver.findElement(By.xpath("//form[@id='lectionForm']/div[5]/div/ul/li[2]")).click();
        driver.findElement(By.id("releaseDate")).click();
        driver.findElement(By.linkText("12")).click();
        driver.findElement(By.id("instructor")).clear();
        driver.findElement(By.id("instructor")).sendKeys("Мария Георгиева");
        driver.findElement(By.id("additionalResources")).clear();
        driver.findElement(By.id("additionalResources")).sendKeys("Урок 2: Основи на граматиката - Времена и правилна структура на изреченията\nУпражнения за основи на изразяване и говорене");
        driver.findElement(By.id("summary")).clear();
        driver.findElement(By.id("summary")).sendKeys("В този урок ще научим основни правила за изразяване и изграждане на кратки изречения на английски език...");
        driver.findElement(By.id("exercisesText")).clear();
        driver.findElement(By.id("exercisesText")).sendKeys("Какъв филм гледахте миналия месец?---комедия=трилър=драма---драма;\nПътувахте ли някъде това лято?---да=не---не;\n...");
        driver.findElement(By.xpath("//form[@id='lectionForm']/div[12]/button")).click();
        driver.get("http://localhost:8080/admin/lections");
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }

    @Test
    @Order(2)
    public void testUpdateLecture() throws InterruptedException {
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
        driver.findElement(By.xpath("//a[@id='add-lection']/div")).click();
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.get("http://localhost:8080/lections/add");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Английски език - Основни правила за изразяване и комуникация (A1)");
        driver.findElement(By.id("theme")).clear();
        driver.findElement(By.id("theme")).sendKeys("Основни изрази и структура на изречения за ежедневна комуникация");
        driver.findElement(By.id("videoUrl")).clear();
        driver.findElement(By.id("videoUrl")).sendKeys("https://www.youtube.com/watch?v=2WKJYekTKBQ");
        driver.findElement(By.id("difficultyLevel")).clear();
        driver.findElement(By.id("difficultyLevel")).sendKeys("A1 (начинаещи)");
        driver.findElement(By.xpath("//form[@id='lectionForm']/div[5]/div/span")).click();
        driver.findElement(By.xpath("//form[@id='lectionForm']/div[5]/div/ul/li[2]")).click();
        driver.findElement(By.id("releaseDate")).click();
        driver.findElement(By.linkText("12")).click();
        driver.findElement(By.id("instructor")).clear();
        driver.findElement(By.id("instructor")).sendKeys("Мария Георгиева");
        driver.findElement(By.id("additionalResources")).clear();
        driver.findElement(By.id("additionalResources")).sendKeys("Урок 2: Основи на граматиката - Времена и правилна структура на изреченията\nУпражнения за основи на изразяване и говорене");
        driver.findElement(By.id("summary")).clear();
        driver.findElement(By.id("summary")).sendKeys("В този урок ще научим основни правила за изразяване и изграждане на кратки изречения на английски език. Ще се фокусираме върху ключови граматични правила за поставяне на глаголи, употреба на съществителни и прилагателни, както и общи фрази за ежедневна комуникация.");
        driver.findElement(By.id("exercisesText")).clear();
        driver.findElement(By.id("exercisesText")).sendKeys("Какъв филм гледахте миналия месец?---комедия=трилър=драма---драма;\nПътувахте ли някъде това лято?---да=не---не;\nПътувахте ли някъде тази зима?---***---не;\n;;;");
        driver.findElement(By.xpath("//form[@id='lectionForm']/div[12]/button")).click();
        driver.get("http://localhost:8080/admin/lections");
        driver.findElement(By.id("logout-button")).click();
        driver.get("http://localhost:8080/login?logout=true");
    }

    @Test
    @Order(3)
    public void testDeleteLecture() throws InterruptedException {
        driver.get("http://localhost:8080/login?logout=true");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("loginForm")).submit();
        driver.get("http://localhost:8080/profile");
        driver.findElement(By.linkText("Таблици")).click();
        driver.get("http://localhost:8080/admin/dashboard");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.id("lections")).click();
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.get("http://localhost:8080/admin/lections");
        Thread.sleep(3000); // Waits for 3000 milliseconds (3 seconds)
        driver.findElement(By.linkText("Изтриване")).click();
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
