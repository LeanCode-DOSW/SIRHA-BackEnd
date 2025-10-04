package edu.dosw.sirha.SIRHA_BackEnd.dto;

/**
 * DTO que representa la respuesta enviada al cliente después de una
 * autenticación exitosa dentro del sistema SIRHA.
 *
 * Incluye información básica del usuario autenticado junto con
 * un mensaje y un timestamp del momento en que se generó la respuesta.
 *
 * Campos principales:
 * - id: Identificador único del usuario en la base de datos.
 * - username: Nombre de usuario utilizado para autenticación.
 * - email: Correo electrónico asociado al usuario.
 * - studentCode: Código estudiantil (si aplica al rol del usuario).
 * - message: Mensaje descriptivo de la autenticación.
 * - timestamp: Momento de creación de la respuesta (en milisegundos).
 */
public class AuthResponse {

    private String id;
    private String username;
    private String email;
    private String studentCode;
    private String message;
    private long timestamp;

    /**
     * Constructor para inicializar un objeto de respuesta de autenticación.
     *
     * @param id identificador único del usuario
     * @param username nombre de usuario
     * @param email correo electrónico del usuario
     * @param studentCode código estudiantil único
     * @param message mensaje de éxito o estado
     */
    public AuthResponse(String id, String username, String email, String studentCode, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.studentCode = studentCode;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    // ---------- Getters y Setters ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
