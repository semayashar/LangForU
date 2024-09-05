package LangForU_Development.LangForU.registration.token;

import LangForU_Development.LangForU.appuser.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmationTokenTest {

    private ConfirmationToken confirmationToken;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        confirmationToken = new ConfirmationToken(
                "test-token",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
    }

    @Test
    void testTokenCreation() {
        assertNotNull(confirmationToken);
        assertEquals("test-token", confirmationToken.getToken());
        assertNotNull(confirmationToken.getCreatedAt());
        assertNotNull(confirmationToken.getExpiresAt());
        assertEquals(appUser, confirmationToken.getAppUser());
    }

    @Test
    void testTokenExpiration() {
        LocalDateTime now = LocalDateTime.now();
        confirmationToken.setExpiresAt(now);
        assertEquals(now, confirmationToken.getExpiresAt());
    }

    @Test
    void testTokenConfirmation() {
        LocalDateTime now = LocalDateTime.now();
        confirmationToken.setConfirmedAt(now);
        assertEquals(now, confirmationToken.getConfirmedAt());
    }

    @Test
    void testTokenValidity() {
        assertTrue(confirmationToken.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Test
    void testTokenHasExpired() {
        confirmationToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        assertTrue(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()));
    }

    @Test
    void testTokenIsConfirmed() {
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        assertNotNull(confirmationToken.getConfirmedAt());
    }
}