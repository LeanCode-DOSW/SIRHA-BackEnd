package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

public class Professor extends User {
    private String horario;   //MIRAR

    public Professor() {
        super();
    }
    public Professor(String username, String email, String passwordHash, String horario) {
        super(username, email, passwordHash);
        this.horario = horario;
    }
}
