package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import java.util.stream.Collectors;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.AcademicProgress;
import edu.dosw.sirha.SIRHA_BackEnd.dto.SubjectDecoratorDTO;

/**
 * Implementación del progreso académico que mantiene el estado de las materias
 * de un estudiante mediante un sistema de semáforo por colores.
 */
public class Semaforo implements AcademicProgress {
    private Map<String, SubjectDecorator> subjects;
    private final StudyPlan studyPlan;
    private AcademicPeriod currentPeriod;

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

    public Collection<SubjectDecorator> getSubjects() {
        return subjects.values();
    }
    public AcademicPeriod getCurrentAcademicPeriod() {
        return currentPeriod;
    }
    public void setCurrentAcademicPeriod(AcademicPeriod period) {
        this.currentPeriod = period;
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
        return decorator.getEstadoColor() == SemaforoColores.VERDE;
    }
    @Override
    public boolean isSubjectCursando(Subject subject) {

        if (!subjects.containsKey(subject.getName())) {
            System.out.println("Error: La materia no está en el semáforo");
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.AMARILLO;
    }
    @Override
    public boolean isSubjectReprobada(Subject subject) {

        if (!subjects.containsKey(subject.getName())) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.ROJO;
    }
    @Override
    public boolean isSubjectNoCursada(Subject subject) {

        if (!subjects.containsKey(subject.getName())) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject.getName());
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.GRIS;
    }


    public boolean verifyChangeGroup(Subject subject, Group newGroup) {

        SubjectDecorator decorator = subjects.get(subject.getName());
        if (decorator == null) {
            System.out.println("Error: La materia no está en el semáforo");
            throw new IllegalStateException("La materia no está en el semáforo");
        }
        if (!isSubjectCursando(subject)) {
            System.out.println("Error: La materia no está en curso");
            throw new IllegalStateException("La materia no está en curso");
        }

        if (decorator.getGroup().equals(newGroup)) {
            System.out.println("Error: El nuevo grupo es el mismo que el actual");
            throw new IllegalStateException("El nuevo grupo es el mismo que el actual");
        }
        if (!subject.hasGroup(newGroup)) {
            System.out.println("Error: El grupo no pertenece a la materia especificada");
            throw new IllegalStateException("El grupo no pertenece a la materia especificada");
        }
        return true;
    }

    @Override
    public void enrollSubjectInGroup(Subject subject, Group group) {

        SubjectDecorator decorator = subjects.get(subject.getName());
        if (decorator == null) {
            System.out.println("Error: La materia no está en el semáforo");
            throw new IllegalArgumentException("La materia no está en el semáforo");
        }
        if (!subject.hasGroup(group)) {
            System.out.println("Error: El grupo no pertenece a la materia especificada");
            throw new IllegalArgumentException("El grupo no pertenece a la materia especificada");
        }
        decorator.inscribir(group);
        //faltaria set semestre
    }

    @Override
    public Map<AcademicPeriod, List<Schedule>> getAllSchedules() {
        Map<AcademicPeriod, List<Schedule>> schedules = new HashMap<>();

        subjects.values().forEach(sd -> { schedules.putIfAbsent(sd.getAcademicPeriod(), sd.getSchedules());
        });
        return schedules;
    }

    @Override
    public Map<SemaforoColores, List<SubjectDecoratorDTO>> getAcademicPensum() {
        Map<SemaforoColores, List<SubjectDecoratorDTO>> pensum = new HashMap<>();
        
        subjects.values().forEach(sd -> {
            SemaforoColores color = sd.getEstadoColor();
            pensum.putIfAbsent(color, new ArrayList<>());
            
            SubjectDecoratorDTO dto = new SubjectDecoratorDTO(
                sd.getId(),
                sd.getName(),
                sd.getCredits(),
                sd.getSemester(),
                color,
                sd.getGrade()
            );
            
            pensum.get(color).add(dto);
        });
        
        return pensum;
    }

}
