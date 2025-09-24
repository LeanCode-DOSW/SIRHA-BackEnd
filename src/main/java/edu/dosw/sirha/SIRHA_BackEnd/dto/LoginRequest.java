package edu.dosw.sirha.SIRHA_BackEnd.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "El username o email es obligatorio")
    private String username; // Puede ser username o email

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
