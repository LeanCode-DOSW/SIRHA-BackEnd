package edu.dosw.sirha.sirha_backend.domain.model;

import java.util.*;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.dto.AcademicIndicatorsDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

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
    
    public List<SubjectDecorator> getPassedSubjects() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.VERDE)
            .toList();
    }

    public List<SubjectDecorator> getSubjectsInProgress() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.AMARILLO)
            .toList();
    }

    public List<SubjectDecorator> getFailedSubjects() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.ROJO)
            .toList();
    }

    public List<SubjectDecorator> getSubjectsNotTaken() {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == SemaforoColores.GRIS)
            .toList();
    }

    public int getSubjectsCount() {
        return subjects.size();
    }

    public int getPassedSubjectsCount() {
        return getPassedSubjects().size();
    }
    public int getSubjectsInProgressCount() {
        return getSubjectsInProgress().size();
    }
    public int getFailedSubjectsCount() {
        return getFailedSubjects().size();
    }
    public int getSubjectsNotTakenCount() {
        return getSubjectsNotTaken().size();
    }
    public int getTotalSubjectsCount() {
        return subjects.size();
    }
    public int getCreditsByColor(SemaforoColores color) {
        return subjects.values().stream()
            .filter(s -> s.getEstadoColor() == color)
            .mapToInt(SubjectDecorator::getCredits)
            .sum();
    }
    
    @Override
    public List<SubjectDecorator> getSubjectsBySemester(int semester) {
        List<SubjectDecorator> subjectsSemester = new ArrayList<>();
        for (SubjectDecorator sd : subjects.values()) {
            if (sd.getSemester() == semester) {
                subjectsSemester.add(sd);
            }
        }
        return subjectsSemester;
    }

    @Override
    public boolean hasSubject(Subject subject) {
        return subjects.containsKey(subject.getName());
    }

    @Override
    public Careers getCareer() {
        return studyPlan.getCareer();
    }
    

    @Override
    public int[] getContadoresPorEstado() {
        return new int[]{
            getPassedSubjectsCount(),
            getSubjectsInProgressCount(), 
            getFailedSubjectsCount(),
            getSubjectsNotTakenCount()
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
            throw new IllegalStateException("La materia no está en el semáforo");
        }
        if (!isSubjectCursando(subject)) {
            throw new IllegalStateException("La materia no está en curso");
        }

        if (decorator.getGroup().equals(newGroup)) {
            throw new IllegalStateException("El nuevo grupo es el mismo que el actual");
        }
        if (!subject.hasGroup(newGroup)) {
            throw new IllegalStateException("El grupo no pertenece a la materia especificada");
        }
        return true;
    }

    @Override
    public void enrollSubjectInGroup(Subject subject, Group group) {

        SubjectDecorator decorator = subjects.get(subject.getName());
        if (decorator == null) {
            throw new IllegalArgumentException("La materia no está en el semáforo");
        }
        if (!subject.hasGroup(group)) {
            throw new IllegalArgumentException("El grupo no pertenece a la materia especificada");
        }
        decorator.inscribir(group);
        //faltaria set semestre
    }

    @Override
    public Map<AcademicPeriod, List<Schedule>> getAllSchedules() {
        Map<AcademicPeriod, List<Schedule>> schedules = new HashMap<>();

        subjects.values().forEach(sd -> schedules.putIfAbsent(sd.getAcademicPeriod(), sd.getSchedules()));
        return schedules;
    }

    @Override
    public Map<SemaforoColores, List<SubjectDecoratorDTO>> getAcademicPensum() {
        Map<SemaforoColores, List<SubjectDecoratorDTO>> pensum = new EnumMap<>(SemaforoColores.class);
        
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

    @Override
    public Map<SemaforoColores, Double> getPercentageByColor() {
        Map<SemaforoColores, Double> porcentajes = new EnumMap<>(SemaforoColores.class);

        int totalSubjects = getTotalSubjectsCount();

        if (totalSubjects == 0) {return porcentajes;}

        porcentajes.put(SemaforoColores.VERDE, (getSubjectsByColorCount(SemaforoColores.VERDE) / (double) totalSubjects) * 100);
        porcentajes.put(SemaforoColores.AMARILLO, (getSubjectsByColorCount(SemaforoColores.AMARILLO) / (double) totalSubjects) * 100);
        porcentajes.put(SemaforoColores.ROJO, (getSubjectsByColorCount(SemaforoColores.ROJO) / (double) totalSubjects) * 100);
        porcentajes.put(SemaforoColores.GRIS, (getSubjectsByColorCount(SemaforoColores.GRIS) / (double) totalSubjects) * 100);


        return porcentajes;
    }

    /**
     * Calcula el porcentaje de avance general en el plan de estudios
     * @return Porcentaje de avance (0.0 a 100.0)
     */
    @Override
    public double getOverallProgressPercentage() {
        int totalSubjects = getTotalSubjectsCount();
        if (totalSubjects == 0) {
            return 0.0;
        }
        int approvedSubjects = getPassedSubjectsCount();
        return (approvedSubjects / (double) totalSubjects) * 100;
    }
    /**
     * Calcula la tasa de éxito académico (materias aprobadas vs total cursadas)
     * @return Porcentaje de éxito académico (0.0 a 100.0)
     */
    @Override
    public double getAcademicSuccessRate() {
        int totalSubjects = getPassedSubjectsCount() + getFailedSubjectsCount();
        if (totalSubjects == 0) {
            return 0.0;
        }
        int approvedSubjects = getPassedSubjectsCount();
        return (approvedSubjects / (double) totalSubjects) * 100;
    }
    /**
     * Obtiene el porcentaje de créditos completados
     * @return Porcentaje de créditos obtenidos del total requerido
     */
    @Override
    public double getCompletedCreditsPercentage() {
        int totalCredits = getCreditsStudyPlan();
        if (totalCredits == 0) {
            return 0.0;
        }
        int approvedCredits = getCreditsByColor(SemaforoColores.VERDE);
        return (approvedCredits / (double) totalCredits) * 100;
    }

    @Override
    public AcademicIndicatorsDTO getAcademicIndicators() {
        double overallProgress = getOverallProgressPercentage();
        SemaforoColores globalIndicator;
        if (overallProgress >= 75.0) {
            globalIndicator = SemaforoColores.VERDE;
        } else if (overallProgress >= 50.0) {
            globalIndicator = SemaforoColores.AMARILLO;
        } else if (overallProgress >= 25.0) {
            globalIndicator = SemaforoColores.ROJO;
        } else {
            globalIndicator = SemaforoColores.GRIS;
        }

        double academicSuccessRate = getAcademicSuccessRate();
        double creditsCompletionPercentage = getCompletedCreditsPercentage();
        boolean academicRisk = globalIndicator == SemaforoColores.ROJO || globalIndicator == SemaforoColores.GRIS;

        String academicStatus;
        if (overallProgress >= 90.0) {
            academicStatus = "Excelente";
        } else if (overallProgress >= 70.0) {
            academicStatus = "Bueno";
        } else if (overallProgress >= 50.0) {
            academicStatus = "Regular";
        } else {
            academicStatus = "Necesita Mejorar";
        }

        Map<SemaforoColores, Double> trafficLightSummary = getPercentageByColor();

        return new AcademicIndicatorsDTO(
            overallProgress,
            globalIndicator,
            academicSuccessRate,
            creditsCompletionPercentage,
            academicRisk,
            academicStatus,
            trafficLightSummary
        );
    }
    
    @Override
    public int getCreditsStudyPlan() {
        return studyPlan.calculateTotalCredits();
    }

    @Override
    public void approveSubject(String subject) throws SirhaException {
        SubjectDecorator decorator = subjects.get(subject);
        if (decorator == null) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
        }
        decorator.aprobar();
    }

    @Override
    public void failSubject(String subject) throws SirhaException {
        SubjectDecorator decorator = subjects.get(subject);
        if (decorator == null) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
        }
        decorator.reprobar();
    }
    @Override
    public void unenrollSubjectFromGroup(String subject, Group group) throws SirhaException {
        SubjectDecorator decorator = subjects.get(subject);
        if (decorator == null) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
        }
        decorator.retirar();
    }

    @Override
    public int getSubjectsByColorCount(SemaforoColores color) {
        return (int) subjects.values().stream()
            .filter(s -> s.getEstadoColor() == color)
            .count();
    }

    @Override
    public void setSubjectSemester(String name, int semester) throws SirhaException {
        SubjectDecorator decorator = subjects.get(name);
        if (decorator == null) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
        }
        decorator.setSemester(semester);
    }

}
