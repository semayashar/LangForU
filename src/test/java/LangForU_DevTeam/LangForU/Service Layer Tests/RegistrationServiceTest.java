package LangForU_DevTeam.LangForU.registration;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.email.EmailService;
import LangForU_DevTeam.LangForU.email.EmailTemplateService;
import LangForU_DevTeam.LangForU.registration.token.ConfirmationToken;
import LangForU_DevTeam.LangForU.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private AppUserService appUserService;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private EmailTemplateService emailTemplateService;

    @InjectMocks
    private RegistrationService registrationService;

    private RegistrationRequest registrationRequest;
    private AppUser testUser;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationRequest.setName("Тест Потребител");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setDateOfBirth(LocalDate.of(2000, 1, 1));
        registrationRequest.setGender("Male");
        registrationRequest.setAddress("Test Address");

        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setEmail(registrationRequest.getEmail());
    }

    @Test
    void register_whenRequestIsValid_shouldCreateUserAndSendEmail() {
        String testToken = "test-token-123";
        String emailBody = "This is a test email body.";

        when(emailValidator.test(registrationRequest.getEmail())).thenReturn(true);
        when(appUserService.signUpUser(any(AppUser.class))).thenReturn(testToken);
        when(emailTemplateService.buildEmail_Registration(anyString(), anyString())).thenReturn(emailBody);
        doNothing().when(emailService).send(anyString(), anyString());

        String resultToken = registrationService.register(registrationRequest);

        assertNotNull(resultToken);
        assertEquals(testToken, resultToken);

        verify(emailValidator).test(registrationRequest.getEmail());
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserService).signUpUser(userCaptor.capture());
        AppUser capturedUser = userCaptor.getValue();
        assertEquals(registrationRequest.getName(), capturedUser.getName());
        assertEquals(registrationRequest.getEmail(), capturedUser.getEmail());

        verify(emailService).send(registrationRequest.getEmail(), emailBody);
    }

    @Test
    void register_whenEmailIsInvalid_shouldThrowIllegalStateException() {
        when(emailValidator.test(registrationRequest.getEmail())).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> registrationService.register(registrationRequest));

        assertEquals("Невалиден имейл адрес", exception.getMessage());

        verify(appUserService, never()).signUpUser(any());
        verify(emailService, never()).send(anyString(), anyString());
    }

    @Test
    void confirmToken_whenTokenIsValid_shouldConfirmAndEnableUser() {
        String validToken = "valid-token";
        ConfirmationToken token = new ConfirmationToken(validToken, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), testUser);
        when(confirmationTokenService.getToken(validToken)).thenReturn(Optional.of(token));
        when(appUserService.enableAppUser(testUser.getEmail())).thenReturn(1);

        String result = registrationService.confirmToken(validToken);

        assertEquals("confirmed", result);
        verify(confirmationTokenService).setConfirmedAt(validToken);
        verify(appUserService).enableAppUser(testUser.getEmail());
    }

    @Test
    void confirmToken_whenTokenNotFound_shouldThrowIllegalStateException() {
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> registrationService.confirmToken("not-found-token"));
        assertEquals("Невалиден или изтекъл токен. Моля, уверете се, че връзката не е изтекла, и опитайте отново.", exception.getMessage());
    }

    @Test
    void confirmToken_whenTokenAlreadyConfirmed_shouldThrowIllegalStateException() {
        ConfirmationToken token = new ConfirmationToken();
        token.setConfirmedAt(LocalDateTime.now().minusMinutes(5));
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.of(token));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> registrationService.confirmToken("already-confirmed-token"));
        assertEquals("Имейлът вече е потвърден", exception.getMessage());
    }

    @Test
    void confirmToken_whenTokenIsExpired_shouldThrowIllegalStateException() {
        ConfirmationToken token = new ConfirmationToken("expired-token", LocalDateTime.now().minusMinutes(20), LocalDateTime.now().minusMinutes(5), testUser);
        when(confirmationTokenService.getToken("expired-token")).thenReturn(Optional.of(token));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> registrationService.confirmToken("expired-token"));
        assertEquals("Токенът е изтекъл. Моля, опитайте отново с нов токен.", exception.getMessage());
    }
}