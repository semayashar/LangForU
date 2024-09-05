package LangForU_Development.LangForU.registration;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.appuser.AppUserRole;
import LangForU_Development.LangForU.appuser.AppUserService;
import LangForU_Development.LangForU.email.EmailSender;
import LangForU_Development.LangForU.email.EmailTemplateService;
import LangForU_Development.LangForU.registration.token.ConfirmationToken;
import LangForU_Development.LangForU.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private AppUserService appUserService;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private EmailSender emailSender;

    @Mock
    private EmailTemplateService emailTemplateService;

    @InjectMocks
    private RegistrationService registrationService;

    @Captor
    private ArgumentCaptor<AppUser> appUserArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegistrationRequest request = new RegistrationRequest(
                "test@example.com",
                "password",
                "Test User",
                LocalDate.now().minusYears(20),
                "Male",
                "123 Test St",
                "1234567890"
        );

        when(emailValidator.test(request.getEmail())).thenReturn(true);
        when(appUserService.signUpUser(any(AppUser.class))).thenReturn("test-token");
        when(emailTemplateService.buildEmail_Registration(anyString(), anyString())).thenReturn("Email Content");

        String token = registrationService.register(request);

        assertEquals("test-token", token);

        verify(appUserService).signUpUser(appUserArgumentCaptor.capture());
        AppUser capturedUser = appUserArgumentCaptor.getValue();
        assertEquals(request.getEmail(), capturedUser.getEmail());
        assertEquals(request.getName(), capturedUser.getName());

        verify(emailSender).send(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        assertEquals(request.getEmail(), stringArgumentCaptor.getAllValues().get(0));
    }


    @Test
    void testConfirmToken_TokenNotFound() {
        String token = "invalid-token";

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("token not found", exception.getMessage());
    }

    @Test
    void testConfirmToken_TokenAlreadyConfirmed() {
        String token = "test-token";
        AppUser appUser = new AppUser();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationToken.setConfirmedAt(LocalDateTime.now());

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("email already confirmed", exception.getMessage());
    }

    @Test
    void testConfirmToken_TokenExpired() {
        String token = "test-token";
        AppUser appUser = new AppUser();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(30), // Created 30 minutes ago
                LocalDateTime.now().minusMinutes(15), // Expired 15 minutes ago
                appUser
        );

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("token expired", exception.getMessage());
    }

    @Test
    void testConfirmToken_NullAppUser() {
        String token = "test-token";
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                null // Null AppUser
        );

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("token not found", exception.getMessage());
    }

    @Test
    void testConfirmToken_NullTokenInConfirmationToken() {
        String token = "test-token";
        AppUser appUser = new AppUser();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                null, // Null token
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("token not found", exception.getMessage());
    }

    @Test
    void testConfirmToken_NullEmailInAppUser() {
        String token = "test-token";
        AppUser appUser = new AppUser();
        appUser.setEmail(null); // Null email
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("token not found", exception.getMessage());
    }

}