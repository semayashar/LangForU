package LangForU_DevTeam.LangForU.appuser;

import LangForU_DevTeam.LangForU.email.EmailService;
import LangForU_DevTeam.LangForU.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Всеки тест се връща назад (rollback) след изпълнение
class AppUserServiceIntegrationTest {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private EmailService emailService;

    @Test
    void testSaveAndFindByEmail() {
        AppUser user = new AppUser(
                "test@example.com",
                "password123",
                "Test User",
                LocalDate.of(1990, 1, 1),
                "male",
                "Sofia",
                AppUserRole.USER,
                true
        );

        appUserService.save(user);

        AppUser found = appUserService.findByEmail("test@example.com");
        assertNotNull(found);
        assertEquals("Test User", found.getName());
    }

    @Test
    @Transactional
    void testEnableAndDisableUser() {
        AppUser user = new AppUser(
                "enabletest@example.com",
                "password123",
                "Enable User",
                LocalDate.of(1990, 1, 1),
                "female",
                "Plovdiv",
                AppUserRole.USER,
                false
        );
        user = appUserRepository.saveAndFlush(user);

        appUserService.enableUserById(user.getId());

        AppUser updated = appUserRepository.findById(user.getId()).orElseThrow();
        Assertions.assertTrue(updated.isEnabled());
    }

    @Test
    void testGetAllUsersByRole() {
        AppUser admin = new AppUser(
                "admin@example.com",
                "password123",
                "Admin User",
                LocalDate.of(1985, 5, 5),
                "male",
                "Varna",
                AppUserRole.ADMIN,
                true
        );
        appUserService.save(admin);

        List<AppUser> admins = appUserService.getAllUsersByRole(AppUserRole.ADMIN);
        assertFalse(admins.isEmpty());
        assertEquals(AppUserRole.ADMIN, admins.get(0).getAppUserRole());
    }

    @Test
    void testDeleteUserAndRelatedRecords() {
        AppUser user = new AppUser(
                "deleteuser@example.com",
                "password123",
                "Delete User",
                LocalDate.of(1995, 12, 12),
                "female",
                "Burgas",
                AppUserRole.USER,
                true
        );
        appUserService.save(user);

        Long userId = user.getId();
        appUserService.deleteUserAndRelatedRecords(userId);

        assertFalse(appUserRepository.findById(userId).isPresent());
    }
}
