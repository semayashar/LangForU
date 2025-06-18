package LangForU_DevTeam.LangForU.Data_Layer_Tests;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRole;
import LangForU_DevTeam.LangForU.registration.token.ConfirmationToken;
import LangForU_DevTeam.LangForU.registration.token.ConfirmationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ConfirmationTokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Test
    public void whenFindByToken_thenReturnToken() {
        // given
        AppUser user = new AppUser("tokenuser@test.com", "password123", "Token User",
                LocalDate.now().minusYears(25), "Female", "Addr", AppUserRole.USER, false);
        entityManager.persist(user);

        String tokenValue = UUID.randomUUID().toString();
        ConfirmationToken token = new ConfirmationToken(tokenValue, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        entityManager.persist(token);
        entityManager.flush();

        // when
        Optional<ConfirmationToken> found = confirmationTokenRepository.findByToken(tokenValue);

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getToken()).isEqualTo(tokenValue);
    }

    @Test
    public void whenUpdateConfirmedAt_thenTokenIsUpdated() {
        // given
        AppUser user = new AppUser("confirmuser@test.com", "password123", "Confirm User",
                LocalDate.now().minusYears(22), "Male", "Addr", AppUserRole.USER, false);
        entityManager.persist(user);
        String tokenValue = UUID.randomUUID().toString();
        ConfirmationToken token = new ConfirmationToken(tokenValue, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        entityManager.persist(token);
        entityManager.flush();

        // when
        confirmationTokenRepository.updateConfirmedAt(tokenValue, LocalDateTime.now());
        entityManager.clear(); // Add this line to clear the persistence context cache

        Optional<ConfirmationToken> updatedToken = confirmationTokenRepository.findByToken(tokenValue);

        // then
        assertThat(updatedToken.isPresent()).isTrue();
        assertThat(updatedToken.get().getConfirmedAt()).isNotNull();
    }
}