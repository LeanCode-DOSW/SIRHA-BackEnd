package edu.dosw.sirha.SIRHA_BackEnd.dto;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.DiasSemana;

/**
 * DTO para representar horarios en las respuestas de la API.
 * 
 * Contiene información sobre el día, horarios, materia, profesor y aula
 * para facilitar la visualización de horarios por parte del frontend.
 */
public class ScheduleDTO {
    private DiasSemana dia;
    private int horaInicio;
    private int horaFin;
    private int materiaId;
    private String materiaNombre;
    private String profesorNombre;
    private String aula;
    private String grupoId;
    private int creditos;

    // Constructores
    public ScheduleDTO() {}

    public ScheduleDTO(DiasSemana dia, int horaInicio, int horaFin, int materiaId, 
                      String materiaNombre, String profesorNombre, String aula, 
                      String grupoId, int creditos) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.profesorNombre = profesorNombre;
        this.aula = aula;
        this.grupoId = grupoId;
        this.creditos = creditos;
    }

    // Getters y Setters
    public DiasSemana getDia() {
        return dia;
    }

    public void setDia(DiasSemana dia) {
        this.dia = dia;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }

    public String getMateriaNombre() {
        return materiaNombre;
    }

    public void setMateriaNombre(String materiaNombre) {
        this.materiaNombre = materiaNombre;
    }

    public String getProfesorNombre() {
        return profesorNombre;
    }

    public void setProfesorNombre(String profesorNombre) {
        this.profesorNombre = profesorNombre;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {
        return String.format("%s %02d:00-%02d:00 - %s (%s) - %s - Aula: %s", 
                           dia, horaInicio, horaFin, materiaNombre, 
                           creditos + " créditos", profesorNombre, aula);
    }
}