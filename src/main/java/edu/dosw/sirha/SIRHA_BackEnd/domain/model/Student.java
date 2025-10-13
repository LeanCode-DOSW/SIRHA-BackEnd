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
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicOperations;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgressViewer;
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
public class Student extends User implements SolicitudFactory, ScheduleManager, AcademicProgressViewer, AcademicOperations {
    private String codigo;
    private AcademicProgress academicProgress;
    private List<BaseRequest> solicitudes;
    
 
    public Student() {
        solicitudes = new ArrayList<>();
    }

    public Student(String id, String username, String email, String passwordHash, String codigo) {
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
    public Student(String username, String email, String passwordHash, String codigo) {
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
    public void addRequest(BaseRequest solicitud) {
        if (solicitud == null) {
            throw new IllegalStateException("La solicitud no puede ser null");
        }
        
        if (this.solicitudes == null) {
            this.solicitudes = new ArrayList<>();
        }
        
        this.solicitudes.add(solicitud);
    }

    public void removeRequest(BaseRequest solicitud) {
        if (solicitud == null) {
            throw new IllegalStateException("La solicitud no puede ser null");
        }
        
        if (this.solicitudes == null || !this.solicitudes.contains(solicitud)) {
            throw new IllegalStateException("La solicitud no existe en la lista del estudiante");
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
     * @throws IllegalStateException si el código es null o vacío
     */
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalStateException("El código no puede ser null o vacío");
        }
        this.codigo = codigo;
    }
 
    /**
     * Obtiene el plan de estudios asignado.
     * @return plan de estudios del estudiante, puede ser null si no se ha asignado
     */
    public StudyPlan getPlanGeneral() {
        return academicProgress.getStudyPlan();
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
    public List<BaseRequest> getSolicitudes() {
        if (solicitudes == null) {
            solicitudes = new ArrayList<>();
        }
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
       
        Student student = (Student) obj;
        return Objects.equals(codigo, student.codigo);
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

        return getSubjectsInProgress().stream()
            .filter(materia -> materia.getGroup() != null)
            .anyMatch(materia -> materia.getGroup().conflictoConHorario(nuevoGrupo));
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
        return getSubjectsInProgress().stream()
            .filter(materia -> materia.getGroup() != null)
            .flatMap(materia -> materia.getGroup().getSchedules().stream())
            .toList();
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
    public void setCurrentPeriod(AcademicPeriod currentPeriod) {
        if (academicProgress == null) {
            throw new IllegalStateException("El progreso académico no está inicializado");
        }
        if (!academicProgress.getSubjectsInProgress().isEmpty()) {
            throw new IllegalStateException("No se puede cambiar el período académico mientras hay materias en curso");
        }
        academicProgress.setCurrentAcademicPeriod(currentPeriod);
    }

    public boolean hasSubject(SubjectDecorator subject) {
        if (subject == null || academicProgress == null) {
            return false;
        }
        return hasSubject(subject.getSubject());
    }

    public boolean canEnroll(Subject subject) {
        // 1. Verificar que la materia esté en el plan de estudios
        if (academicProgress.getStudyPlan() == null || !academicProgress.getStudyPlan().hasSubject(subject)) {
            throw new IllegalStateException("La materia no está en el plan de estudios del estudiante");
        }
        
        // 2. Verificar que la materia no esté ya inscrita

        if (!academicProgress.isSubjectNoCursada(subject)) {
            throw new IllegalStateException("La materia ya está inscrita");
        }
        
        // 3. Verificar prerrequisitos
        if (subject.hasPrerequisites() && !subject.canEnroll(academicProgress)) {
                throw new IllegalStateException("No se cumplen los prerrequisitos para inscribir la materia");
            }
        
        return true;
    }
    public boolean canEnrollInGroup(Subject subject, Group group) {
        // Primero verificar las validaciones básicas de la materia
        if (!canEnroll(subject)) {
            throw new IllegalStateException("La materia no se puede inscribir");
        }
        
        // 4. Verificar que el grupo esté abierto
        if (!group.isOpen()) {
            throw new IllegalStateException("El grupo está cerrado");
        }
        
        // 6. Verificar período académico activo
        AcademicPeriod currentPeriod = getCurrentPeriod();
        if (currentPeriod == null || !currentPeriod.isActive() || !group.sameAcademicPeriod(currentPeriod)) {
            throw new IllegalStateException("El período académico no es válido");
        }
        
        // 7. Verificar conflicto de horarios
        if (hasScheduleConflictWith(group)) {
            throw new IllegalStateException("Conflicto de horarios detectado");
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
            throw new IllegalStateException("El período académico no es válido para el grupo especificado");
        }

        group.unenrollStudent(this);
        academicProgress.unenrollSubjectFromGroup(subject.getName(), group);
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

    public boolean validateChangeGroup(Subject subject, Group newGroup){

        if (!hasSubject(subject)) {
            throw new IllegalStateException("El estudiante no tiene la materia especificada");
        }
        if (!academicProgress.isSubjectCursando(subject)) {
            throw new IllegalStateException("La materia no está en curso");
        }

        if (!academicProgress.verifyChangeGroup(subject, newGroup)) {
            throw new IllegalStateException("El nuevo grupo es el mismo que el actual");
        }
        if (!subject.hasGroup(newGroup)) {
            throw new IllegalStateException("El nuevo grupo no pertenece a la materia especificada");
        }
        if (!newGroup.isOpen()) {
            throw new IllegalStateException("El nuevo grupo está cerrado");
        }
        if (hasScheduleConflictWith(newGroup)) {
            throw new IllegalStateException("Conflicto de horarios con el nuevo grupo");
        }
        AcademicPeriod currentPeriod = getCurrentPeriod();
        if (currentPeriod == null || !currentPeriod.isActive() || !newGroup.sameAcademicPeriod(currentPeriod)) {
            throw new IllegalStateException("El período académico no es válido para el nuevo grupo");
        }
        return true;
    }

    public boolean validateChangeSubject(Subject oldSubject, Subject newSubject, Group newGroup) {
        if (!hasSubject(oldSubject)) {
            throw new IllegalStateException("El estudiante no tiene la materia antigua especificada");
        }
        if (!academicProgress.isSubjectCursando(oldSubject)) {
            throw new IllegalStateException("La materia antigua no está en curso");
        }
        if (oldSubject.equals(newSubject)) {
            throw new IllegalStateException("La materia nueva es la misma que la antigua");
        }
        if (academicProgress.hasSubject(newSubject) && !academicProgress.isSubjectNoCursada(newSubject)) {
            throw new IllegalStateException("El estudiante ya tiene la materia nueva inscrita o aprobada");
        }
        if (!academicProgress.getStudyPlan().hasSubject(newSubject)) {
            throw new IllegalStateException("La materia nueva no está en el plan de estudios del estudiante");
        }
        if (newSubject.hasPrerequisites() && !newSubject.canEnroll(academicProgress)) {
            throw new IllegalStateException("No se cumplen los prerrequisitos para inscribir la materia nueva");
        }
        if (!newSubject.hasGroup(newGroup)) {
            throw new IllegalStateException("El nuevo grupo no pertenece a la materia nueva especificada");
        }
        if (!newGroup.isOpen()) {
            throw new IllegalStateException("El nuevo grupo está cerrado");
        }
        if (hasScheduleConflictWith(newGroup)) {
            throw new IllegalStateException("Conflicto de horarios con el nuevo grupo");
        }
        AcademicPeriod currentPeriod = getCurrentPeriod();
        if (currentPeriod == null || !currentPeriod.isActive() || !newGroup.sameAcademicPeriod(currentPeriod)) {
            throw new IllegalStateException("El período académico no es válido para el nuevo grupo");
        }
        return true;
    }

    public CambioGrupo createGroupChangeRequest(Subject subject, Group newGroup) {
        validateChangeGroup(subject, newGroup);
        CambioGrupo solicitud = new CambioGrupo(this, subject, newGroup, getCurrentPeriod());
        addRequest(solicitud);
        return solicitud;
    }
    public CambioMateria createSubjectChangeRequest(Subject oldSubject, Subject newSubject, Group newGroup) {
        validateChangeSubject(oldSubject, newSubject, newGroup);
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
        if (solicitudes == null || solicitudes.isEmpty()) {
            return 0.0;
        }
        return getRequestApprovalRate().getApprovalRatePercentage();
    }
    @Override
    public double getRejectionRequestPercentage(){
        if (solicitudes == null || solicitudes.isEmpty()) {
            return 0.0;
        }
        return getRequestApprovalRate().getRejectionRatePercentage();
    }
    @Override
    public double getPendingRequestPercentage(){
        if (solicitudes == null || solicitudes.isEmpty()) {
            return 0.0;
        }
        return getRequestApprovalRate().getPendingRatePercentage();
    }
    @Override
    public double getInReviewRequestPercentage(){
        if (solicitudes == null || solicitudes.isEmpty()) {
            return 0.0;
        }
        return getRequestApprovalRate().getInReviewRatePercentage();
    }

    @Override
    public int getTotalRequestsMade(){
        if (solicitudes == null) {
            return 0;
        }
        return solicitudes.size();
    }
    @Override
    public boolean hasActiveRequests() {
        if (solicitudes == null) {
            return false;
        }
        return solicitudes.stream().anyMatch(s -> s.getActualState() == RequestStateEnum.PENDIENTE || s.getActualState() == RequestStateEnum.EN_REVISION);
    }


    @Override
    public RequestApprovalRateDTO getRequestApprovalRate() {
        if (solicitudes == null || solicitudes.isEmpty()) {
            return new RequestApprovalRateDTO(0, 
            0, 0,
            0, 0);
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
        if (solicitudes == null) {
            return 0;
        }
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.APROBADA).count();
    }

    @Override
    public int getTotalRejectedRequests() {
        if (solicitudes == null) {
            return 0;
        }
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.RECHAZADA).count();
    }

    @Override
    public int getTotalPendingRequests() {
        if (solicitudes == null) {
            return 0;
        }
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.PENDIENTE).count();
    }

    @Override
    public int getTotalInReviewRequests() {
        if (solicitudes == null) {
            return 0;
        }
        return (int) solicitudes.stream().filter(s -> s.getActualState() == RequestStateEnum.EN_REVISION).count();
    }
    @Override
    public int getSubjectsByColorCount(SemaforoColores color) {
        if (academicProgress == null) {
            return 0;
        }
        return academicProgress.getSubjectsByColorCount(color);
    }
    
    public BaseRequest getRequestById(String requestId) {
        if (solicitudes == null || solicitudes.isEmpty()) {
            return null;
        }
        return solicitudes.stream()
            .filter(s -> s.getId().equals(requestId))
            .findFirst()
            .orElse(null);
    }
    /*
     * Devuelve el historial de solicitudes resueltas: aprobadas o rechazadas.
     */
    public List<BaseRequest> getRequestsHistory() {
        if (solicitudes == null) {
            return new ArrayList<>();
        }
        return solicitudes.stream()
            .filter(s -> s.getActualState() == RequestStateEnum.APROBADA || s.getActualState() == RequestStateEnum.RECHAZADA)
            .toList();
    }

}
 
