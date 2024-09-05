package LangForU_Development.LangForU.registration;

import LangForU_Development.LangForU.appuser.AppUser;
import LangForU_Development.LangForU.appuser.AppUserRole;
import LangForU_Development.LangForU.appuser.AppUserService;
import LangForU_Development.LangForU.email.EmailSender;
import LangForU_Development.LangForU.email.EmailTemplateService;
import LangForU_Development.LangForU.registration.token.ConfirmationToken;
import LangForU_Development.LangForU.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final EmailTemplateService emailTemplateService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        String token = appUserService.signUpUser(
                new AppUser(
                        request.getEmail(),
                        request.getPassword(),
                        request.getName(),
                        request.getDateOfBirth(),
                        request.getGender(),
                        request.getAddress(),
                        request.getPhoneNumber(),
                        AppUserRole.USER
                )
        );

        String link = "http://localhost:8080/registration/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                emailTemplateService.buildEmail_Registration(request.getName(), link));

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt == null || expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        AppUser appUser = confirmationToken.getAppUser();
        if (appUser == null || appUser.getEmail() == null) {
            throw new IllegalStateException("token not found");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(appUser.getEmail());
        return "confirmed";
    }


}
