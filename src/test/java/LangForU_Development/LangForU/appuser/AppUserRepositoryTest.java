package LangForU_Development.LangForU.appuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void testFindByEmail() {
        AppUser appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        appUserRepository.save(appUser);
        Optional<AppUser> foundUser = appUserRepository.findByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testEnableAppUser() {
        AppUser appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        appUser.setEnabled(false);
        appUserRepository.save(appUser);
        int result = appUserRepository.enableAppUser("test@example.com");
        assertEquals(1, result);
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<AppUser> foundUser = appUserRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testEnableAppUserNotFound() {
        int result = appUserRepository.enableAppUser("nonexistent@example.com");
        assertEquals(0, result);
    }

    @Test
    void testSaveAppUser() {
        AppUser appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        AppUser savedUser = appUserRepository.save(appUser);
        assertNotNull(savedUser.getId());
    }

    @Test
    void testDeleteAppUser() {
        AppUser appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        appUserRepository.save(appUser);
        appUserRepository.delete(appUser);
        Optional<AppUser> foundUser = appUserRepository.findByEmail("test@example.com");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUpdateAppUser() {
        AppUser appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        appUserRepository.save(appUser);
        appUser.setName("Jane Doe");
        appUserRepository.save(appUser);
        Optional<AppUser> foundUser = appUserRepository.findByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("Jane Doe", foundUser.get().getName());
    }

    @Test
    void testFindAllAppUsers() {
        AppUser appUser1 = new AppUser(
                "test1@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        AppUser appUser2 = new AppUser(
                "test2@example.com",
                "password123",
                "Jane Doe",
                LocalDate.of(1990, 1, 1),
                "Female",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        appUserRepository.save(appUser1);
        appUserRepository.save(appUser2);
        Iterable<AppUser> users = appUserRepository.findAll();
        assertEquals(2, ((Collection<?>) users).size());
    }

    @Test
    void testFindById() {
        AppUser appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        AppUser savedUser = appUserRepository.save(appUser);
        Optional<AppUser> foundUser = appUserRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void testDeleteById() {
        AppUser appUser = new AppUser(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "+1234567890",
                AppUserRole.USER
        );
        AppUser savedUser = appUserRepository.save(appUser);
        appUserRepository.deleteById(savedUser.getId());
        Optional<AppUser> foundUser = appUserRepository.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }
}