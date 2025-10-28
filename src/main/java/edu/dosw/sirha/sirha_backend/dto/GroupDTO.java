package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para transferencia de datos de Group.
 * Simplificado para operaciones de creación/actualización.
 */
public class GroupDTO {

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser un número positivo")
    private Integer capacidad;

    @NotNull(message = "El periodo académico es obligatorio")
    private String academicPeriodId;

    private String professorId;
    private String aula;
    private List<ScheduleDTO> schedules;

    // Constructor vacío para deserialización
    public GroupDTO() {}

    // Constructor básico para creación
    public GroupDTO(Integer capacidad, String academicPeriodId) {
        this.capacidad = capacidad;
        this.academicPeriodId = academicPeriodId;
    }

    // Constructor completo
    public GroupDTO(Integer capacidad, String academicPeriodId, String professorId,
                    String aula, List<ScheduleDTO> schedules) {
        this.capacidad = capacidad;
        this.academicPeriodId = academicPeriodId;
        this.professorId = professorId;
        this.aula = aula;
        this.schedules = schedules;
    }

    // Getters
    public Integer getCapacidad() {
        return capacidad;
    }

    public String getAcademicPeriodId() {
        return academicPeriodId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public String getAula() {
        return aula;
    }

    public List<ScheduleDTO> getSchedules() {
        return schedules;
    }

    // Setters
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public void setAcademicPeriodId(String academicPeriodId) {
        this.academicPeriodId = academicPeriodId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public void setSchedules(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }
}