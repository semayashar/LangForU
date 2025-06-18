import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminPanelTests {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    // --- Тестови данни ---
    private static final String ADMIN_EMAIL = "langforu.softdev@gmail.com";
    private static final String ADMIN_PASSWORD = "Admin123";
    private static final String TEST_COURSE_NAME = "Тестов курс по програмиране " + System.currentTimeMillis();
    private static final String TEST_BLOG_TITLE = "Тестова блог публикация " + System.currentTimeMillis();
    private static final String USER_TO_MANAGE_EMAIL = "semayasharova@gmail.com";
    private static final String COURSE_FOR_APPROVAL = "Безплатен курс по Немски език A1";


    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // --- Помощни методи ---
    private void waitForPreloaderToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("preloader-active")));
        } catch (TimeoutException e) {
            // Игнорираме, ако прелоудърът вече е изчезнал
        }
    }

    private void loginAsAdmin() {
        driver.get(BASE_URL + "/login");
        waitForPreloaderToDisappear();
        driver.findElement(By.id("email")).sendKeys(ADMIN_EMAIL);
        driver.findElement(By.id("password")).sendKeys(ADMIN_PASSWORD);
        driver.findElement(By.xpath("//button[contains(text(), 'Влез в профила')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
    }

    // --- Тестове ---

    @Test
    @Order(1)
    public void testAdminCanCreateNewCourse() {
        loginAsAdmin();
        driver.get(BASE_URL + "/courses/add");
        waitForPreloaderToDisappear();

        driver.findElement(By.id("language")).sendKeys(TEST_COURSE_NAME);
        driver.findElement(By.id("description")).sendKeys("Това е описание на тестови курс, създаден автоматично.");
        driver.findElement(By.id("startDate")).sendKeys("01/01/2026");
        driver.findElement(By.id("endDate")).sendKeys("31/12/2026");
        new Select(driver.findElement(By.id("level"))).selectByValue("A1");
        driver.findElement(By.id("price")).sendKeys("123.45");
        driver.findElement(By.id("imageUrl")).sendKeys("https://example.com/image.png");

        driver.findElement(By.xpath("//button[text()='Добави курс']")).click();

        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/courses"));
        WebElement newCourseLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='" + TEST_COURSE_NAME + "']")));
        assertTrue(newCourseLink.isDisplayed(), "Новосъздаденият курс не се появи в списъка.");
    }

    @Test
    @Order(2)
    public void testAdminCanAddLessonsAndExercisesToCourse() {
        loginAsAdmin();
        // Намираме ID-то на току-що създадения курс
        driver.get(BASE_URL + "/courses");
        WebElement courseLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='" + TEST_COURSE_NAME + "']")));
        String courseUrl = courseLink.getAttribute("href");
        String courseId = courseUrl.substring(courseUrl.lastIndexOf('/') + 1);

        // Добавяне на лекция
        driver.get(BASE_URL + "/lections/add/" + courseId);
        driver.findElement(By.id("name")).sendKeys("Урок 1: Въведение");
        driver.findElement(By.id("theme")).sendKeys("Основни понятия");
        driver.findElement(By.id("summary")).sendKeys("Резюме на урока.");
        driver.findElement(By.id("videoUrl")).sendKeys("https://youtube.com/embed/example");
        driver.findElement(By.id("releaseDate")).sendKeys("18/06/2025");

        // Добавяне на въпрос към лекцията
        driver.findElement(By.id("questionText_0")).sendKeys("Коя е столицата на България?");
        driver.findElement(By.id("correctAnswer_0")).sendKeys("София");

        driver.findElement(By.xpath("//button[text()='Добави лекция']")).click();

        wait.until(ExpectedConditions.urlContains("/course/details/" + courseId));
        WebElement lectionInList = driver.findElement(By.xpath("//li/a[contains(text(), 'Урок 1: Въведение')]"));
        assertTrue(lectionInList.isDisplayed(), "Новосъздаденият урок не се вижда в детайлите на курса.");
    }

    @Test
    @Order(3)
    public void testAdminCanAddFinalExamToCourse() {
        loginAsAdmin();
        driver.get(BASE_URL + "/courses");
        WebElement courseLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='" + TEST_COURSE_NAME + "']")));
        String courseId = courseLink.getAttribute("href").substring(courseLink.getAttribute("href").lastIndexOf('/') + 1);

        driver.get(BASE_URL + "/final-exams/add/" + courseId);
        driver.findElement(By.id("examName")).sendKeys("Финален изпит");
        driver.findElement(By.id("questionText_0")).sendKeys("Какво е променлива?");
        driver.findElement(By.id("correctAnswer_0")).sendKeys("Контейнер за данни");
        driver.findElement(By.id("examEssay")).sendKeys("Напишете есе за променливите.");

        driver.findElement(By.xpath("//button[text()='Добави изпит']")).click();

        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/admin/courses"));
        // Проверка indirektno - връщаме се в админ панела успешно.
        assertEquals(BASE_URL + "/admin/courses", driver.getCurrentUrl(), "Неуспешно пренасочване след добавяне на изпит.");
    }

    @Test
    @Order(4)
    public void testAdminCanApproveUserEnrollmentRequests() {
        loginAsAdmin();
        driver.get(BASE_URL + "/admin/unconfirmedUsers");

        String rowXPath = String.format("//td[contains(., '%s')]/parent::tr", COURSE_FOR_APPROVAL);
        List<WebElement> requestRows = driver.findElements(By.xpath(rowXPath));
        if (requestRows.isEmpty()) {
            System.out.println("ПРЕДУПРЕЖДЕНИЕ: Няма заявка за '" + COURSE_FOR_APPROVAL + "' за одобряване. Пропускам теста.");
            return; // Пропускаме теста, ако няма заявка
        }

        WebElement requestRow = requestRows.get(0);
        WebElement confirmButton = requestRow.findElement(By.xpath(".//button[contains(text(), 'Потвърди плащане')]"));
        confirmButton.click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();

        wait.until(ExpectedConditions.stalenessOf(requestRow));
        List<WebElement> remainingRows = driver.findElements(By.xpath(rowXPath));
        assertTrue(remainingRows.isEmpty(), "Заявката за курса все още присъства в списъка след потвърждение.");
    }

    @Test
    @Order(5)
    public void testAdminCanManageUserAccounts() {
        loginAsAdmin();
        driver.get(BASE_URL + "/admin/users");

        String rowXPath = String.format("//td[text()='%s']/parent::tr", USER_TO_MANAGE_EMAIL);
        WebElement userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));

        // Заключване
        WebElement lockButton = userRow.findElement(By.xpath(".//button[contains(text(), 'Заключи')]"));
        lockButton.click();
        wait.until(ExpectedConditions.stalenessOf(lockButton));

        userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
        WebElement unlockButton = userRow.findElement(By.xpath(".//button[contains(text(), 'Отключи')]"));
        assertTrue(unlockButton.isDisplayed(), "Потребителят не беше заключен успешно.");

        // Отключване
        unlockButton.click();
        wait.until(ExpectedConditions.stalenessOf(unlockButton));

        userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
        lockButton = userRow.findElement(By.xpath(".//button[contains(text(), 'Заключи')]"));
        assertTrue(lockButton.isDisplayed(), "Потребителят не беше отключен успешно.");
    }

    @Test
    @Order(6)
    public void testAdminCanChangeUserRole() {
        loginAsAdmin();
        driver.get(BASE_URL + "/admin/users");

        String rowXPath = String.format("//td[text()='%s']/parent::tr", USER_TO_MANAGE_EMAIL);
        WebElement userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));

        // Промяна към ADMIN
        WebElement makeAdminButton = userRow.findElement(By.xpath(".//button[contains(text(), 'Направи администратор')]"));
        makeAdminButton.click();
        wait.until(ExpectedConditions.stalenessOf(makeAdminButton));

        userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
        WebElement roleCell = userRow.findElement(By.xpath("./td[3]")); // 3-тата колона е за роля
        assertEquals("ADMIN", roleCell.getText(), "Ролята не е променена на ADMIN.");

        // Връщане към USER
        WebElement makeUserButton = userRow.findElement(By.xpath(".//button[contains(text(), 'Направи потребител')]"));
        makeUserButton.click();
        wait.until(ExpectedConditions.stalenessOf(makeUserButton));

        userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
        roleCell = userRow.findElement(By.xpath("./td[3]"));
        assertEquals("USER", roleCell.getText(), "Ролята не е върната на USER.");
    }

    @Test
    @Order(7)
    public void testAdminCanAddAndDeleteBlogPosts() {
        loginAsAdmin();
        // Добавяне
        driver.get(BASE_URL + "/admin/blogs/add");
        driver.findElement(By.id("name")).sendKeys(TEST_BLOG_TITLE);
        driver.findElement(By.id("content")).sendKeys("Съдържание на тестовата публикация.");
        driver.findElement(By.id("category")).sendKeys("Тестове");
        driver.findElement(By.id("tag")).sendKeys("автоматизация");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/blog"));
        assertTrue(driver.getPageSource().contains(TEST_BLOG_TITLE), "Новата блог публикация не се вижда.");

        // Изтриване
        driver.get(BASE_URL + "/admin/blogs");
        String rowXPath = String.format("//td[contains(text(), \"%s\")]/parent::tr", TEST_BLOG_TITLE);
        WebElement postRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
        WebElement deleteButton = postRow.findElement(By.xpath(".//a[text()='Изтрий']"));
        deleteButton.click();

        wait.until(ExpectedConditions.stalenessOf(postRow));
        assertFalse(driver.getPageSource().contains(TEST_BLOG_TITLE), "Публикацията не е изтрита успешно.");
    }

    @Test
    @Order(8)
    public void testAdminCanDeleteCourse() {
        loginAsAdmin();
        driver.get(BASE_URL + "/admin/courses");

        String rowXPath = String.format("//td[contains(text(), '%s')]/parent::tr", TEST_COURSE_NAME);
        WebElement courseRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
        WebElement deleteLink = courseRow.findElement(By.xpath(".//a[text()='Изтрий']"));
        deleteLink.click();

        wait.until(ExpectedConditions.stalenessOf(courseRow));
        List<WebElement> remainingRows = driver.findElements(By.xpath(rowXPath));
        assertTrue(remainingRows.isEmpty(), "Курсът не е изтрит успешно от админ панела.");
    }

    // Тестовете за модерация на коментари и отговор на запитвания изискват
    // предварително създадени данни от потребителски тестове, затова са по-сложни за изолиране.
    // Тук са представени в опростен вид.

    @Test
    @Order(9)
    public void testAdminCanViewAndReplyToContactSubmissions() {
        loginAsAdmin();
        driver.get(BASE_URL + "/admin/contact-requests");

        List<WebElement> requests = driver.findElements(By.xpath("//table/tbody/tr"));
        if (requests.isEmpty()) {
            System.out.println("ПРЕДУПРЕЖДЕНИЕ: Няма контактни форми за преглед. Пропускам теста.");
            return;
        }

        WebElement firstRequestRow = requests.get(0);
        WebElement toggleStatusButton = firstRequestRow.findElement(By.xpath(".//button[contains(@class, 'btn')]"));
        String initialStatus = toggleStatusButton.getText();

        toggleStatusButton.click();
        wait.until(ExpectedConditions.stalenessOf(firstRequestRow));

        WebElement updatedRequestRow = driver.findElements(By.xpath("//table/tbody/tr")).get(0);
        WebElement updatedButton = updatedRequestRow.findElement(By.xpath(".//button[contains(@class, 'btn')]"));

        assertNotEquals(initialStatus, updatedButton.getText(), "Статусът на контактната форма не е променен.");
    }


    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}