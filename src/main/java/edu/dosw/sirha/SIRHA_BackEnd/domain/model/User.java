package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Authenticable;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Schedulable;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;
import jakarta.validation.constraints.Email;

//@Document(collection = "users")
public abstract class User implements Authenticable, Schedulable {
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

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        setEmail(email);
        this.password = password.startsWith("$2a$") ? password : PasswordUtils.hashPassword(password);
    }
    public User(String id, String username, String email, String password) {
        this(username, email, password);
        this.id = id;
    }
    
    public boolean verificarContraseña(String rawPassword) {
        return PasswordUtils.verifyPassword(rawPassword, this.password);
    }

    public void setId(String id){this.id = id;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getId(){return id;}
    public String getPasswordHash(){return password;}
    public String getEmail(){return email;}

    public void setEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}