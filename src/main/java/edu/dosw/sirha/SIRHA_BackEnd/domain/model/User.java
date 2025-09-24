package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Authenticable;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;

@Document(collection = "users")
public abstract class User implements Authenticable {
    @Id
    private String id;
    @Field("username")
    private String userName;
    @Field("email")
    private String email;
    @Field("password")
    private String password;

    public User() {}
    
    public User(String id, String userName, String email, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password.startsWith("$2a$") ? password : PasswordUtils.hashPassword(password);
    }
    
    public boolean verificarContraseña(String rawPassword) {
        return PasswordUtils.verifyPassword(rawPassword, this.password);
    }

    public String getUsername(){
        return userName;
    }
    public void setUsername(String userName){
        this.userName = userName;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getPasswordHash(){
        return password;
    }
    public void setPasswordHash(String passwordHash){
        this.password = PasswordUtils.hashPassword(passwordHash);
    }
    public String getEmail(){
        return email;
    }
}