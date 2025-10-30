package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;

import edu.dosw.sirha.sirha_backend.util.ValidationUtil;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void emailValidation_validAndInvalid() {
        assertTrue(ValidationUtil.isValidInstitutionalEmail("juan.perez@universidad.edu.co"));
        assertFalse(ValidationUtil.isValidInstitutionalEmail("juan.perez@gmail.com"));
        assertFalse(ValidationUtil.isValidInstitutionalEmail(null));
    }

    @Test
    void studentCodeValidation_cases() {
        assertTrue(ValidationUtil.isValidStudentCode("ADM-0001"));
        assertTrue(ValidationUtil.isValidStudentCode("est-1234")); // case-insensitive
        assertFalse(ValidationUtil.isValidStudentCode("ADM1234"));
        assertFalse(ValidationUtil.isValidStudentCode("XX-1234"));
    }

    @Test
    void usernameValidation_examples() {
        assertTrue(ValidationUtil.isValidUsername("usuario_01"));
        assertFalse(ValidationUtil.isValidUsername("ab")); // too short
        assertFalse(ValidationUtil.isValidUsername(null));
    }

    @Test
    void passwordValidation_examples() {
        assertTrue(ValidationUtil.isValidPassword("Password1"));
        assertFalse(ValidationUtil.isValidPassword("short1A")); // too short
        assertFalse(ValidationUtil.isValidPassword("nouppercase1"));
        assertFalse(ValidationUtil.isValidPassword(null));
    }

    @Test
    void validateRegistration_throwsOnInvalid() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                ValidationUtil.validateRegistration("ab", "mail@universidad.edu.co", "Password1", "ADM-0001")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("username"));
    }

}
