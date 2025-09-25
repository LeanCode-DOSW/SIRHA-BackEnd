package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;

public class Semaforo {
    private Map<String, SubjectDecorator> subjects;
    private final StudyPlan studyPlan;

    public Semaforo(StudyPlan studyPlan) {
        this.subjects = new HashMap<>();
        this.studyPlan = studyPlan;
        iniciarSemaforo();
    }

    private void iniciarSemaforo() {
        studyPlan.getMaterias().forEach((name, subject) -> {
            SubjectDecorator sd = new SubjectDecorator(subject);
            sd.setEstadoColor(SemaforoColores.GRIS);
            sd.setSemestreMateria(0);
            subjects.put(name, sd);
        });
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


    public void addSubject(SubjectDecorator subject) {
        if (subject != null) {
            this.subjects.put(subject.getName(), subject);
        }
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

    public List<SubjectDecorator> getMateriasAprobadas() {
        List<SubjectDecorator> aprobadas = new ArrayList<>();
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getEstadoColor() == SemaforoColores.VERDE) {
                aprobadas.add(sd);
            }
        }
        return aprobadas;
    }
    public List<SubjectDecorator> getMateriasCursando() {
        List<SubjectDecorator> cursando = new ArrayList<>();
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getEstadoColor() == SemaforoColores.AMARILLO) {
                cursando.add(sd);
            }
        }
        return cursando;
    }
    public List<SubjectDecorator> getMateriasReprobadas() {
        List<SubjectDecorator> reprobadas = new ArrayList<>();
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getEstadoColor() == SemaforoColores.ROJO) {
                reprobadas.add(sd);
            }
        }
        return reprobadas;
    }
    public List<SubjectDecorator> getMateriasNoCursadas() {
        List<SubjectDecorator> noCursadas = new ArrayList<>();
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getEstadoColor() == SemaforoColores.GRIS) {
                noCursadas.add(sd);
            }
        }
        return noCursadas;
    }
    public int getCreditosPorColor(SemaforoColores color) {
        int totalCreditos = 0;
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getEstadoColor() == color) {
                totalCreditos += sd.getCreditos();
            }
        }
        return totalCreditos;
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

    public void actualizarEstadoMateria(String subjectName, SemaforoColores nuevoEstado) {
        SubjectDecorator sd = subjects.get(subjectName);
        if (sd != null) {
            sd.setEstadoColor(nuevoEstado);
        }
        else {
            System.out.println("Materia no encontrada: " + subjectName);
        }
    }

    public void setSemestreMateria(String subjectName, int semestre) {
        SubjectDecorator sd = subjects.get(subjectName);
        if (sd != null) {
            sd.setSemestreMateria(semestre);
        }
        else {
            System.out.println("Materia no encontrada: " + subjectName);
        }
    }

    public boolean estaInscrito(String subjectName) {
        return subjects.containsKey(subjectName) && 
               subjects.get(subjectName).getEstadoColor() == SemaforoColores.AMARILLO;
    }

    public boolean tieneConflictosHorario(SubjectDecorator subject) {
        List<SubjectDecorator> cursando = getMateriasCursando();
        for (SubjectDecorator sd : cursando) {
            if (sd.tieneConflictoConHorario(subject)) {
                return true;
            }
        }
        return false;
    }
}
