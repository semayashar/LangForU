package LangForU_Development.LangForU.appuser;

import LangForU_Development.LangForU.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @InjectMocks
    private AppUserService appUserService;

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );

        when(bCryptPasswordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
    }

    @Test
    void testSignUpUser() {
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        String token = appUserService.signUpUser(appUser);
        assertNotNull(token);
        verify(appUserRepository, times(1)).save(appUser);
    }

    @Test
    void testSignUpUserTokenCreation() {
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        String token = appUserService.signUpUser(appUser);
        assertNotNull(token);
        verify(confirmationTokenService, times(1)).saveConfirmationToken(any());
    }

    @Test
    void testSignUpUserWithDifferentEmail() {
        AppUser differentUser = new AppUser(
                "different@example.com",
                "password123",
                "Jane Doe",
                LocalDate.of(1990, 1, 1),
                "Female",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        when(appUserRepository.save(any(AppUser.class))).thenReturn(differentUser);
        String token = appUserService.signUpUser(differentUser);
        assertNotNull(token);
        verify(appUserRepository, times(1)).save(differentUser);
    }
}
