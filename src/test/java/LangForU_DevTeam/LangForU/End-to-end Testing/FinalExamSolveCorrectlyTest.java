import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * End-to-end тест, който решава финален изпит с верните отговори.
 */
public class FinalExamSolveCorrectlyTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private Map<String, String> correctAnswers;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Инициализация на картата с верните отговори
        correctAnswers = new HashMap<>();
        // Отворени въпроси
        correctAnswers.put("Преведете на английски: 'Аз се казвам Мария и уча английски език.'", "My name is Maria and I study English.");
        correctAnswers.put("Съставете изречение с тези думи: 'I / go / to / school / every / day'", "I go to school every day.");
        correctAnswers.put("Напишете кратко представяне на себе си на английски.", "My name is Test User. I am 25 years old. I am from Bulgaria.");
        correctAnswers.put("Опишете вашия дом на английски език.", "My home is small. It has two bedrooms and a kitchen.");
        correctAnswers.put("Разкажете за вашето хоби на английски.", "I like reading books and playing football.");
        correctAnswers.put("Как прекарвате свободното си време? Отговорете на английски.", "I spend my free time watching movies or walking in the park.");
        correctAnswers.put("Опишете ваш приятел на английски.", "My friend is kind. He is tall and has black hair.");
        correctAnswers.put("Какво обичате да ядете за закуска? Напишете на английски.", "I like to eat eggs and toast for breakfast.");
        correctAnswers.put("Опишете вашата училищна стая на английски.", "There are 20 desks and a whiteboard.");
        correctAnswers.put("Какъв е вашият любим ден от седмицата и защо? Отговорете на английски.", "My favorite day is Saturday because I can relax.");
        correctAnswers.put("Опишете времето днес на английски.", "It is sunny and warm today.");
        correctAnswers.put("Какво правите след училище? Отговорете на английски.", "I do my homework and then play outside.");
        correctAnswers.put("Разкажете за пътуване, което сте правили. Напишете на английски.", "I went to the mountains last summer.");
        correctAnswers.put("Какви животни харесвате? Отговорете на английски.", "I like cats and dogs.");
        correctAnswers.put("Какъв е вашият любим предмет в училище и защо? Напишете на английски.", "My favorite subject is English because it is fun.");
        // Затворени въпроси
        correctAnswers.put("Попълнете пропуските: 'My name _____ John. I _____ 25 years old.'", "is, am");
        correctAnswers.put("Изберете правилния отговор: 'What is your name?'", "My name is John");
        correctAnswers.put("Попълнете: 'I _____ a student.'", "am");
        correctAnswers.put("Изберете: 'She _____ a teacher.'", "is");
        correctAnswers.put("Кое е правилно: 'I ___ coffee.'", "drink");
        correctAnswers.put("Попълнете: 'He _____ to school every day.'", "goes");
        correctAnswers.put("Изберете правилното местоимение: '_____ is my sister.'", "She");
        correctAnswers.put("Попълнете: 'We _____ happy today.'", "are");
        correctAnswers.put("Изберете правилния отговор: 'How old are you?'", "I am 20");
        correctAnswers.put("Попълнете: 'They _____ at home now.'", "are");
        correctAnswers.put("Кое е правилно: 'You _____ late.'", "are");
        correctAnswers.put("Попълнете: 'The cat _____ on the sofa.'", "is");
        correctAnswers.put("Изберете: 'It is _____ apple.'", "an");
        correctAnswers.put("Попълнете: 'This is _____ book.'", "my");
        // ---- КОРЕКЦИЯТА Е ТУК ----
        correctAnswers.put("Изберете правилния отговор: 'I _____ breakfast at 8.''", "eat");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void solveAndSubmitFinalExamCorrectlyTest() {
        // 1. Вход в системата с конкретни данни
        driver.get("http://localhost:8080/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("semayasharova@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Sema1234");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("http://localhost:8080/login")));

        // 2. Навигация към страницата на изпита
        driver.get("http://localhost:8080/final-exams/1");
        WebElement examForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("examForm")));

        // 3. Попълване на въпросите с верните отговори
        List<WebElement> questionContainers = examForm.findElements(By.xpath("./div[.//strong]"));

        for (WebElement container : questionContainers) {
            String questionText = container.findElement(By.tagName("strong")).getText();
            String correctAnswer = correctAnswers.get(questionText);

            if (correctAnswer == null) {
                fail("Няма намерен отговор за въпроса: '" + questionText + "'. Моля, проверете картата с отговори.");
                continue;
            }

            // Проверка за отворен въпрос
            List<WebElement> openQuestions = container.findElements(By.cssSelector("input[type='text']"));
            if (!openQuestions.isEmpty()) {
                openQuestions.get(0).sendKeys(correctAnswer);
                continue;
            }

            // Проверка за затворен въпрос
            List<WebElement> radioButtons = container.findElements(By.cssSelector("input[type='radio']"));
            if (!radioButtons.isEmpty()) {
                try {
                    WebElement correctRadio = container.findElement(By.cssSelector("input[value='" + correctAnswer + "']"));
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", correctRadio);
                } catch (Exception e) {
                    fail("Не може да бъде намерен радио бутон с отговор: '" + correctAnswer + "' за въпроса: '" + questionText + "'");
                }
            }
        }

        // 4. Попълване на есето
        WebElement essayTextarea = examForm.findElement(By.name("essayAnswer"));
        essayTextarea.sendKeys("On a typical school day, I wake up at 7 AM. After I have breakfast, I go to school. " +
                "My first class starts at 8 AM. I enjoy studying many subjects, but my favorite is English. " +
                "During the break, I usually talk with my friends or read a book. After school, I return home, " +
                "I do my homework, and then I have some free time to relax and play outside.");


        // 5. Предаване на изпита
        examForm.findElement(By.cssSelector("button[type='submit']")).click();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Тестът беше прекъснат по време на изчакването.");
        }

        // 6. Проверка (Assertion)
        WebElement resultsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Финален изпит - резултати')]")));
        assertEquals("Финален изпит - резултати", resultsTitle.getText(), "Страницата с резултати не се зареди успешно след предаване на изпита.");

        WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p/strong[text()='Статус:']/following-sibling::span")));
        assertEquals("Преминат ✅", statusElement.getText(), "Очакваше се изпитът да бъде преминат успешно.");
    }
}