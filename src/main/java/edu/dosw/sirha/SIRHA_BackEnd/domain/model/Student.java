package edu.dosw.sirha.SIRHA_BackEnd.domain.model;
 
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
 
import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.AcademicOperations;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.AcademicProgress;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.AcademicProgressViewer;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.PrerequisiteRule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Request;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestProcess;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.ScheduleManager;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SolicitudFactory;

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
    private List<RequestProcess> solicitudes;
    private AcademicPeriod currentPeriod;
 
    public Student() {
        super();
    }
 
    public Student(int id, String username, String email, String passwordHash, String codigo) {
        super(id, username, email, passwordHash);
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
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalStateException("El código de estudiante no puede ser null o vacío");
        }
        this.codigo = codigo;
    }

    /**
     * Agrega una nueva solicitud a la lista del estudiante.
     * 
     * Añade una solicitud académica (cambio de grupo, cambio de materia, etc.)
     * a la lista de solicitudes del estudiante. La solicitud debe estar
     * completamente inicializada antes de agregarla.
     */
    public void agregarSolicitud(RequestProcess solicitud) {
        if (solicitud == null) {
            throw new IllegalStateException("La solicitud no puede ser null");
        }
        
        if (this.solicitudes == null) {
            this.solicitudes = new ArrayList<>();
        }
        
        this.solicitudes.add(solicitud);
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
    public List<RequestProcess> getSolicitudes() {
        if (solicitudes == null) {
            solicitudes = new ArrayList<>();
        }
        return solicitudes;
    }

    /**
     * Establece la lista completa de solicitudes del estudiante.
     * @param solicitudes nueva lista de solicitudes. Si es null, se inicializa como lista vacía.
     */
    public void setSolicitudes(List<RequestProcess> solicitudes) {
        this.solicitudes = solicitudes != null ? solicitudes : new ArrayList<>();
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
    
    public List<SubjectDecorator> getMateriasCursando() {
        if (this.academicProgress == null) {
            return new ArrayList<>();
        }
        return academicProgress.getMateriasCursando();
    }
    public int getMateriasCursandoCount() {
        if (this.academicProgress == null) {
            return 0;
        }
        return academicProgress.getMateriasCursandoCount();
    }
    
    public int getMateriasAprobadasCount() {
        if (this.academicProgress == null) {
            return 0;
        }
        return academicProgress.getMateriasAprobadasCount();
    }
   
    public int getMateriasReprobadasCount() {
        if (this.academicProgress == null) {
            return 0;
        }
        return academicProgress.getMateriasReprobadasCount();
    }
    
    public int getMateriasNoCursadasCount() {
        if (this.academicProgress == null) {
            return 0;
        }
        return academicProgress.getMateriasNoCursadasCount();
    }
    
    /**
     * Obtiene las materias de un semestre específico.
     *
     * @param semestre el semestre a consultar
     * @return lista de materias del semestre
     */
    public List<SubjectDecorator> getMateriasPorSemestre(int semestre) {
        if (this.academicProgress == null) {
            return new ArrayList<>();
        }
        return academicProgress.getMateriasPorSemestre(semestre);
    }
 
    /**
     * Calcula el total de créditos por estado del semáforo.
     *
     * @param color el color del semáforo a filtrar
     * @return total de créditos
     */
    public int getCreditosPorColor(SemaforoColores color) {
        if (this.academicProgress == null) {
            return 0;
        }
        return academicProgress.getCreditosPorColor(color);
    }
 
   
    /**
     * Obtiene un resumen del progreso académico del estudiante.
     *
     * @return string con el resumen
     */
    public String getResumenAcademico() {
        int aprobadas = getMateriasAprobadasCount();
        int cursando = getMateriasCursandoCount();
        int reprobadas = getMateriasReprobadasCount();
        int noCursadas = getMateriasNoCursadasCount();
        int creditosAprobados = getCreditosPorColor(SemaforoColores.VERDE);
        int creditosCursando = getCreditosPorColor(SemaforoColores.AMARILLO);
 
        return String.format(
            "Estudiante: %s - Aprobadas: %d (%d créditos) | Cursando: %d (%d créditos) | Reprobadas: %d | No Cursadas: %d",
            codigo, aprobadas, creditosAprobados, cursando, creditosCursando, reprobadas, noCursadas
        );
    }
 
 
    public boolean tieneConflictoConHorario(Group nuevoGrupo) {
        if (nuevoGrupo == null || academicProgress == null) {
            return false;
        }
 
        return getMateriasCursando().stream()
            .filter(materia -> materia.getGroup() != null)
            .anyMatch(materia -> materia.getGroup().conflictoConHorario(nuevoGrupo));
    }
    
    /**
     * Obtiene todos los semestres únicos de las materias del estudiante.
     * 
     * @return conjunto de semestres ordenados
     */
    public List<Integer> getSemestresHistoricos() {
        if (academicProgress == null) {
            return new ArrayList<>();
        }
        
        return academicProgress.getSubjects().stream()
            .map(SubjectDecorator::getSemestre)
            .filter(s -> s > 0)
            .distinct()
            .sorted()
            .toList();
    }
    
    /**
     * Obtiene los horarios de las materias en curso (materias amarillas).
     * 
     * @return lista de horarios de Schedule
     */
    public List<Schedule> getHorariosActuales() {
        if (academicProgress == null) {
            return new ArrayList<>();
        }
        
        return getMateriasCursando().stream()
            .filter(materia -> materia.getGroup() != null)
            .flatMap(materia -> materia.getGroup().getSchedules().stream())
            .toList();
    }
    
    /**
     * Verifica si el estudiante tiene alguna materia inscrita en el semestre actual.
     * 
     * @return true si tiene materias cursando, false en caso contrario
     */
    public boolean tieneMateriasEnCurso() {
        return getMateriasCursandoCount() > 0;
    }
    
    /**
     * Obtiene el total de créditos que está cursando actualmente.
     * 
     * @return total de créditos en curso
     */
    public int getCreditosEnCurso() {
        return getCreditosPorColor(SemaforoColores.AMARILLO);
    }
    
    /**
     * Obtiene el semestre académico actual basado en las materias en curso.
     * 
     * @return semestre actual calculado
     */
    public int getSemestreActual() {
        List<SubjectDecorator> cursando = getMateriasCursando();
        if (cursando.isEmpty()) {
            return 1; // Si no tiene materias en curso, asume primer semestre
        }
        
        double promedioSemestre = cursando.stream()
            .mapToInt(SubjectDecorator::getSemestre)
            .filter(s -> s > 0)
            .average()
            .orElse(1.0);
            
        return (int) Math.ceil(promedioSemestre);
    }


    public AcademicPeriod getCurrentPeriod() {
        return currentPeriod;
    }
    public void setCurrentPeriod(AcademicPeriod currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public boolean hasSubject(SubjectDecorator subject) {
        if (subject == null || academicProgress == null) {
            return false;
        }
        return hasSubject(subject.getSubject());
    }


    /**
     * Representación en string del estudiante.
     * @return string con información básica del estudiante
     */
    @Override
    public String toString() {
        return String.format("Student{id='%s', username='%s', codigo='%s'}",
                            getId(), getUsername(), codigo);
    }

    @Override
    public boolean canEnroll(Subject subject) {
        // 1. Verificar que la materia esté en el plan de estudios
        if (academicProgress.getStudyPlan() == null || !academicProgress.getStudyPlan().hasSubject(subject)) {
            throw new IllegalStateException("La materia no está en el plan de estudios del estudiante");
        }
        
        // 2. Verificar que la materia no esté ya inscrita
        if (academicProgress == null || !academicProgress.isSubjectNoCursada(subject)) {
            throw new IllegalStateException("La materia ya está inscrita");
        }
        
        // 3. Verificar prerrequisitos
        if (subject.hasPrerequisites() && !subject.canEnroll(academicProgress)) {
                throw new IllegalStateException("No se cumplen los prerrequisitos para inscribir la materia");
            }
        
        return true;
    }
    @Override
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
        if (currentPeriod == null || !currentPeriod.isActive() || !group.sameAcademicPeriod(currentPeriod)) {
            throw new IllegalStateException("El período académico no es válido");
        }
        
        // 7. Verificar conflicto de horarios
        if (tieneConflictoConHorario(group)) {
            throw new IllegalStateException("Conflicto de horarios detectado");
        }
        
        // 9. Verificar límite de créditos por semestre FALTA IMPLEMENTAR AAAAAAAAAAAAAAAAAAA

        return true;
    }

    @Override
    public void enrollSubject(Subject subject, Group group) {
        if (!canEnrollInGroup(subject, group)) {
            throw new IllegalStateException("No se puede inscribir en la materia o grupo especificado");
        }
        
        
        group.enrollStudent(this);
        academicProgress.enrollSubjectInGroup(subject, group);
        //academicProgress.recordEnrollment(subject, group, currentPeriod); despues
        
        /*try {   
        } catch (Exception e) {
            throw new RuntimeException("Error durante la inscripción: " + e.getMessage(), e);
            //rollback si es necesario
        }*/
    }

    @Override
    public void unenrollSubject(Subject subject, Group group) {
        throw new UnsupportedOperationException("Método no implementado aún"); //:C
    }

    /*
     * Verifica si el estudiante tiene una materia específica en su progreso académico.
     */
    @Override
    public boolean hasSubject(Subject subject) {
        return academicProgress != null && academicProgress.hasSubject(subject);
    }
    
}
 
