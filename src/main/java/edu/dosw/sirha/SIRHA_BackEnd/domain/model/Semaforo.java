package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import java.util.stream.Collectors;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
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
        studyPlan.getSubjects().forEach((name, subject) -> {
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

    public int getStudyPlanSubjectsCount() {
        return studyPlan.getSubjects().size();
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
            .mapToInt(SubjectDecorator::getCredits)
            .sum();
    }

    public List<SubjectDecorator> getMateriasPorSemestre(int semestre) {
        List<SubjectDecorator> materiasSemestre = new ArrayList<>();
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getSemester() == semestre) {
                materiasSemestre.add(sd);
            }
        }
        return materiasSemestre;
    }

    @Override
    public boolean hasSubject(Subject subject) {
        if (subject == null) {
            return false;
        }
        return subjects.containsKey(subject.getName());
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

    @Override
    public boolean isSubjectApproved(Subject subject) {
        if (subject == null) {
            return false;
        }
        if (!subjects.containsKey(subject.getName())) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.VERDE;
    }
    @Override
    public boolean isSubjectCursando(Subject subject) {
        if (subject == null) {
            return false;
        }
        if (!subjects.containsKey(subject.getName())) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.AMARILLO;
    }
    @Override
    public boolean isSubjectReprobada(Subject subject) {
        if (subject == null) {
            return false;
        }
        if (!subjects.containsKey(subject.getName())) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.ROJO;
    }
    @Override
    public boolean isSubjectNoCursada(Subject subject) {
        if (subject == null) {
            return false;
        }
        if (!subjects.containsKey(subject.getName())) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.GRIS;
    }

    @Override
    public void enrollSubjectInGroup(Subject subject, Group group) {
        if (subject == null || group == null) {
            System.out.println("Error: La materia y el grupo no pueden ser nulos");
            throw new IllegalArgumentException("La materia y el grupo no pueden ser nulos");
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        if (decorator == null) {
            System.out.println("Error: La materia no está en el semáforo");
            throw new IllegalArgumentException("La materia no está en el semáforo");
        }
        if (!subject.isHasGroup(group)) {
            System.out.println("Error: El grupo no pertenece a la materia especificada");
            throw new IllegalArgumentException("El grupo no pertenece a la materia especificada");
        }
        decorator.inscribir(group);
        //faltaria set semestre
    }

}
