package LangForU_DevTeam.LangForU.certificate;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.courses.Course;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequest;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Сервизен клас, отговорен за генерирането на PDF сертификати за участници,
 * завършили успешно курсове. Използва библиотеката iTextPDF.
 */
@Service
public class CertificateGeneratorService {

    // Логър за записване на грешки и информация.
    private static final Logger logger = LoggerFactory.getLogger(CertificateGeneratorService.class);

    //<editor-fold desc="Font Definitions">
    // Дефиниции на шрифтове, които ще се използват в целия клас.
    private static final Font TITLE_FONT;
    private static final Font NORMAL_FONT;
    private static final Font BOLD_FONT;

    /**
     * Статичен блок за инициализация.
     * Изпълнява се еднократно при зареждане на класа.
     * Зарежда персонализирани шрифтове от файлове, за да поддържа кирилица в PDF документите.
     */
    static {
        try {
            // Зареждане на стандартен и удебелен шрифт от .otf файлове.
            BaseFont freeSerif = BaseFont.createFont(
                    "src/main/resources/static/fonts/FreeSerif.otf",
                    BaseFont.IDENTITY_H, // Позволява Unicode (кирилица)
                    BaseFont.EMBEDDED // Вгражда шрифта в PDF файла
            );
            BaseFont freeSerifBold = BaseFont.createFont(
                    "src/main/resources/static/fonts/FreeSerifBold.otf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            // Инициализация на константите за шрифтове с различни размери и стилове.
            TITLE_FONT = new Font(freeSerif, 18, Font.BOLD);
            NORMAL_FONT = new Font(freeSerif, 12);
            BOLD_FONT = new Font(freeSerifBold, 12, Font.BOLD);

        } catch (Exception e) {
            logger.error("Грешка при зареждане на шрифтове", e);
            throw new RuntimeException("Грешка при зареждане на шрифтове", e);
        }
    }
    //</editor-fold>

    /**
     * Увиващ (wrapper) метод, който приема заявка за курс и генерира сертификат.
     * Извлича необходимите данни от обекта и извиква основния генериращ метод.
     * @param request Обект {@link UserCourseRequest}, съдържащ данни за потребителя и курса.
     * @param finalExamScore Резултат от финалния изпит (в момента този параметър не се използва в извикания метод).
     * @return Масив от байтове (byte[]), представляващ генерирания PDF файл, или null при грешка.
     */
    public byte[] generateCertificateAsBytes(UserCourseRequest request, int finalExamScore) {
        try {
            String fullName = request.getUser().getName();
            String egn = request.getPIN();
            String courseName = request.getCourse().getLanguage();
            LocalDate startDate = request.getCourse().getStartDate();
            LocalDate endDate = request.getCourse().getEndDate();
            int totalHours = request.getCourse().getLections().size();

            // Извикване на основния метод за генериране с извлечените данни.
            return generateCertificateAsBytes(fullName, egn, courseName, startDate, endDate, totalHours);
        } catch (Exception e) {
            logger.error("Грешка при генериране на сертификат: ", e);
            return null;
        }
    }

    /**
     * Основен метод, който създава и форматира PDF сертификат с подадените данни.
     * @param fullName Пълно име на участника.
     * @param egn ЕГН на участника.
     * @param courseName Име на курса.
     * @param startDate Начална дата на курса.
     * @param endDate Крайна дата на курса.
     * @param totalHours Обща продължителност в учебни часове.
     * @return Масив от байтове (byte[]), представляващ генерирания PDF файл.
     * @throws Exception при грешки по време на създаването на PDF.
     */
    public byte[] generateCertificateAsBytes(String fullName, String egn, String courseName,
                                             LocalDate startDate, LocalDate endDate, int totalHours) throws Exception {

        Document document = new Document(PageSize.A4, 50, 50, 60, 60); // Създаване на А4 документ с полета.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // Поток за запис на PDF в паметта.
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        // Добавяне на лого
        Image logo = Image.getInstance("src/main/resources/static/img/logo/logo.png");
        logo.scaleToFit(100, 100);
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);

        // Дефиниране на шрифтове за конкретния дизайн на сертификата
        BaseFont baseFont = BaseFont.createFont("src/main/resources/static/fonts/FreeSerif.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont boldFont = BaseFont.createFont("src/main/resources/static/fonts/FreeSerifBold.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font titleFont = new Font(boldFont, 24, Font.BOLD, new BaseColor(40, 40, 90));
        Font subFont = new Font(baseFont, 12, Font.NORMAL, new BaseColor(40, 40, 90));
        Font nameFont = new Font(boldFont, 28, Font.BOLD, new BaseColor(40, 40, 90));
        Font labelFont = new Font(baseFont, 10, Font.BOLD, BaseColor.DARK_GRAY);
        Font footerFont = new Font(baseFont, 10, Font.NORMAL, BaseColor.WHITE);

        // Добавяне на съдържание
        Paragraph title = new Paragraph("СЕРТИФИКАТ ЗА УЧАСТИЕ", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        Paragraph certText = new Paragraph("С настоящото се удостоверява, че", subFont);
        certText.setAlignment(Element.ALIGN_CENTER);
        certText.setSpacingAfter(15);
        document.add(certText);

        Paragraph name = new Paragraph(fullName, nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        name.setSpacingAfter(15);
        document.add(name);

        String courseDetails = String.format(
                "е участвал(а) активно в курса по тема \"%s\", проведен в периода от %s до %s, с обща продължителност от %d учебни часа.",
                courseName,
                startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                totalHours
        );
        Paragraph details = new Paragraph(courseDetails, subFont);
        details.setAlignment(Element.ALIGN_CENTER);
        details.setSpacingBefore(10);
        details.setSpacingAfter(25);
        document.add(details);

        Paragraph egnLine = new Paragraph("ЕГН: " + egn, labelFont);
        egnLine.setAlignment(Element.ALIGN_CENTER);
        egnLine.setSpacingAfter(30);
        document.add(egnLine);

        // Добавяне на таблица за подписи и дата в долната част
        PdfPTable footerTable = new PdfPTable(3);
        footerTable.setWidthPercentage(100);
        footerTable.setWidths(new float[]{1.5f, 1f, 1.5f});

        // Лява колона (подпис)
        PdfPCell left = new PdfPCell();
        left.setBorder(Rectangle.NO_BORDER);
        left.addElement(new Paragraph("Подпис:\nД-р Иван Иванов\nРъководител", labelFont));
        footerTable.addCell(left);

        // Централна колона
        PdfPCell center = new PdfPCell();
        center.setBorder(Rectangle.NO_BORDER);
        center.setHorizontalAlignment(Element.ALIGN_CENTER);
        center.addElement(new Paragraph("УЧАСТНИК\n2025", labelFont));
        footerTable.addCell(center);

        // Дясна колона (дата на издаване)
        PdfPCell right = new PdfPCell();
        right.setBorder(Rectangle.NO_BORDER);
        right.setHorizontalAlignment(Element.ALIGN_RIGHT);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        right.addElement(new Paragraph("Дата на издаване: " + today, labelFont));
        footerTable.addCell(right);

        document.add(footerTable);

        // Добавяне на цветен футър в най-долната част на страницата
        PdfContentByte canvas = writer.getDirectContentUnder();
        Rectangle rect = new Rectangle(0, 0, PageSize.A4.getWidth(), 50);
        rect.setBackgroundColor(new BaseColor(40, 40, 90));
        canvas.rectangle(rect);

        // Добавяне на текст върху цветния футър
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                new Phrase("© 2025 Академия за Езици. Всички права запазени.", footerFont),
                PageSize.A4.getWidth() / 2, 20, 0);

        document.close();
        return outputStream.toByteArray();
    }

    //<editor-fold desc="Unused Methods">
    // Забележка: Следващите методи са дефинирани, но не се извикват от никой друг метод в класа.
    // Вероятно представляват алтернативен или стар дизайн на сертификата.

    /**
     * Добавя съдържание към PDF документ.
     * Този метод в момента не се използва.
     */
    private void addCertificateContent(Document document, AppUser user, Course course, UserCourseRequest request, int finalExamScore) throws DocumentException {
        // ...
    }

    /**
     * Добавя детайли за потребителя в документа.
     * Този метод в момента не се използва.
     */
    private void addUserDetails(Document document, AppUser user, UserCourseRequest request) throws DocumentException {
        // ...
    }

    /**
     * Добавя детайли за курса в документа.
     * Този метод в момента не се използва.
     */
    private void addCourseDetails(Document document, Course course, int finalExamScore) throws DocumentException {
        // ...
    }

    /**

     * Преобразува точки от изпит в текстова оценка.
     * Този метод в момента не се използва.
     * @param points Точки от изпита.
     * @return Текстова оценка (напр. "Отличен (6)").
     */
    private String getGradeFromPoints(int points) {
        if (points >= 5) return "Отличен (6)";
        if (points == 4) return "Много добър (5)";
        return "Добър (4)";
    }
    //</editor-fold>
}