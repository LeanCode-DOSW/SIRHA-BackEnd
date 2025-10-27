package edu.dosw.sirha.sirha_backend.dto;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
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
    
    private Careers career;

    public RegisterRequest(String username, String email, String password, String codigo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.codigo = codigo;
    }
    public RegisterRequest(String username, String email, String password, Careers career) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.career = career;
    }

    public String getUsername() { return username; }
    
    public String getEmail() { return email; }
    
    public String getPassword() { return password; }
    
    public String getCodigo() { return codigo; }

    public Careers getCareer() { return career; }
    public void setCareer(Careers career) { this.career = career; }

}
