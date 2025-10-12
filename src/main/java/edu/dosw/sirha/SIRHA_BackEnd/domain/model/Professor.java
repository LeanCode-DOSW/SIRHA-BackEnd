package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

public class Professor extends User {

    public Professor() {
        super();
    }
    public Professor(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
    }
}
