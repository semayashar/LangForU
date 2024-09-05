package LangForU_Development.LangForU.registration;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RegistrationRequest {
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;

    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Phone number should be valid")
    private String phoneNumber;


}
