package LangForU_DevTeam.LangForU.appuser;

import LangForU_DevTeam.LangForU.email.EmailService;
import LangForU_DevTeam.LangForU.email.EmailTemplateService;
import LangForU_DevTeam.LangForU.registration.admin.AdminConfirmationToken;
import LangForU_DevTeam.LangForU.registration.admin.AdminConfirmationTokenService;
import LangForU_DevTeam.LangForU.registration.token.ConfirmationTokenService;
import LangForU_DevTeam.LangForU.singUpForCourse.UserCourseRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private AdminConfirmationTokenService adminConfirmationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private EmailTemplateService emailTemplateService;
    @Mock
    private UserCourseRequestService userCourseRequestService;

    @InjectMocks
    private AppUserService appUserService;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser(
                "test@example.com",
                "password123",
                "Тест Потребител",
                LocalDate.of(2000, 1, 1),
                "Male",
                "Test Address",
                AppUserRole.USER
        );
        testUser.setId(1L);
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        when(appUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        UserDetails userDetails = appUserService.loadUserByUsername("test@example.com");
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_whenUserNotFound_shouldThrowUsernameNotFoundException() {
        when(appUserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    void deleteUserAndRelatedRecords_shouldDeleteUserSuccessfully() {
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(emailTemplateService.buildEmail_AccountDeletion(anyString(), anyString())).thenReturn("Email body");

        appUserService.deleteUserAndRelatedRecords(1L);

        verify(userCourseRequestService).deleteRequestsByUserId(1L);
        verify(confirmationTokenService).deleteTokensByUserId(1L);
        verify(appUserRepository).deleteById(1L);
        verify(emailService).send(eq("test@example.com"), anyString());
    }

    @Test
    void enableAppUser_shouldThrowException_whenRepositoryReturnsZero() {
        when(appUserRepository.enableAppUser("test@example.com")).thenReturn(0);
        assertThrows(RuntimeException.class, () -> appUserService.enableAppUser("test@example.com"));
    }

    @Test
    void disableUserById_shouldDisableUser_whenIdIsValid() {
        when(appUserRepository.existsById(1L)).thenReturn(true);
        when(appUserRepository.disableAppUser(1L)).thenReturn(1);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(emailTemplateService.buildEmail_AccountDisable(anyString(), anyString())).thenReturn("Email body");

        appUserService.disableUserById(1L);

        verify(appUserRepository).disableAppUser(1L);
        verify(emailService).send(eq("test@example.com"), anyString());
    }

    @Test
    void sendAdminRequest_shouldReturnToken_whenUserIsValid() {
        doNothing().when(adminConfirmationTokenService).saveAdminConfirmationToken(any(AdminConfirmationToken.class));
        when(emailTemplateService.buildEmail_AdminActivation(anyString(), anyString())).thenReturn("Някакво съдържание на имейл");
        String token = appUserService.sendAdminRequest(testUser);
        assertNotNull(token);
        verify(adminConfirmationTokenService).saveAdminConfirmationToken(any(AdminConfirmationToken.class));
        verify(emailService).send(eq("test@example.com"), anyString());
    }
}