package edu.dosw.sirha.sirha_backend.dto;

/**
 * DTO para respuestas de autenticación exitosa.
 */
public class AuthResponse {
    private String id;
    private String username;
    private String email;
    private String codigo;
    private String message;
    private long timestamp;

    public AuthResponse(String id, String username, String email, String codigo, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.codigo = codigo;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
}
