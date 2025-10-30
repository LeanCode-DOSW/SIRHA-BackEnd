package edu.dosw.sirha.sirha_backend.domain.model;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class Professor extends User {

    public Professor() {
        super();
    }
    public Professor(String username, String email, String passwordHash) throws SirhaException {
        super(username, email, passwordHash);
    }
}
