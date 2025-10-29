package edu.dosw.sirha.sirha_backend.domain.model;

import java.util.*;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.dto.AcademicIndicatorsDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Implementación del progreso académico que mantiene el estado de las materias
 * de un estudiante mediante un sistema de semáforo por colores.
 *
 * Utiliza referencias (IDs) para desacoplar del StudyPlan pero mantiene
 * información crítica en cache para evitar consultas constantes.
 */
public class Semaforo implements AcademicProgress {
    private Map<String, SubjectDecorator> subjects; // Key: nombre de la materia

    // ✅ Referencias e información crítica
    private final String studyPlanId;
    private final Careers career;
    private final int totalCreditsInPlan; // Cache del total de créditos

    private AcademicPeriod currentPeriod;

    /**
     * Constructor completo del Semaforo.
     *
     * @param studyPlanId ID del plan de estudios
     * @param career Carrera del plan
     * @param totalCreditsInPlan Total de créditos del plan (para cache)
     * @param subjectsFromPlan Lista de materias del plan
     */
    public Semaforo(String studyPlanId, Careers career, int totalCreditsInPlan, List<Subject> subjectsFromPlan) throws SirhaException {
        if (studyPlanId == null || studyPlanId.trim().isEmpty()) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El ID del plan de estudios no puede estar vacío");
        }
        if (career == null) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "La carrera no puede ser null");
        }
        if (subjectsFromPlan == null || subjectsFromPlan.isEmpty()) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El plan de estudios debe tener al menos una materia");
        }

        this.subjects = new HashMap<>();
        this.studyPlanId = studyPlanId;
        this.career = career;
        this.totalCreditsInPlan = totalCreditsInPlan;

        iniciarSemaforo(subjectsFromPlan);
    }

    /**
     * Inicializa el semáforo con las materias del plan de estudios.
     */
    private void iniciarSemaforo(List<Subject> subjectsFromPlan) throws SirhaException {
        for (Subject subject : subjectsFromPlan) {
            if (subject == null) {
                continue; // Ignorar materias null
            }
            SubjectDecorator decorator = new SubjectDecorator(subject);
            subjects.put(subject.getName(), decorator);
        }

        if (subjects.isEmpty()) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "No se pudieron inicializar materias en el semáforo");
        }
    }

    // ========== Getters básicos ==========

    public String getStudyPlanId() {
        return studyPlanId;
    }

    @Override
    public Careers getCareer() {
        return career;
    }

    @Override
    public int getCreditsStudyPlan() {
        return totalCreditsInPlan;
    }

    public Collection<SubjectDecorator> getSubjects() {
        return Collections.unmodifiableCollection(subjects.values());
    }

    public AcademicPeriod getCurrentAcademicPeriod() {
        return currentPeriod;
    }

    public void setCurrentAcademicPeriod(AcademicPeriod period) {
        this.currentPeriod = period;
    }

    // ========== Métodos de consulta por color ==========

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

    // ========== Contadores ==========

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
    public int getSubjectsByColorCount(SemaforoColores color) {
        return (int) subjects.values().stream()
                .filter(s -> s.getEstadoColor() == color)
                .count();
    }

    // ========== Métodos de AcademicProgress ==========

    @Override
    public List<SubjectDecorator> getSubjectsBySemester(int semester) {
        return subjects.values().stream()
                .filter(sd -> sd.getSemester() == semester)
                .toList();
    }

    @Override
    public boolean hasSubject(Subject subject) {
        return subject != null && subjects.containsKey(subject.getName());
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
    public boolean isSubjectApproved(String subject) {
        if (subject == null || !subjects.containsKey(subject)) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject);
        return decorator.getEstadoColor() == SemaforoColores.VERDE;
    }

    @Override
    public boolean isSubjectCursando(String subject) {
        if (!subjects.containsKey(subject)) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject);
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.AMARILLO;
    }

    @Override
    public boolean isSubjectReprobada(String subject) {
        if (!subjects.containsKey(subject)) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject);
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.ROJO;
    }

    @Override
    public boolean isSubjectNoCursada(String subject) {
        if (!subjects.containsKey(subject)) {
            return false;
        }
        SubjectDecorator decorator = subjects.get(subject);
        return decorator != null && decorator.getEstadoColor() == SemaforoColores.GRIS;
    }

    // ========== Gestión de inscripciones ==========

    public boolean verifyChangeGroup(Subject subject, Group newGroup) throws SirhaException {
        SubjectDecorator decorator = subjects.get(subject.getName());
        if (decorator == null) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "La materia no está en el semáforo");
        }
        if (!isSubjectCursando(subject.getName())) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_IN_PROGRESS, "La materia no está en curso");
        }

        if (decorator.getGroup().equals(newGroup)) {
            throw SirhaException.of(ErrorCodeSirha.SAME_GROUP, "El nuevo grupo es el mismo que el actual");
        }
        if (!subject.hasGroup(newGroup)) {
            throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND, "El grupo no pertenece a la materia especificada");
        }
        return true;
    }

    @Override
    public void enrollSubjectInGroup(Subject subject, Group group) throws SirhaException {
        SubjectDecorator decorator = subjects.get(subject.getName());
        if (decorator == null) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "La materia no está en el semáforo");
        }
        if (!subject.hasGroup(group)) {
            throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND, "El grupo no pertenece a la materia especificada");
        }
        decorator.inscribir(group);
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
    public void setSubjectSemester(String name, int semester) throws SirhaException {
        SubjectDecorator decorator = subjects.get(name);
        if (decorator == null) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
        }
        decorator.setSemester(semester);
    }

    // ========== Horarios ==========

    @Override
    public Map<AcademicPeriod, List<Schedule>> getAllSchedules() {
        Map<AcademicPeriod, List<Schedule>> schedules = new HashMap<>();
        for (SubjectDecorator sd : subjects.values()) {
            try {
                AcademicPeriod period = sd.getAcademicPeriod();
                List<Schedule> subjectSchedules = sd.getSchedules();
                schedules.putIfAbsent(period, new ArrayList<>());
                schedules.get(period).addAll(subjectSchedules);
            } catch (SirhaException e) {
                // ignorar materias sin grupo asignado
            }
        }
        return schedules;
    }

    // ========== Indicadores académicos ==========

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
        if (totalSubjects == 0) {
            return porcentajes;
        }

        porcentajes.put(SemaforoColores.VERDE, (getSubjectsByColorCount(SemaforoColores.VERDE) / (double) totalSubjects) * 100);
        porcentajes.put(SemaforoColores.AMARILLO, (getSubjectsByColorCount(SemaforoColores.AMARILLO) / (double) totalSubjects) * 100);
        porcentajes.put(SemaforoColores.ROJO, (getSubjectsByColorCount(SemaforoColores.ROJO) / (double) totalSubjects) * 100);
        porcentajes.put(SemaforoColores.GRIS, (getSubjectsByColorCount(SemaforoColores.GRIS) / (double) totalSubjects) * 100);

        return porcentajes;
    }

    @Override
    public double getOverallProgressPercentage() {
        int totalSubjects = getTotalSubjectsCount();
        if (totalSubjects == 0) {
            return 0.0;
        }
        int approvedSubjects = getPassedSubjectsCount();
        return (approvedSubjects / (double) totalSubjects) * 100;
    }

    @Override
    public double getAcademicSuccessRate() {
        int totalAttempted = getPassedSubjectsCount() + getFailedSubjectsCount();
        if (totalAttempted == 0) {
            return 0.0;
        }
        int approvedSubjects = getPassedSubjectsCount();
        return (approvedSubjects / (double) totalAttempted) * 100;
    }

    @Override
    public double getCompletedCreditsPercentage() {
        if (totalCreditsInPlan == 0) {
            return 0.0;
        }
        int approvedCredits = getCreditsByColor(SemaforoColores.VERDE);
        return (approvedCredits / (double) totalCreditsInPlan) * 100;
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
        boolean academicRisk = globalIndicator == SemaforoColores.ROJO;

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
    public StudyPlan getStudyPlan() {
        return null;
    }
}