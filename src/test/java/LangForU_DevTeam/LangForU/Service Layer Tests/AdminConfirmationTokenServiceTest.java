package LangForU_DevTeam.LangForU.registration.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminConfirmationTokenServiceTest {

    @Mock
    private AdminConfirmationTokenRepository adminConfirmationTokenRepository;

    @InjectMocks
    private AdminConfirmationTokenService adminConfirmationTokenService;

    private AdminConfirmationToken testToken;

    @BeforeEach
    void setUp() {
        testToken = new AdminConfirmationToken(
                "test-token-string",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                null
        );
        testToken.setId(1L);
    }

    @Test
    void saveAdminConfirmationToken_whenCalled_shouldInvokeRepositorySave() {
        adminConfirmationTokenService.saveAdminConfirmationToken(testToken);
        verify(adminConfirmationTokenRepository).save(testToken);
    }

    @Test
    void getToken_whenTokenExists_shouldReturnOptionalOfToken() {
        when(adminConfirmationTokenRepository.findByToken("test-token-string")).thenReturn(Optional.of(testToken));

        Optional<AdminConfirmationToken> result = adminConfirmationTokenService.getToken("test-token-string");

        assertTrue(result.isPresent());
        assertEquals(testToken.getId(), result.get().getId());
    }

    @Test
    void getToken_whenTokenDoesNotExist_shouldReturnEmptyOptional() {
        when(adminConfirmationTokenRepository.findByToken("non-existent-token")).thenReturn(Optional.empty());

        Optional<AdminConfirmationToken> result = adminConfirmationTokenService.getToken("non-existent-token");

        assertTrue(result.isEmpty());
    }

    @Test
    void setConfirmedAt_whenCalled_shouldInvokeRepositoryUpdateAndReturnCount() {
        when(adminConfirmationTokenRepository.updateConfirmedAt(anyString(), any(LocalDateTime.class))).thenReturn(1);

        int updatedRows = adminConfirmationTokenService.setConfirmedAt("test-token-string");

        assertEquals(1, updatedRows);
        verify(adminConfirmationTokenRepository).updateConfirmedAt(eq("test-token-string"), any(LocalDateTime.class));
    }
}