package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Authenticable;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Schedulable;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;
import jakarta.validation.constraints.Email;

@Document(collection = "users")
public abstract class User implements Authenticable, Schedulable {
    @Id
    private int id;
    @Field("username")
    private String username;
    @Field("email")
    @Email
    private String email;
    @Field("password")
    private String password;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password.startsWith("$2a$") ? password : PasswordUtils.hashPassword(password);
    }
    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password.startsWith("$2a$") ? password : PasswordUtils.hashPassword(password);
    }
    
    public boolean verificarContraseña(String rawPassword) {
        return PasswordUtils.verifyPassword(rawPassword, this.password);
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getPasswordHash(){
        return password;
    }
    public String getEmail(){
        return email;
    }
}