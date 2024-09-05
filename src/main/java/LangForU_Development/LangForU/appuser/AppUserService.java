package LangForU_Development.LangForU.appuser;

import LangForU_Development.LangForU.email.EmailService;
import LangForU_Development.LangForU.email.EmailTemplateService;
import LangForU_Development.LangForU.registration.admin.AdminConfirmationToken;
import LangForU_Development.LangForU.registration.admin.AdminConfirmationTokenService;
import LangForU_Development.LangForU.registration.token.ConfirmationToken;
import LangForU_Development.LangForU.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final AdminConfirmationTokenService adminConfirmationTokenService;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public AppUser authenticate(String email, String rawPassword) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            if (bCryptPasswordEncoder.matches(rawPassword, appUser.getPassword())) {
                return appUser;
            }
        }
        return null;
    }

    public List<AppUser> getEnabledUsersWithRoleUser() {
        return appUserRepository.findByAppUserRoleAndEnabled(AppUserRole.USER, true);
    }

    public AppUser findUserById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (appUserRepository.existsById(id)) {
            int updatedRows = appUserRepository.disableAppUser(id);
            if (updatedRows == 0) {
                throw new RuntimeException("User not found or already deleted with id: " + id);
            }
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public String sendAdminRequest(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        AdminConfirmationToken adminToken = new AdminConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        adminConfirmationTokenService.saveAdminConfirmationToken(adminToken);

        String link = "http://localhost:8080/admin/confirmAdmin?token=" + token;
        String emailBody = emailTemplateService.buildEmail_AdminActivation(appUser.getName(), link);
        emailService.send(appUser.getEmail(), emailBody);

        return token;
    }

    public int confirmAdmin(String token) {
        Optional<AdminConfirmationToken> optionalToken = adminConfirmationTokenService.getToken(token);

        if (optionalToken.isPresent()) {
            AdminConfirmationToken adminToken = optionalToken.get();
            if (adminToken.getConfirmedAt() == null && adminToken.getExpiresAt().isAfter(LocalDateTime.now())) {
                adminConfirmationTokenService.setConfirmedAt(token);
                appUserRepository.updateUserRole(adminToken.getAppUser().getId(), AppUserRole.ADMIN);
                return 1; // Success
            }
        }
        return 0; // Failure
    }
}
