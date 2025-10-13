package edu.dosw.sirha.sirha_backend.util;

import java.util.regex.Pattern;

/**
 * Utilidades para validaciones del sistema SIRHA.
 */
public class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Patrones de validación
    private static final Pattern EMAIL_INSTITUCIONAL = 
        Pattern.compile("^[a-zA-Z0-9._%+-]+@universidad\\.edu\\.co$");
    
    private static final Pattern CODIGO_ESTUDIANTIL = 
        Pattern.compile("^\\d{4}-\\d{6}$");
    
    private static final Pattern USERNAME_VALIDO = 
        Pattern.compile("^[a-zA-Z0-9._-]{3,50}$");

    /**
     * Valida que el email sea del dominio institucional.
     */
    public static boolean isValidInstitutionalEmail(String email) {
        return email != null && EMAIL_INSTITUCIONAL.matcher(email).matches();
    }

    /**
     * Valida el formato del código estudiantil (YYYY-NNNNNN).
     */
    public static boolean isValidStudentCode(String codigo) {
        return codigo != null && CODIGO_ESTUDIANTIL.matcher(codigo).matches();
    }

    /**
     * Valida el formato del username.
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_VALIDO.matcher(username).matches();
    }

    /**
     * Valida la fortaleza de la contraseña.
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Al menos una mayúscula, una minúscula, un número
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUpper && hasLower && hasDigit;
    }

    /**
     * Valida todos los campos de registro de estudiante.
     */
    public static void validateStudentRegistration(String username, String email, 
                                                 String password, String codigo) {
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Username inválido: debe tener 3-50 caracteres alfanuméricos");
        }
        
        if (!isValidInstitutionalEmail(email)) {
            throw new IllegalArgumentException("Email debe ser del dominio @universidad.edu.co");
        }
        
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Contraseña debe tener al menos 8 caracteres, mayúscula, minúscula y número");
        }
        
        if (!isValidStudentCode(codigo)) {
            throw new IllegalArgumentException("Código estudiantil debe tener formato YYYY-NNNNNN");
        }
    }
}