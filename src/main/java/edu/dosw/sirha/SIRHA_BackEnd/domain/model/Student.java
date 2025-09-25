package edu.dosw.sirha.SIRHA_BackEnd.domain.model;
 
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
 
import org.springframework.data.mongodb.core.mapping.Document;
 
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.*;

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
public class Student extends User {
    private String codigo;
    private StudyPlan planGeneral;
    private List<BaseRequest> solicitudes;
    private AcademicProgress academicProgress;
 
 
    public Student() {
        super();
    }
 
    public Student(String id, String username, String email, String passwordHash, String codigo) {
        super(id, username, email, passwordHash);
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de estudiante no puede ser null o vacío");
        }
        this.codigo = codigo;
    }
    
    public List<BaseRequest> getSolicitudes(){
        return solicitudes;
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
     * @throws IllegalArgumentException si el código es null o vacío
     */
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede ser null o vacío");
        }
        this.codigo = codigo;
    }
 
    /**
     * Obtiene el plan de estudios asignado.
     * @return plan de estudios del estudiante, puede ser null si no se ha asignado
     */
    public StudyPlan getPlanGeneral() {
        return planGeneral;
    }
 
    /**
     * Establece el plan de estudios del estudiante.
     * @param planGeneral nuevo plan de estudios a asignar
     */
    public void setPlanGeneral(StudyPlan planGeneral) {
        this.planGeneral = planGeneral;
    }
 

    public AcademicProgress getAcademicProgress() {
        return academicProgress;
    }
    

    public AcademicProgress getSemaforo() {
        return  academicProgress;
    }

    public void setAcademicProgress(AcademicProgress academicProgress) {
        this.academicProgress = academicProgress;
    }
    

    public void setSemaforo(Semaforo semaforo) {
        this.academicProgress = semaforo;
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
     * Representación en string del estudiante.
     * @return string con información básica del estudiante
     */
    @Override
    public String toString() {
        return String.format("Student{id='%s', username='%s', codigo='%s'}",
                            getId(), getUsername(), codigo);
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
            .flatMap(materia -> materia.getGroup().getHorarios().stream())
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
     * Calcula el semestre promedio de las materias que está cursando.
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
   
}
 