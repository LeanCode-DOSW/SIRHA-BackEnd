package edu.dosw.sirha.sirha_backend.domain.model;
 
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicOperations;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgressViewer;
import edu.dosw.sirha.sirha_backend.domain.port.RequestManager;
import edu.dosw.sirha.sirha_backend.domain.port.ScheduleManager;
import edu.dosw.sirha.sirha_backend.domain.port.SolicitudFactory;
import edu.dosw.sirha.sirha_backend.dto.AcademicIndicatorsDTO;
import edu.dosw.sirha.sirha_backend.dto.RequestApprovalRateDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentReportDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Entidad del dominio que representa a un estudiante en el sistema SIRHA.
 *
 * Esta clase extiende de User y añade funcionalidades específicas para estudiantes,
 * incluyendo gestión de código estudiantil, plan de estudios, semáforo académico
 * y solicitudes realizadas.
 *
 * El estudiante es la entidad central del sistema, ya que todas las operaciones
 * principales (inscripciones, cambios de grupo, solicitudes) se realizan en
 * torno a esta entidad.
 *
 */
@Document(collection = "students")
public class Student extends User implements SolicitudFactory, ScheduleManager, AcademicProgressViewer, AcademicOperations, RequestManager {
    private String codigo;
    private AcademicProgress academicProgress;
    private List<BaseRequest> solicitudes;
    
 
    public Student() {
        solicitudes = new ArrayList<>();
    }

    public Student(String id, String username, String email, String passwordHash, String codigo) throws SirhaException {
        super(id, username, email, passwordHash);
        this.codigo = codigo;
        solicitudes = new ArrayList<>();
    }

    /**
     * Constructor principal para crear un nuevo estudiante.
     * 
     * Inicializa un estudiante con los datos básicos requeridos.
     * La lista de solicitudes se inicializa como lista vacía.
     * El plan de estudios y semáforo deben ser asignados posteriormente.
     */
    public Student(String username, String email, String passwordHash, String codigo) throws SirhaException {
        super(username, email, passwordHash);
        this.codigo = codigo;
        solicitudes = new ArrayList<>();
    }

    /**
     * Agrega una nueva solicitud a la lista del estudiante.
     * 
     * Añade una solicitud académica (cambio de grupo, cambio de materia, etc.)
     * a la lista de solicitudes del estudiante. La solicitud debe estar
     * completamente inicializada antes de agregarla.
     */
    @Override
    public void addRequest(BaseRequest solicitud){
        this.solicitudes.add(solicitud);
    }

    @Override
    public void removeRequest(BaseRequest solicitud) throws SirhaException {
        if (!this.solicitudes.contains(solicitud)) {
            throw SirhaException.of(ErrorCodeSirha.REQUEST_NOT_FOUND, "La solicitud no existe en la lista del estudiante");
        }

        this.solicitudes.remove(solicitud);
    }

    /**
     * Obtiene el código estudiantil.
     * @return código único del estudiante
     */
    public String getCodigo() {
        return codigo;
    }
 
    /**
     * Establece el código estudiantil.
     * @param codigo nuevo código del estudiante. No debe ser null o vacío.
     * @throws SirhaException 
     * @throws IllegalStateException si el código es null o vacío
     */
    public void setCodigo(String codigo) throws SirhaException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El código no puede ser null o vacío");
        }
        this.codigo = codigo;
    }

    public AcademicProgress getAcademicProgress() {
        return academicProgress;
    }
    
    public void setAcademicProgress(AcademicProgress academicProgress) {
        this.academicProgress = academicProgress;
    }
    

    /**
     * Obtiene la lista de solicitudes del estudiante.
     * @return lista de solicitudes, nunca null (inicializada como lista vacía)
     */
    @Override
    public List<BaseRequest> getSolicitudes() {
        return new ArrayList<>(solicitudes);
    }
    
    @Override
    public Careers getCareer() {
        if (academicProgress == null) {
            return Careers.DEFAULT;
        }
        return academicProgress.getCareer();
    }
    
 
    /**
     * Compara este estudiante con otro objeto para determinar igualdad.
     * Dos estudiantes son iguales si tienen el mismo código.
     *
     * @param obj objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
       
        Student student = (Student) obj;
        return Objects.equals(codigo, student.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), codigo);
    }
    
    public List<SubjectDecorator> getSubjectsInProgress() {
        if (this.academicProgress == null) {
            return new ArrayList<>();
        }
        return academicProgress.getSubjectsInProgress();
    }
    public int getTotalSubjectsCount() {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getTotalSubjectsCount();
    }
    public int getSubjectsInProgressCount() {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getSubjectsInProgressCount();
    }
    
    public int getPassedSubjectsCount() {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getPassedSubjectsCount();
    }
   
    public int getFailedSubjectsCount() {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getFailedSubjectsCount();
    }
    
    public int getSubjectsNotTakenCount() {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getSubjectsNotTakenCount();
    }
    
    /**
     * Obtiene las materias de un semestre específico.
     *
     * @param semestre el semestre a consultar
     * @return lista de materias del semestre
     */
    public List<SubjectDecorator> getSubjectsBySemester(int semestre) {
        if (this.academicProgress == null) {
            return new ArrayList<>();
        }
        return academicProgress.getSubjectsBySemester(semestre);
    }
 
    /**
     * Calcula el total de créditos por estado del semáforo.
     *
     * @param color el color del semáforo a filtrar
     * @return total de créditos
     */
    public int getCreditsByColor(SemaforoColores color) {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getCreditsByColor(color);
    }

    public boolean hasScheduleConflictWith(Group nuevoGrupo) {
        if (nuevoGrupo == null || academicProgress == null) {
            return false;
        }

        for (SubjectDecorator materia : getSubjectsInProgress()) {
            try {
                Group g = materia.getGroup();
                if (g != null && g.conflictoConHorario(nuevoGrupo)) {
                    return true;
                }
            } catch (SirhaException e) {
                // materia sin grupo asignado -> no puede haber conflicto, saltar
            }
        }
        return false;
    }
    
    
    /**
     * Obtiene los horarios de las materias en curso (materias amarillas).
     * 
     * @return lista de horarios de Schedule
     */
    public List<Schedule> getCurrentSchedule() {
        if (academicProgress == null) {
            return new ArrayList<>();
        }

        List<Schedule> result = new ArrayList<>();
        for (SubjectDecorator materia : getSubjectsInProgress()) {
            try {
                List<Schedule> schedules = materia.getSchedules();
                if (schedules != null && !schedules.isEmpty()) {
                    result.addAll(schedules);
                }
            } catch (SirhaException e) {
                // materia sin grupo asignado -> no hay horarios, saltar
            }
        }
        return result;
    }
    
    /**
     * Verifica si el estudiante tiene alguna materia inscrita en el semestre actual.
     * 
     * @return true si tiene materias cursando, false en caso contrario
     */
    public boolean hasCoursesInProgress() {
        return getSubjectsInProgressCount() > 0;
    }
    
    /**
     * Obtiene el total de créditos que está cursando actualmente.
     * 
     * @return total de créditos en curso
     */
    public int getCreditsInProgress() {
        if (academicProgress == null) {
            return 0;
        }
        return getCreditsByColor(SemaforoColores.AMARILLO);
    }
    
    /**
     * Obtiene el semestre académico actual basado en las materias en curso.
     * 
     * @return semestre actual calculado
     */
    public int getCurrentSemester() {
        List<SubjectDecorator> cursando = getSubjectsInProgress();
        if (cursando.isEmpty()) {
            return 1; // Si no tiene materias en curso, asume primer semestre
        }
        
        double promedioSemestre = cursando.stream()
            .mapToInt(SubjectDecorator::getSemester)
            .filter(s -> s > 0)
            .average()
            .orElse(1.0);
            
        return (int) Math.ceil(promedioSemestre);
    }


    public AcademicPeriod getCurrentPeriod() {
        if (academicProgress == null) {
            return null;
        }
        return academicProgress.getCurrentAcademicPeriod();
    }
    public void setCurrentPeriod(AcademicPeriod currentPeriod) throws SirhaException {
        if (academicProgress == null) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El progreso académico no está inicializado");
        }
        if (!academicProgress.getSubjectsInProgress().isEmpty()) {
            throw SirhaException.of(ErrorCodeSirha.OPERATION_NOT_ALLOWED, "No se puede cambiar el período académico mientras hay materias en curso");
        }
        academicProgress.setCurrentAcademicPeriod(currentPeriod);
    }

    public boolean hasSubject(SubjectDecorator subject) {
        if (subject == null || academicProgress == null) {
            return false;
        }
        return hasSubject(subject.getSubject());
    }

    public boolean canEnroll(Subject subject) throws SirhaException {
        if (academicProgress == null) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El progreso académico no está inicializado");
        }

        // 1. Verificar que la materia esté en el semáforo (plan de estudios)
        if (!academicProgress.hasSubject(subject)) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "La materia no está en el plan de estudios del estudiante");
        }

        // 2. Verificar que la materia no esté ya inscrita
        if (!isSubjectNoCursada(subject.getName())) {
            throw SirhaException.of(ErrorCodeSirha.OPERATION_NOT_ALLOWED, "La materia ya está inscrita");
        }

        // 3. Verificar prerrequisitos
        if (subject.hasPrerequisites() && !subject.canEnroll(academicProgress)) {
            throw SirhaException.of(ErrorCodeSirha.OPERATION_NOT_ALLOWED, "No se cumplen los prerrequisitos para inscribir la materia");
        }

        return true;
    }
    
    public boolean canEnrollInGroup(Subject subject, Group group) throws SirhaException {
        // Primero verificar las validaciones básicas de la materia
        canEnroll(subject);
        
        // 4. Verificar que el grupo esté abierto
        if (!group.isOpen()) {
            throw SirhaException.of(ErrorCodeSirha.GROUP_CLOSED, "El grupo está cerrado");
        }
        
        // 6. Verificar período académico activo
        AcademicPeriod currentPeriod = getCurrentPeriod();
        if (currentPeriod == null || !currentPeriod.isActive() || !group.sameAcademicPeriod(currentPeriod)) {
            throw SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_VALID, "El período académico no es válido");
        }
        
        // 7. Verificar conflicto de horarios
        if (hasScheduleConflictWith(group)) {
            throw SirhaException.of(ErrorCodeSirha.SCHEDULE_CONFLICT, "Conflicto de horarios detectado");
        }
        
        // 9. Verificar límite de créditos por semestre FALTA IMPLEMENTAR AAAAAAAAAAAAAAAAAAA

        return true;
    }

    public void enrollSubject(Subject subject, Group group) throws SirhaException {
        if (!canEnrollInGroup(subject, group)) {
            throw SirhaException.of(ErrorCodeSirha.OPERATION_NOT_ALLOWED, "No se puede inscribir en la materia o grupo especificado");
        }
        group.enrollStudent(this);
        academicProgress.enrollSubjectInGroup(subject, group);

        //academicProgress.recordEnrollment(subject, group, currentPeriod); despues
    }

    public void enrollSubject(Subject subject, Group group, int semester) throws SirhaException {
        enrollSubject(subject, group);
        academicProgress.setSubjectSemester(subject.getName(), semester);

    }

    @Override
    public void unenrollSubject(Subject subject, Group group) throws SirhaException {
        AcademicPeriod currentPeriod = getCurrentPeriod();
        if (currentPeriod == null || !currentPeriod.isActive() || !group.sameAcademicPeriod(currentPeriod)) {
            throw SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_VALID, "El período académico no es válido para el grupo especificado");
        }
        
        academicProgress.unenrollSubjectFromGroup(subject.getName(), group);
        group.unenrollStudent(this);
        
    }

    @Override
    public void approveSubject(Subject subject) throws SirhaException {
        academicProgress.approveSubject(subject.getName());
    }

    @Override
    public void failSubject(Subject subject) throws SirhaException {
        academicProgress.failSubject(subject.getName());
    }

    /*
     * Verifica si el estudiante tiene una materia específica en su progreso académico.
     */
    public boolean hasSubject(Subject subject) {
        return academicProgress != null && academicProgress.hasSubject(subject);
    }

    public boolean validateChangeGroup(Subject subject, Group newGroup) throws SirhaException {

        if (!hasSubject(subject)) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "El estudiante no tiene la materia especificada");
        }
        if (!isSubjectCursando(subject.getName())) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_IN_PROGRESS, "La materia no está en curso");
        }

        if (!academicProgress.verifyChangeGroup(subject, newGroup)) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_STATE_TRANSITION, "No se puede cambiar al nuevo grupo especificado");
        }
        if (!subject.hasGroup(newGroup)) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_STATE_TRANSITION, "El nuevo grupo no pertenece a la materia especificada");
        }
        if (!newGroup.isOpen()) {
            throw SirhaException.of(ErrorCodeSirha.GROUP_CLOSED, "El nuevo grupo está cerrado");
        }
        if (hasScheduleConflictWith(newGroup)) {
            throw SirhaException.of(ErrorCodeSirha.SCHEDULE_CONFLICT, "Conflicto de horarios con el nuevo grupo");
        }
        AcademicPeriod currentPeriod = getCurrentPeriod();
        if (currentPeriod == null || !currentPeriod.isActive() || !newGroup.sameAcademicPeriod(currentPeriod)) {
            throw SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_VALID, "El período académico no es válido para el nuevo grupo");
        }
        return true;
    }

    public boolean validateChangeSubject(Subject oldSubject, Subject newSubject, Group newGroup) throws SirhaException {
        if (!hasSubject(oldSubject)) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "El estudiante no tiene la materia antigua especificada");
        }
        if (!isSubjectCursando(oldSubject.getName())) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_IN_PROGRESS, "La materia antigua no está en curso");
        }
        if (oldSubject.equals(newSubject)) {
            throw SirhaException.of(ErrorCodeSirha.SAME_SUBJECT, "La materia nueva es la misma que la antigua");
        }
        if (academicProgress.hasSubject(newSubject) && !isSubjectNoCursada(newSubject.getName())) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_ALREADY_ENROLLED, "El estudiante ya tiene la materia nueva inscrita o aprobada");
        }
        // ✅ CAMBIO: Usar hasSubject del academicProgress directamente
        if (!academicProgress.hasSubject(newSubject)) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_IN_STUDY_PLAN, "La materia nueva no está en el plan de estudios del estudiante");
        }
        if (newSubject.hasPrerequisites() && !newSubject.canEnroll(academicProgress)) {
            throw SirhaException.of(ErrorCodeSirha.PREREQUISITES_NOT_MET, "No se cumplen los prerrequisitos para inscribir la materia nueva");
        }
        if (!newSubject.hasGroup(newGroup)) {
            throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND, "El nuevo grupo no pertenece a la materia nueva especificada");
        }
        if (!newGroup.isOpen()) {
            throw SirhaException.of(ErrorCodeSirha.GROUP_CLOSED, "El nuevo grupo está cerrado");
        }
        if (hasScheduleConflictWith(newGroup)) {
            throw SirhaException.of(ErrorCodeSirha.SCHEDULE_CONFLICT, "Conflicto de horarios con el nuevo grupo");
        }
        AcademicPeriod currentPeriod = getCurrentPeriod();
        if (currentPeriod == null || !currentPeriod.isActive() || !newGroup.sameAcademicPeriod(currentPeriod)) {
            throw SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_VALID, "El período académico no es válido para el nuevo grupo");
        }
        return true;
    }

    public CambioGrupo createGroupChangeRequest(Subject subject, Group newGroup) throws SirhaException {
        validateChangeGroup(subject, newGroup);
        CambioGrupo solicitud = new CambioGrupo(this, subject, newGroup, getCurrentPeriod());
        addRequest(solicitud);
        return solicitud;
    }
    @Override
    public CambioMateria createSubjectChangeRequest(Subject oldSubject, Subject newSubject, Group newGroup) throws SirhaException {
        validateChangeSubject(oldSubject, newSubject, newGroup) ;
        CambioMateria solicitud = new CambioMateria(this, oldSubject, newSubject, newGroup, getCurrentPeriod());
        addRequest(solicitud);
        return solicitud;
        
    }

    public Map<AcademicPeriod, List<Schedule>> getAllSchedules() {
        if (academicProgress == null) {
            return Map.of();
            
        }
        return academicProgress.getAllSchedules();
    }

    @Override
    public List<Schedule> getScheduleForPeriod(AcademicPeriod period) {
        return getAllSchedules().getOrDefault(period, new ArrayList<>());
    }

    /**
     * Representación en string del estudiante.
     * @return string con información básica del estudiante
     */
    public String toString() {
        return String.format("Student{id='%s', username='%s', codigo='%s'}",
                            getId(), getUsername(), codigo);
    }

    @Override
    public Map<SemaforoColores, List<SubjectDecoratorDTO>> getAcademicPensum() {
        if (academicProgress == null) {
            return Map.of(
                SemaforoColores.VERDE, new ArrayList<>(),
                SemaforoColores.AMARILLO, new ArrayList<>(),
                SemaforoColores.ROJO, new ArrayList<>(),
                SemaforoColores.GRIS, new ArrayList<>()
            );
        }
        return academicProgress.getAcademicPensum();
    }


    @Override
    public Map<SemaforoColores, Double> getPercentageByColor() {
        if (academicProgress == null) {
            return new EnumMap<>(SemaforoColores.class);
        }
        return academicProgress.getPercentageByColor();
    }

    @Override
    public StudentDTO getStudentBasicInfo() {
        return new StudentDTO(
            getId(),
            getUsername(),
            getEmail(),
            getCodigo(),
            getCareer()
        );
    }

    @Override
    public double getOverallProgressPercentage() {
        if (academicProgress == null) {
            return 0.0;
        }
        return academicProgress.getOverallProgressPercentage();
    }

    public double getAcademicSuccessRate(){
        if (academicProgress == null) {
            return 0.0;
        }
        return academicProgress.getAcademicSuccessRate();
    }
    public double getCompletedCreditsPercentage(){
        if (academicProgress == null) {
            return 0.0;
        }
        return academicProgress.getCompletedCreditsPercentage();
    }

    public AcademicIndicatorsDTO getAcademicIndicators(){
        if (academicProgress == null) {
            return null;
        }
        return academicProgress.getAcademicIndicators();
    }

    @Override
    public StudentReportDTO generateCompleteReport() {
        if (academicProgress == null) {
            return null;
        }
        return new StudentReportDTO(
            getStudentBasicInfo(),
            getAcademicIndicators(),
            getRequestApprovalRate()
        );
    }

    /**
     * Obtiene un resumen del progreso académico del estudiante.
     *
     * @return string con el resumen
     */
    @Override
    public String getAcademicSummary() {
        int aprobadas = getPassedSubjectsCount();
        int cursando = getSubjectsInProgressCount();
        int reprobadas = getFailedSubjectsCount();
        int noCursadas = getSubjectsNotTakenCount();
        int creditosAprobados = getCreditsByColor(SemaforoColores.VERDE);
        int creditosCursando = getCreditsByColor(SemaforoColores.AMARILLO);
 
        return String.format(
            "Estudiante: %s del programa %s - Aprobadas: %d (%d créditos) | Cursando: %d (%d créditos) | Reprobadas: %d | No Cursadas: %d - Progreso: %.2f%%",
            codigo, getCareer().getDisplayName(), aprobadas, creditosAprobados, cursando, creditosCursando, reprobadas, noCursadas, getOverallProgressPercentage()
        );
    }

    @Override
    public double getApprovalRequestPercentage() {
        return getRequestApprovalRate().getApprovalRatePercentage();
    }
    @Override
    public double getRejectionRequestPercentage(){
        return getRequestApprovalRate().getRejectionRatePercentage();
    }
    @Override
    public double getPendingRequestPercentage(){
        return getRequestApprovalRate().getPendingRatePercentage();
    }
    @Override
    public double getInReviewRequestPercentage(){
        return getRequestApprovalRate().getInReviewRatePercentage();
    }

    @Override
    public int getTotalRequestsMade(){
        return solicitudes.size();
    }

    @Override
    public boolean hasActiveRequests() {
        return solicitudes.stream().anyMatch(s -> s.getActualState() == RequestStateEnum.PENDIENTE || s.getActualState() == RequestStateEnum.EN_REVISION);
    }


    @Override
    public RequestApprovalRateDTO getRequestApprovalRate() {
        if (solicitudes.isEmpty()) {
            return new RequestApprovalRateDTO(0, 0, 0, 0, 0);
        }
        int totalRequests = getTotalRequestsMade();
        int approvedRequests = getTotalApprovedRequests();
        int rejectedRequests = getTotalRejectedRequests();
        int pendingRequests = getTotalPendingRequests();
        int inReviewRequests = getTotalInReviewRequests();

        return new RequestApprovalRateDTO(
            totalRequests,
            approvedRequests,
            rejectedRequests,
            pendingRequests,
            inReviewRequests
        );
    }

    @Override
    public int getTotalApprovedRequests() {
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.APROBADA).count();
    }

    @Override
    public int getTotalRejectedRequests() {
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.RECHAZADA).count();
    }

    @Override
    public int getTotalPendingRequests() {
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.PENDIENTE).count();
    }

    @Override
    public int getTotalInReviewRequests() {
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.EN_REVISION).count();
    }
    @Override
    public int getSubjectsByColorCount(SemaforoColores color) {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getSubjectsByColorCount(color);
    }
    
    @Override
    public BaseRequest getRequestById(String requestId) {
        return solicitudes.stream()
            .filter(s -> s.getId().equals(requestId))
            .findFirst()
            .orElse(null);
    }
    
    /*
     * Devuelve el historial de solicitudes resueltas: aprobadas o rechazadas.
     */
    @Override
    public List<BaseRequest> getRequestsHistory() {
        return solicitudes.stream()
            .filter(s -> s.getActualState() == RequestStateEnum.APROBADA || s.getActualState() == RequestStateEnum.RECHAZADA)
            .toList();
    }

    @Override
    public boolean isSubjectApproved(String subject) {
        if (academicProgress == null) {
            return false;
        }
        return academicProgress.isSubjectApproved(subject);
    }

    @Override
    public boolean isSubjectCursando(String subject) {
        if (academicProgress == null) {
            return false;
        }
        return academicProgress.isSubjectCursando(subject);
    }

    @Override
    public boolean isSubjectReprobada(String subject) {
        if (academicProgress == null) {
            return false;
        }
        return academicProgress.isSubjectReprobada(subject);
    }

    @Override
    public boolean isSubjectNoCursada(String subject) {
        if (academicProgress == null) {
            return false;
        }
        return academicProgress.isSubjectNoCursada(subject);
    }
    /**
     * Obtiene el ID del plan de estudios del estudiante.
     * @return ID del plan de estudios, o null si no tiene progreso académico
     */
    public String getStudyPlanId() {
        if (academicProgress == null) {
            return null;
        }
        return academicProgress.getStudyPlanId();
    }

    /**
     * Obtiene información resumida del plan de estudios.
     * @return String con ID y carrera del plan
     */
    public String getStudyPlanInfo() {
        if (academicProgress == null) {
            return "Sin plan de estudios asignado";
        }
        return String.format("Plan: %s - Carrera: %s - Créditos totales: %d",
                academicProgress.getStudyPlanId(),
                academicProgress.getCareer().getDisplayName(),
                academicProgress.getCreditsStudyPlan()
        );
    }

}

