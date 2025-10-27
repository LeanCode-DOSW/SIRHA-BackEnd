package edu.dosw.sirha.sirha_backend.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Role;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;

@Document(collection = "accounts")
public class Account {
    @Id
    private String id;

    private String username;     
    private String email;        
    private String passwordHash; 
    private Role role;           
    private Careers career;

    public Account() {}

    public Account(String username, String email, String passwordHash, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    public Account(String username, String email, String passwordHash, Role role, Careers career) {
        this(username, email, passwordHash, role);
        this.career = career;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public Careers getCareer() { return career; }
}
