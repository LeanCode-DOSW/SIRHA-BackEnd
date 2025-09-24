package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Authenticable;

public abstract class User implements Authenticable {
    private String id;
    private String name;
    private String email;
    private String password;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    public boolean verificarContraseña(String rawPassword) {
        // Aquí usarás BCrypt u otra utilidad
        return password.equals(rawPassword);
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
        this.password = passwordHash;
    }
    public String getEmail(){
        return email;
    }
}