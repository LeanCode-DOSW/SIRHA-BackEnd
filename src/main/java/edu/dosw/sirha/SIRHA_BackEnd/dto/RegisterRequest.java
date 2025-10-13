package edu.dosw.sirha.sirha_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * DTO para solicitudes de registro de estudiantes.
 */
public class RegisterRequest {
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener formato válido")
    @Pattern(regexp = ".*@universidad\\.edu\\.co$", 
             message = "Debe usar email institucional (@universidad.edu.co)")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    
    @NotBlank(message = "El código estudiantil es obligatorio")
    @Pattern(regexp = "\\d{4}-\\d{6}", 
             message = "Código estudiantil debe tener formato YYYY-NNNNNN")
    private String codigo;

    public RegisterRequest(String username, String email, String password, String codigo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.codigo = codigo;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

}
