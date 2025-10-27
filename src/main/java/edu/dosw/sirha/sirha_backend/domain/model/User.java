package edu.dosw.sirha.sirha_backend.domain.model;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;
import edu.dosw.sirha.sirha_backend.domain.port.Authenticable;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.util.PasswordUtils;
import jakarta.validation.constraints.Email;

public abstract class User implements Authenticable {
    @Id
    private String id;
    @Field("username")
    private String username;

    @Field("email")
    @Email
    @Indexed(unique = true)
    private String email;
    @Field("password")
    private String password;
    
    protected User() {}

    protected User(String username, String email, String password) throws SirhaException {
        this.username = username;
        setEmail(email);
        this.password = password.startsWith("$2a$") ? password : PasswordUtils.hashPassword(password);
    }
    protected User(String id, String username, String email, String password) throws SirhaException {
        this(username, email, password);
        this.id = id;
    }
    
    public boolean verificarContrasena(String rawPassword) {
        return PasswordUtils.verifyPassword(rawPassword, this.password);
    }

    public void setId(String id){this.id = id;}

    public String getUsername(){return username;}
    public String getId(){return id;}
    public String getPasswordHash(){return password;}
    public String getEmail(){return email;}

    public void setEmail(String email) throws SirhaException {
        if (!isValidEmail(email)) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "Invalid email format");
        }
        this.email = email;
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}