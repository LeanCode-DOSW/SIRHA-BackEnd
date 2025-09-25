package edu.dosw.sirha.SIRHA_BackEnd.dto;

import java.util.List;

/**
 * DTO para representar el horario completo de un estudiante.
 * 
 * Agrupa todos los horarios por semestre y proporciona información
 * adicional como el total de créditos y resumen académico.
 */
public class StudentScheduleDTO {
    private String studentId;
    private String studentName;
    private String codigo;
    private int semestre;
    private List<ScheduleDTO> horarios;
    private int totalCreditos;
    private String semestreAcademico; // ej: "2024-1"

    // Constructores
    public StudentScheduleDTO() {}

    public StudentScheduleDTO(String studentId, String studentName, String codigo, 
                             int semestre, List<ScheduleDTO> horarios, int totalCreditos) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.codigo = codigo;
        this.semestre = semestre;
        this.horarios = horarios;
        this.totalCreditos = totalCreditos;
    }

    // Getters y Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public List<ScheduleDTO> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<ScheduleDTO> horarios) {
        this.horarios = horarios;
    }

    public int getTotalCreditos() {
        return totalCreditos;
    }

    public void setTotalCreditos(int totalCreditos) {
        this.totalCreditos = totalCreditos;
    }

    public String getSemestreAcademico() {
        return semestreAcademico;
    }

    public void setSemestreAcademico(String semestreAcademico) {
        this.semestreAcademico = semestreAcademico;
    }
}