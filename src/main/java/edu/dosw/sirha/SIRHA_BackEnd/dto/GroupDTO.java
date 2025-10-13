package edu.dosw.sirha.sirha_backend.dto;

public class GroupDTO {
    private String id;
    private int capacidad;
    private int inscritos;
    private String profesorId;

    public GroupDTO(String id, int capacidad, int inscritos, String profesorId) {
        this.id = id;
        this.capacidad = capacidad;
        this.inscritos = inscritos;
        this.profesorId = profesorId;
    }
    public String getId() {
        return id;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public int getInscritos() {
        return inscritos;
    }
    public String getProfesorId() {
        return profesorId;
    }
}