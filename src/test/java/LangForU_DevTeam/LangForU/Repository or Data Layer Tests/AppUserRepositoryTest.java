package LangForU_DevTeam.LangForU.Data_Layer_Tests;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRepository;
import LangForU_DevTeam.LangForU.appuser.AppUserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AppUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void whenFindByEmail_thenReturnAppUser() {
        // given
        AppUser user = new AppUser("test@test.com", "password123", "Test User",
                LocalDate.of(1990, 1, 1), "Male", "Address", AppUserRole.USER, true);
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<AppUser> found = appUserRepository.findByEmail(user.getEmail());

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }
    @Test
    public void whenEnableAppUser_thenUserIsEnabled() {
        // given
        AppUser user = new AppUser("newuser@test.com", "password123", "New User",
                LocalDate.of(1995, 5, 5), "Female", "New Address", AppUserRole.USER, false);
        entityManager.persist(user);
        entityManager.flush();

        // when
        appUserRepository.enableAppUser(user.getEmail());
        entityManager.clear(); // КОРЕКЦИЯ: Изчистване на persistence context
        Optional<AppUser> updatedUser = appUserRepository.findByEmail(user.getEmail());

        // then
        assertThat(updatedUser.isPresent()).isTrue();
        assertThat(updatedUser.get().getEnabled()).isTrue();
    }

    @Test
    public void whenFindByAppUserRole_thenReturnUsers() {
        // given
        AppUser admin = new AppUser("admin@test.com", "password123", "Admin User",
                LocalDate.of(1980, 1, 1), "Male", "Admin Address", AppUserRole.ADMIN, true);
        AppUser user = new AppUser("user@test.com", "password123", "Regular User",
                LocalDate.of(1990, 1, 1), "Female", "User Address", AppUserRole.USER, true);
        entityManager.persist(admin);
        entityManager.persist(user);
        entityManager.flush();

        // when
        List<AppUser> admins = appUserRepository.findByAppUserRole(AppUserRole.ADMIN);
        List<AppUser> users = appUserRepository.findByAppUserRole(AppUserRole.USER);

        // then
        assertThat(admins).hasSize(1).extracting(AppUser::getAppUserRole).containsOnly(AppUserRole.ADMIN);
        assertThat(users).hasSize(1).extracting(AppUser::getAppUserRole).containsOnly(AppUserRole.USER);
    }
}