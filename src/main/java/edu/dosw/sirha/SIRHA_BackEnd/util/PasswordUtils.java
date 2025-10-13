package edu.dosw.sirha.sirha_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase utilitaria para el manejo seguro de contraseñas en el sistema SIRHA.
 * 
 * Proporciona funcionalidades para el hash seguro de contraseñas usando BCrypt
 * y la verificación de contraseñas contra sus hashes correspondientes.
 * 
 * Esta clase utiliza BCrypt como algoritmo de hash, que incluye:
 * - Generación automática de salt
 * - Múltiples rondas de hash para mayor seguridad
 * - Resistencia a ataques de rainbow table
 */
public class PasswordUtils {

    private PasswordUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Genera un hash seguro de una contraseña en texto plano.
     * 
     * Este método utiliza el algoritmo BCrypt para crear un hash seguro
     * que incluye un salt único generado automáticamente. El hash resultante
     * es seguro para almacenar en la base de datos.
     * 
     * @param rawPassword la contraseña en texto plano que se desea hashear.
     *                   No debe ser null o vacía.
     * @return el hash seguro de la contraseña, incluyendo el salt.
     *         El formato del hash es compatible con BCrypt.
     * @example
     * <pre>
     * String password = "miContraseñaSegura123";
     * String hashedPassword = PasswordUtils.hashPassword(password);
     * // Resultado: "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7q5.0PD5y"
     * </pre>
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser null o vacía");
        }
        return encoder.encode(rawPassword);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash almacenado.
     * 
     * Este método compara de forma segura una contraseña en texto plano
     * con su hash correspondiente usando el algoritmo BCrypt. La verificación
     * tiene en cuenta el salt incluido en el hash.
     * 
     * @param rawPassword la contraseña en texto plano a verificar.
     *                   No debe ser null.
     * @param hashedPassword el hash almacenado contra el cual verificar.
     *                      Debe ser un hash válido generado por BCrypt.
     *                      No debe ser null.
     * @return true si la contraseña coincide con el hash, false en caso contrario
     * @example
     * <pre>
     * String password = "miContraseñaSegura123";
     * String storedHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7q5.0PD5y";
     * 
     * boolean isValid = PasswordUtils.verifyPassword(password, storedHash);
     * if (isValid) {
     *     System.out.println("Contraseña correcta");
     * } else {
     *     System.out.println("Contraseña incorrecta");
     * }
     * </pre>
     */
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("La contraseña no puede ser null");
        }
        if (hashedPassword == null) {
            throw new IllegalArgumentException("El hash no puede ser null");
        }
        return encoder.matches(rawPassword, hashedPassword);
    }
}
