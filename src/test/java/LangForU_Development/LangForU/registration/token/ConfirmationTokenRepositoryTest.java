package LangForU_Development.LangForU.registration.token;

import LangForU_Development.LangForU.appuser.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ConfirmationTokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    private ConfirmationToken confirmationToken;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        entityManager.persist(appUser);
        confirmationToken = new ConfirmationToken(
                "test-token",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        entityManager.persist(confirmationToken);
    }

    @Test
    @Transactional
    void testUpdateConfirmedAt() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        int updatedRows = confirmationTokenRepository.updateConfirmedAt("test-token", now);
        assertEquals(1, updatedRows);

        entityManager.flush();
        entityManager.clear();

        ConfirmationToken updatedToken = entityManager.find(ConfirmationToken.class, confirmationToken.getId());
        assertEquals(now, updatedToken.getConfirmedAt().truncatedTo(ChronoUnit.MILLIS));
    }
}