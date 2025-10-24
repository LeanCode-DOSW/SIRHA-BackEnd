package edu.dosw.sirha.sirha_backend.domain.model.auth;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Role;

@Document(collection = "accounts")
public class Account {
    @Id
    private String id;

    private String username;     
    private String email;        
    private String passwordHash; 
    private Role role;           
    private String linkedId;     

    public Account() {}

    public Account(String username, String email, String passwordHash, Role role, String linkedId) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.linkedId = linkedId;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public String getLinkedId() { return linkedId; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
