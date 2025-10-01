package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import java.util.stream.Collectors;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.AcademicProgress;

/**
 * Implementación del progreso académico que mantiene el estado de las materias
 * de un estudiante mediante un sistema de semáforo por colores.
 */
public class Semaforo implements AcademicProgress {
    private Map<String, SubjectDecorator> subjects;
    private final StudyPlan studyPlan;

    public Semaforo(StudyPlan studyPlan) {
        this.subjects = new HashMap<>();
        this.studyPlan = studyPlan;
        iniciarSemaforo();
    }

    private void iniciarSemaforo() {
        studyPlan.getMaterias().forEach((name, subject) -> {
            SubjectDecorator decorator = new SubjectDecorator(subject);
            subjects.put(name, decorator);
        });
    }

    public SubjectDecorator getSubject(String nombre) {
        return subjects.get(nombre);
    }

    public Collection<SubjectDecorator> getSubjects() {
        return subjects.values();
    }

    public void setSubjects(Map<String, SubjectDecorator> subjects) {
        this.subjects = subjects != null ? new HashMap<>(subjects) : new HashMap<>();
    }

    public StudyPlan getStudyPlan() {
        return studyPlan;
    }
    public List<SubjectDecorator> getMateriasAprobadas() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.VERDE)
            .collect(Collectors.toList());
    }

    public List<SubjectDecorator> getMateriasCursando() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.AMARILLO)
            .collect(Collectors.toList());
    }

    public List<SubjectDecorator> getMateriasReprobadas() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.ROJO)
            .collect(Collectors.toList());
    }

    public List<SubjectDecorator> getMateriasNoCursadas() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.GRIS)
            .collect(Collectors.toList());
    }

    public int getSubjectsCount() {
        return subjects.size();
    }

    public int getMateriasAprobadasCount() {
        return getMateriasAprobadas().size();
    }
    public int getMateriasCursandoCount() {
        return getMateriasCursando().size();
    }
    public int getMateriasReprobadasCount() {
        return getMateriasReprobadas().size();
    }
    public int getMateriasNoCursadasCount() {
        return getMateriasNoCursadas().size();
    }

    public int getCreditosPorColor(SemaforoColores color) {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == color)
            .mapToInt(SubjectDecorator::getCreditos)
            .sum();
    }

    public List<SubjectDecorator> getMateriasPorSemestre(int semestre) {
        List<SubjectDecorator> materiasSemestre = new ArrayList<>();
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getSemestre() == semestre) {
                materiasSemestre.add(sd);
            }
        }
        return materiasSemestre;
    }

    @Override
    public int[] getContadoresPorEstado() {
        return new int[]{
            getMateriasAprobadasCount(),
            getMateriasCursandoCount(), 
            getMateriasReprobadasCount(),
            getMateriasNoCursadasCount()
        };
    }

}
