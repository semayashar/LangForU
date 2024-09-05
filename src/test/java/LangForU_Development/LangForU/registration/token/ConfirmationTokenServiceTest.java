package LangForU_Development.LangForU.registration.token;

import LangForU_Development.LangForU.appuser.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfirmationTokenServiceTest {

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @InjectMocks
    private ConfirmationTokenService confirmationTokenService;

    private ConfirmationToken confirmationToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        confirmationToken = new ConfirmationToken(
                "test-token",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                new AppUser()
        );
    }

    @Test
    void testSaveConfirmationToken() {
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        verify(confirmationTokenRepository, times(1)).save(confirmationToken);
    }

    @Test
    void testGetToken() {
        when(confirmationTokenRepository.findByToken("test-token")).thenReturn(Optional.of(confirmationToken));
        Optional<ConfirmationToken> token = confirmationTokenService.getToken("test-token");
        assertTrue(token.isPresent());
        assertEquals("test-token", token.get().getToken());
    }

}