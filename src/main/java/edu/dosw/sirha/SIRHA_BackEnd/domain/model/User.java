package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Authenticable;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;

public abstract class User implements Authenticable {
    private String id;
    private String name;
    private String email;
    private String password;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password.startsWith("$2a$") ? password : PasswordUtils.hashPassword(password);
    }
    
    public boolean verificarContraseña(String rawPassword) {
        return PasswordUtils.verifyPassword(rawPassword, this.password);
    }
    
    public String getUsername(){
        return name;
    }
    public void setUsername(String username){
        this.name = username;
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