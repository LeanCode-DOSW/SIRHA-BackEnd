package edu.dosw.sirha.SIRHA_BackEnd.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO que representa la solicitud de inicio de sesión en el sistema SIRHA.
 *
 * Este objeto es utilizado para recibir las credenciales enviadas
 * por el cliente (frontend o consumidor de la API) durante el proceso de autenticación.
 *
 * Campos principales:
 * - username: Puede ser tanto el nombre de usuario como el correo electrónico.
 * - password: Contraseña en texto plano que será validada contra la base de datos.
 *
 * Validaciones:
 * - Ninguno de los campos puede ser null ni estar vacío.
 */
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario o email es obligatorio")
    private String username; // Puede ser username o email

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Constructor vacío requerido por frameworks de serialización (como Jackson).
     */
    public LoginRequest() {}

    /**
     * Constructor principal para inicializar una solicitud de login.
     *
     * @param username nombre de usuario o email del estudiante
     * @param password contraseña en texto plano
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ---------- Getters y Setters ----------

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
