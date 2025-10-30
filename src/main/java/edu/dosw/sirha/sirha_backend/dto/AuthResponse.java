package edu.dosw.sirha.sirha_backend.dto;

import javax.management.relation.Role;

/**
 * DTO para respuestas de autenticación exitosa.
 */
public class AuthResponse {
    private String username;
    private String email;
    private String token;
    private String role;
    private long timestamp;

    public AuthResponse() {
        timestamp = System.currentTimeMillis();
    }

    public AuthResponse(String username, String token) {
        this();
        this.username = username;
        this.token = token;
    }
    public AuthResponse(String username, String email, String token) {
        this(username, token);
        this.email = email;
    }

    public AuthResponse(String username, String email, String role, String token) {
        this(username, email, token);
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getRole() { return role; }
    
}
