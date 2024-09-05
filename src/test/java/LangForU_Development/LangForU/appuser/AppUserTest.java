package LangForU_Development.LangForU.appuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    private AppUser appUser;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void testGetEmail() {
        assertEquals("test@example.com", appUser.getEmail());
    }

    @Test
    void testGetPassword() {
        assertEquals("password123", appUser.getPassword());
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", appUser.getName());
    }

    @Test
    void testGetDateOfBirth() {
        assertEquals(LocalDate.of(1990, 1, 1), appUser.getDateOfBirth());
    }

    @Test
    void testGetGender() {
        assertEquals("Male", appUser.getGender());
    }

    @Test
    void testGetAddress() {
        assertEquals("123 Main St", appUser.getAddress());
    }

    @Test
    void testGetPhoneNumber() {
        assertEquals("+1234567890", appUser.getPhoneNumber());
    }

    @Test
    void testGetAppUserRole() {
        assertEquals(AppUserRole.USER, appUser.getAppUserRole());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(appUser.isAccountNonLocked());
    }

    @Test
    void testIsEnabled() {
        assertFalse(appUser.isEnabled());
    }
}