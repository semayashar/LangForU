import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoursesAndLearningTests {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    // Тестови данни за логнат потребител
    private static final String USER_EMAIL = "semayasharova@gmail.com";
    private static final String USER_PASSWORD = "Sema1234";
    private static String enrolledCourseName = "Немски език";

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Премахнете коментара, за да изпълните тестовете без визуален интерфейс
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Помощен метод за изчакване прелоудърът да изчезне
    private void waitForPreloaderToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("preloader-active")));
        } catch (TimeoutException e) {
            // Игнорираме, ако вече е изчезнал или не съществува
        }
    }

    // Помощен метод за вход в системата
    private void login() {
        driver.get(BASE_URL + "/login");
        waitForPreloaderToDisappear();
        driver.findElement(By.id("email")).sendKeys(USER_EMAIL);
        driver.findElement(By.id("password")).sendKeys(USER_PASSWORD);
        driver.findElement(By.xpath("//button[contains(text(), 'Влез в профила')]")).click();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/profile"),
                ExpectedConditions.urlToBe(BASE_URL + "/")
        ));
        waitForPreloaderToDisappear();
    }

    // 1. Преглед на публичен списък с курсове.
    @Test
    @Order(1)
    public void testViewPublicCourseList() {
        driver.get(BASE_URL + "/courses");
        waitForPreloaderToDisappear();
        assertEquals("LangForU | Курсове", driver.getTitle(), "Заглавието на страницата с курсове е грешно.");
        WebElement heading = driver.findElement(By.xpath("//h1[contains(text(), 'Нашите курсове')]"));
        assertTrue(heading.isDisplayed(), "Заглавието 'Нашите курсове' не е видимо.");
        WebElement firstCourse = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".topic-content a")));
        assertTrue(firstCourse.isDisplayed(), "Няма видими курсове в списъка.");
    }

    // 2. Търсене на курсове по ключова дума (език).
    @Test
    @Order(2)
    public void testSearchCoursesByKeyword() {
        driver.get(BASE_URL + "/courses");
        waitForPreloaderToDisappear();
        WebElement searchInput = driver.findElement(By.name("searchLanguage"));
        searchInput.sendKeys("Английски");
        WebElement searchButton = driver.findElement(By.xpath("//form[@id='coursesSearchForm']//button[@type='submit']"));
        searchButton.click();
        waitForPreloaderToDisappear();
        WebElement courseTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3/a[contains(text(), 'Английски')]")));
        assertTrue(courseTitle.getText().contains("Английски"), "Резултатите от търсенето не съдържат 'Английски'.");
    }

    // 3. Преглед на детайли за конкретен курс (като анонимен потребител).
    @Test
    @Order(3)
    public void testViewSpecificCourseDetails() {
        driver.get(BASE_URL + "/courses");
        waitForPreloaderToDisappear();
        WebElement courseLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'courses-area')]//div[@class='topic-content']//a")));
        String courseName = courseLink.getText();
        courseLink.click();
        waitForPreloaderToDisappear();
        wait.until(ExpectedConditions.urlContains("/courses/view/"));
        WebElement courseTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        assertEquals(courseName, courseTitle.getText(), "Името на курса в детайлната страница не съвпада.");
        WebElement signUpButton = driver.findElement(By.xpath("//a[contains(@class, 'btn') and contains(text(), 'Запиши се')]"));
        assertTrue(signUpButton.isDisplayed(), "Бутонът 'Запиши се' не е наличен.");
    }

    // 4. Записване за курс с валидни данни (вкл. ЕГН и съгласие).
    @Test
    @Order(4)
    public void testEnrollInCourseWithValidData() {
        login();
        driver.get(BASE_URL + "/courses");
        waitForPreloaderToDisappear();
        WebElement courseLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3/a[contains(text(), '" + enrolledCourseName + "')]")));
        courseLink.click();
        wait.until(ExpectedConditions.urlContains("/view/"));
        WebElement signUpButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Запиши се')]")));
        signUpButton.click();
        wait.until(ExpectedConditions.urlContains("/signup/"));
        waitForPreloaderToDisappear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupForm")));

        driver.findElement(By.id("pin")).sendKeys("0000000000");
        driver.findElement(By.id("bulgarianCitizen")).click();
        driver.findElement(By.id("acceptPolicy")).click();
        WebElement submitButton = driver.findElement(By.id("signupButton"));
        assertTrue(submitButton.isEnabled(), "Бутонът за записване трябва да е активен.");
        submitButton.click();

        // Курсът е платен, затова очакваме пренасочване към страницата за успешна заявка
        wait.until(ExpectedConditions.urlContains("/courses/signedup/successfully"));
        WebElement successHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Успешна заявка!')]")));
        assertTrue(successHeader.isDisplayed(), "Неуспешно пренасочване към страницата за потвърждение на заявка.");
    }

    // 5. Опит за записване без съгласие – очаквана грешка.
    @Test
    @Order(5)
    public void testEnrollInCourseWithoutConsentShouldFail() {
        login();
        driver.get(BASE_URL + "/courses/signup/3"); // Приемаме, че курс с ID=3 е "Испански език"
        waitForPreloaderToDisappear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupForm")));

        driver.findElement(By.id("pin")).sendKeys("0000000001");
        driver.findElement(By.id("bulgarianCitizen")).click();
        // Пропускаме съгласието
        WebElement signupButton = driver.findElement(By.id("signupButton"));
        assertFalse(signupButton.isEnabled(), "Бутонът за записване е активен без поставено съгласие.");
    }

    // 6. Преглед на списъка с активни курсове в потребителския профил.
    @Test
    @Order(6)
    public void testViewActiveCoursesInUserProfile() {
        login();
        driver.get(BASE_URL + "/profile");
        waitForPreloaderToDisappear();
        WebElement activeCoursesSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("my-courses")));
        WebElement enrolledCourseLink = activeCoursesSection.findElement(By.xpath(".//a[contains(text(), '" + enrolledCourseName + "')]"));
        assertTrue(enrolledCourseLink.isDisplayed(), "Записаният курс не се показва в профила.");
    }

    // 7. Отваряне на лекция в курс.
    @Test
    @Order(7)
    public void testOpenLectureInCourse() {
        login();
        driver.get(BASE_URL + "/profile");
        waitForPreloaderToDisappear();
        WebElement courseLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='my-courses']//a[contains(text(), '" + enrolledCourseName + "')]")));
        courseLink.click();
        wait.until(ExpectedConditions.urlContains("/course/details/"));
        waitForPreloaderToDisappear();

        // Кликване върху първата налична лекция
        WebElement firstLectureLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ol/li/a")));
        String lectureName = firstLectureLink.getText();
        firstLectureLink.click();

        wait.until(ExpectedConditions.urlContains("/lections/view/"));
        waitForPreloaderToDisappear();
        WebElement lectionTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@data-animation='bounceIn']")));
        assertTrue(lectionTitle.getText().contains(lectureName.split(":")[1].trim()), "Заглавието на страницата на лекцията е грешно.");
    }

    // 8. Изпълнение на упражнение с верни и грешни отговори – проверка на обратната връзка.
    @Test
    @Order(8)
    public void testCompleteExerciseAndVerifyFeedback() {
        // Този тест продължава от предходния - вече сме на страницата на лекцията
        if (!driver.getCurrentUrl().contains("/lections/view/")) {
            testOpenLectureInCourse(); // Изпълняваме го, за да навигираме до правилната страница
        }

        // Намиране на всички въпроси
        List<WebElement> questions = driver.findElements(By.xpath("//form[@id='quizForm']/div[div]"));
        assertFalse(questions.isEmpty(), "На страницата няма упражнения за решаване.");

        // Отговаряме на всички въпроси (независимо дали правилно или грешно)
        for (WebElement question : questions) {
            // Проверка за въпрос с избор
            List<WebElement> radioButtons = question.findElements(By.tagName("input"));
            if (radioButtons.get(0).getAttribute("type").equals("radio")) {
                radioButtons.get(0).click(); // Избираме първия възможен отговор
            } else { // Въпрос с писане
                radioButtons.get(0).sendKeys("Test answer");
            }
        }

        // Натискаме бутона за проверка
        driver.findElement(By.xpath("//form[@id='quizForm']/button")).click();

        // Проверяваме за появата на съобщение за резултат
        WebElement alertBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customAlert")));
        assertTrue(alertBox.isDisplayed(), "Не се показва съобщение с резултата от упражнението.");
        WebElement resultText = driver.findElement(By.id("resultText"));
        assertTrue(resultText.getText().contains("Вашият резултат е:"), "Съобщението не съдържа очаквания текст за резултат.");
    }

    // 9. Взаимодействие с виртуалния асистент „Севи“ по време на урок.
    @Test
    @Order(9)
    public void testInteractWithVirtualAssistant() {
        if (!driver.getCurrentUrl().contains("/lections/view/")) {
            testOpenLectureInCourse();
        }

        WebElement helpLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[contains(text(), 'Извикай Севи на помощ!')]")));
        // Взимаме ID-то на упражнението от onclick атрибута
        String onclickAttr = helpLink.findElement(By.xpath("..")).getAttribute("onclick");
        String exerciseId = onclickAttr.replaceAll("[^0-9]", "");

        helpLink.click();

        WebElement responseBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("response_" + exerciseId)));
        assertTrue(responseBox.isDisplayed(), "Прозорецът за помощ от Севи не се появи.");

        // Изчакваме отговора да се зареди
        WebElement seviResponseText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#response_" + exerciseId + " .sevi-response-text")));
        // Изчакваме текстът да не е празен
        wait.until(driver -> !seviResponseText.getText().trim().isEmpty());
        assertFalse(seviResponseText.getText().trim().isEmpty(), "Севи не върна отговор.");

        // Затваряме прозореца
        WebElement closeButton = responseBox.findElement(By.className("close-btn"));
        closeButton.click();

        wait.until(ExpectedConditions.invisibilityOf(responseBox));
        assertFalse(responseBox.isDisplayed(), "Прозорецът за помощ от Севи не се затвори.");
    }

    // 10. Проверка дали финалният изпит е заключен преди крайната дата на курса.
    @Test
    @Order(10)
    public void testFinalExamIsLocked() {
        login();
        // Навигираме до страницата с детайли на курса, за който сме записани
        driver.get(BASE_URL + "/profile");
        waitForPreloaderToDisappear();
        WebElement courseLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='my-courses']//a[contains(text(), '" + enrolledCourseName + "')]")));
        courseLink.click();
        wait.until(ExpectedConditions.urlContains("/course/details/"));

        // Проверяваме, че секцията за финален изпит съществува
        WebElement finalExamHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Финален изпит']")));
        assertTrue(finalExamHeading.isDisplayed(), "Липсва секция 'Финален изпит'.");

        // Проверяваме, че линкът към изпита НЕ е наличен (тъй като датата на курса не е настъпила)
        List<WebElement> examLinks = driver.findElements(By.xpath("//a[contains(@href, '/final-exams/')]"));
        assertTrue(examLinks.isEmpty(), "Линкът към финалния изпит е достъпен преди крайната дата на курса.");

        // Проверяваме, че е налично информационното съобщение
        WebElement examInfoText = driver.findElement(By.xpath("//p[contains(text(), 'Финален изпит за курс:')]"));
        assertTrue(examInfoText.isDisplayed(), "Липсва информационен текст за финалния изпит.");
    }
    /**
     * Цялостен E2E тест, който симулира пълния жизнен цикъл:
     * 1. Потребител се записва за курс.
     * 2. Администратор одобрява заявката.
     * 3. Потребителят достъпва съдържанието на курса.
     * Този тест адаптира и заменя предоставения сценарий с модерни практики.
     */
    @Test
    @Order(11) // Изпълнява се след всички останали тестове
    public void testFullEnrollmentToLectureLifecycle() {
        // --- ЧАСТ 1: Потребителят се записва за курс ---

        // 1.1. Вход в системата като потребител
        driver.get(BASE_URL + "/login");
        waitForPreloaderToDisappear();
        driver.findElement(By.id("email")).sendKeys(USER_EMAIL);
        driver.findElement(By.id("password")).sendKeys(USER_PASSWORD);
        driver.findElement(By.xpath("//button[contains(text(), 'Влез в профила')]")).click();
        wait.until(ExpectedConditions.urlContains("/profile"));

        // 1.2. Навигиране до курсовете и избор на курс, за който не е записан (Английски)
        driver.get(BASE_URL + "/courses");
        String courseToEnroll = "Английски език";
        WebElement courseLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3/a[contains(text(), '" + courseToEnroll + "')]")));
        courseLink.click();

        // 1.3. Попълване на формата за записване
        wait.until(ExpectedConditions.urlContains("/view/"));
        WebElement signUpButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Запиши се')]")));
        signUpButton.click();

        wait.until(ExpectedConditions.urlContains("/signup/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupForm")));

        driver.findElement(By.id("pin")).sendKeys("1234567890"); // Примерен ЕГН
        driver.findElement(By.id("bulgarianCitizen")).click();
        driver.findElement(By.id("acceptPolicy")).click();
        driver.findElement(By.id("signupButton")).click();

        // 1.4. Проверка за успешна заявка и изход
        wait.until(ExpectedConditions.urlContains("/courses/signedup/successfully"));
        assertTrue(driver.findElement(By.xpath("//h3[contains(text(), 'Успешна заявка!')]")).isDisplayed(), "Страницата за успешна заявка не се зареди.");
        driver.findElement(By.id("logout-button")).click();
        wait.until(ExpectedConditions.urlContains("/login?logout"));

        // --- ЧАСТ 2: Администраторът одобрява заявката ---

        // 2.1. Вход като администратор
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("email")).sendKeys("langforu.softdev@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.xpath("//button[contains(text(), 'Влез в профила')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));

        // 2.2. Одобряване на заявката
        driver.get(BASE_URL + "/admin/unconfirmedUsers");
        String expectedCourseNameInAdmin = "Безплатен курс по Английски език A1"; // Пълното име, както е в админ панела
        String rowXPath = String.format("//td[contains(., \"%s\")]/parent::tr[.//td[contains(., 'Sema')]]", expectedCourseNameInAdmin);
        WebElement requestRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));

        WebElement confirmButton = requestRow.findElement(By.xpath(".//button[contains(text(), 'Потвърди плащане')]"));
        confirmButton.click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        wait.until(ExpectedConditions.stalenessOf(requestRow));

        // 2.3. Проверка дали заявката е изчезнала и изход
        List<WebElement> remainingRows = driver.findElements(By.xpath(rowXPath));
        assertTrue(remainingRows.isEmpty(), "Заявката не е премахната от списъка след одобрение.");
        driver.findElement(By.id("logout-button")).click();
        wait.until(ExpectedConditions.urlContains("/login?logout"));

        // --- ЧАСТ 3: Потребителят достъпва одобрения курс ---

        // 3.1. Вход отново като потребител
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("email")).sendKeys(USER_EMAIL);
        driver.findElement(By.id("password")).sendKeys(USER_PASSWORD);
        driver.findElement(By.xpath("//button[contains(text(), 'Влез в профила')]")).click();
        wait.until(ExpectedConditions.urlContains("/profile"));

        // 3.2. Намиране на одобрения курс в профила и отваряне
        WebElement approvedCourseLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='my-courses']//a[contains(text(),'" + courseToEnroll + "')]")));
        approvedCourseLink.click();

        // 3.3. Отваряне на първата лекция
        wait.until(ExpectedConditions.urlContains("/course/details/"));
        WebElement firstLectureLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ol/li/a[contains(text(), 'Урок 1')]")));
        firstLectureLink.click();

        // 3.4. Проверка, че сме на страницата на лекцията
        wait.until(ExpectedConditions.urlContains("/lections/view/"));
        WebElement lectionTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@data-animation='bounceIn']")));
        assertTrue(lectionTitle.getText().contains("Hello and Welcome!"), "Заглавието на лекцията е грешно.");

        // 3.5. Взаимодействие с упражнението
        // Отговор на въпрос с избор
        WebElement choiceQuestion = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(text(), 'What does `Hello` mean?')]/following-sibling::div//label")));
        choiceQuestion.click();

        // Отговор на въпрос с въвеждане
        WebElement textQuestionInput = driver.findElement(By.name("question_2"));
        textQuestionInput.sendKeys("Apple");

        // Проверка
        driver.findElement(By.xpath("//form[@id='quizForm']/button")).click();
        WebElement alertBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customAlert")));
        assertTrue(alertBox.isDisplayed(), "Съобщението за резултата от теста не се появи.");

        // Затваряне на съобщението
        WebElement closeBtn = alertBox.findElement(By.className("closebtn"));
        closeBtn.click();
        wait.until(ExpectedConditions.invisibilityOf(alertBox));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}