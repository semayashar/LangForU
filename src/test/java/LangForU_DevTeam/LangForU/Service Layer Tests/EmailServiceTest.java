package LangForU_DevTeam.LangForU.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailTemplateService emailTemplateService;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void send_whenSuccessful_shouldCallMailSenderSend() {
        String to = "test@example.com";
        String emailBody = "Hello, this is a test email.";
        doNothing().when(mailSender).send(any(MimeMessage.class));
        emailService.send(to, emailBody);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void send_whenMailSenderFails_shouldThrowMailSendException() {
        String to = "test@example.com";
        String emailBody = "This email will fail.";

        // Симулираме, че JavaMailSender хвърля грешка при изпращане
        doThrow(new MailSendException("Simulated failure")).when(mailSender).send(any(MimeMessage.class));

        // КОРЕКЦИЯ:
        // Проверяваме дали се хвърля точно MailSendException, тъй като catch блокът не се задейства
        assertThrows(MailSendException.class, () -> {
            emailService.send(to, emailBody);
        });
    }
}