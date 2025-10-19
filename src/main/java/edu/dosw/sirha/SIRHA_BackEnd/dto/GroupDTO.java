package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;

public class GroupDTO {
    private Subject subject;
    private int capacidad;
    private int inscritos;
    private Professor profesor;
    private AcademicPeriod currentPeriod;
    private List<Schedule> schedules;
    private String aula;
    private List<Student> estudiantes;

    public GroupDTO(Subject subject, int capacidad, int inscritos, Professor profesor, AcademicPeriod currentPeriod) {
        this.subject = subject;
        this.capacidad = capacidad;
        this.inscritos = inscritos;
        this.profesor = profesor;
        this.currentPeriod = currentPeriod;
    }

    public int getCapacidad() {
        return capacidad;
    }
    public int getInscritos() {
        return inscritos;
    }
    public Professor getProfesor() {
        return profesor;
    }
    public Subject getSubject() {
        return subject;
    }
    public List<Schedule> getSchedules() {
        return schedules;
    }
    public String getAula() {
        return aula;
    }
    public AcademicPeriod getCurrentPeriod() {
        return currentPeriod;
    }
    public List<Student> getEstudiantes() {
        return estudiantes;
    }
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
    public void setAula(String aula) {
        this.aula = aula;
    }
    public void setEstudiantes(List<Student> estudiantes) {
        this.estudiantes = estudiantes;
    }
}