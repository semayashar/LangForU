package LangForU_Development.LangForU.registration.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminConfirmationTokenService {

    private final AdminConfirmationTokenRepository adminConfirmationTokenRepository;

    public void saveAdminConfirmationToken(AdminConfirmationToken token) {
        adminConfirmationTokenRepository.save(token);
    }

    public Optional<AdminConfirmationToken> getToken(String token) {
        return adminConfirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return adminConfirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
