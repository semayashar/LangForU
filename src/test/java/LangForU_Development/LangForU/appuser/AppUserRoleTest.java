package LangForU_Development.LangForU.appuser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserRoleTest {

    @Test
    void testUserRole() {
        assertEquals("USER", AppUserRole.USER.name());
    }

    @Test
    void testAdminRole() {
        assertEquals("ADMIN", AppUserRole.ADMIN.name());
    }

    @Test
    void testAdminRoleOrdinal() {
        assertEquals(0, AppUserRole.ADMIN.ordinal()); // ADMIN should be 0
    }

    @Test
    void testUserRoleOrdinal() {
        assertEquals(1, AppUserRole.USER.ordinal()); // USER should be 1
    }

    @Test
    void testAdminRoleToString() {
        assertEquals("ADMIN", AppUserRole.ADMIN.toString());
    }

    @Test
    void testUserRoleValues() {
        AppUserRole[] roles = AppUserRole.values();
        assertEquals(2, roles.length);
        assertEquals(AppUserRole.ADMIN, roles[0]);
        assertEquals(AppUserRole.USER, roles[1]);
    }

    @Test
    void testUserRoleValueOf() {
        assertEquals(AppUserRole.USER, AppUserRole.valueOf("USER"));
    }

    @Test
    void testAdminRoleValueOf() {
        assertEquals(AppUserRole.ADMIN, AppUserRole.valueOf("ADMIN"));
    }

    @Test
    void testInvalidRoleValueOf() {
        assertThrows(IllegalArgumentException.class, () -> AppUserRole.valueOf("INVALID"));
    }
}