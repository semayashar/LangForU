package LangForU_Development.LangForU.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void testValidEmail() {
        assertTrue(emailValidator.test("test@example.com"));
    }

    @Test
    void testInvalidEmail_NoAtSymbol() {
        assertFalse(emailValidator.test("testexample.com"));
    }

    @Test
    void testInvalidEmail_NoDomain() {
        assertFalse(emailValidator.test("test@.com"));
    }

    @Test
    void testInvalidEmail_NoTLD() {
        assertFalse(emailValidator.test("test@example"));
    }

    @Test
    void testInvalidEmail_SpecialCharacters() {
        assertFalse(emailValidator.test("test@exa!mple.com"));
    }

    @Test
    void testNullEmail() {
        assertFalse(emailValidator.test(null));
    }

    @Test
    void testEmptyEmail() {
        assertFalse(emailValidator.test(""));
    }
}