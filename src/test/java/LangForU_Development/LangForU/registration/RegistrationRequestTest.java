package LangForU_Development.LangForU.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNullEmail() {
        RegistrationRequest request = new RegistrationRequest(
                null,  // null email
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "1234567890"
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void testNullPassword() {
        RegistrationRequest request = new RegistrationRequest(
                "test@example.com",
                null,  // null password
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "1234567890"
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    void testLongName() {
        String longName = "John Doe".repeat(10); // Example long name
        RegistrationRequest request = new RegistrationRequest(
                "test@example.com",
                "password123",
                longName,
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "1234567890"
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty()); // Adjust if there's a length constraint on name
    }

    @Test
    void testInvalidPhoneNumber() {
        RegistrationRequest request = new RegistrationRequest(
                "test@example.com",
                "password123",
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "123 Main St",
                "not-a-phone-number"  // invalid phone number
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Phone number should be valid", violations.iterator().next().getMessage());
    }

}