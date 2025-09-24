package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

public class Professor extends User {
    private String horario;   //MIRAR

    public Professor(String id, String username, String passwordHash, String rol, String horario) {
        super(id, username, passwordHash, rol);
        this.horario = horario;
    }
}
