package edu.dosw.sirha.SIRHA_BackEnd.domain.model;
 
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
 
import org.springframework.data.mongodb.core.mapping.Document;
 
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
 
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
 * Características principales:
 * - Hereda propiedades básicas de User (id, username, password, rol)
 * - Tiene un código estudiantil único en el sistema
 * - Posee un plan de estudios asignado
 * - Mantiene un semáforo de estado académico por materia
 * - Gestiona una lista de solicitudes académicas
 *
 * @see User
 * @see StudyPlan
 * @see Semaforo
 * @see BaseRequest
 */
@Document(collection = "students")
public class Student extends User {
    private String codigo;
    private StudyPlan planGeneral;
    private Semaforo semaforo;
    private List<BaseRequest> solicitudes;
 
 
    public Student() {
        super();
    }
 
    /**
     * Constructor principal para crear un nuevo estudiante.
     *
     * Inicializa un estudiante con los datos básicos requeridos.
     * La lista de solicitudes se inicializa como lista vacía.
     * El plan de estudios y semáforo deben ser asignados posteriormente.
     *
     * @param id identificador único del estudiante en el sistema.
     *          No debe ser null o vacío.
     * @param username nombre de usuario para acceso al sistema.
     *                Debe ser único. No debe ser null o vacío.
     * @param passwordHash hash de la contraseña del estudiante.
     *                    Debe estar previamente hasheado por seguridad.
     * @param rol rol del usuario en el sistema (típicamente "ESTUDIANTE").
     *           No debe ser null.
     * @param codigo código estudiantil único.
     *              No debe ser null o vacío.
     *
     * @throws IllegalArgumentException si algún parámetro requerido es null o vacío
     */
    public Student(String id, String username, String email, String passwordHash, String codigo) {
        super(id, username, email, passwordHash);
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de estudiante no puede ser null o vacío");
        }
        this.codigo = codigo;
        solicitudes = new ArrayList<>();
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
 
    /**
     * Obtiene el semáforo académico del estudiante.
     * @return semáforo académico, puede ser null si no se ha inicializado
     */
    public Semaforo getSemaforo() {
        return semaforo;
    }
 
    /**
     * Establece el semáforo académico del estudiante.
     * @param semaforo nuevo semáforo a asignar
     */
    public void setSemaforo(Semaforo semaforo) {
        this.semaforo = semaforo;
    }
 
    /**
     * Obtiene la lista de solicitudes del estudiante.
     * @return lista de solicitudes, nunca null (inicializada como lista vacía)
     */
    public List<BaseRequest> getSolicitudes() {
        if (solicitudes == null) {
            solicitudes = new ArrayList<>();
        }
        return solicitudes;
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
 
    public List<SubjectDecorator> getMateriasCursando() {
        if (this.semaforo == null) {
            return new ArrayList<>();
        }
        return semaforo.getMateriasCursando();
    }
    public int getMateriasCursandoCount() {
        if (this.semaforo == null) {
            return 0;
        }
        return semaforo.getMateriasCursandoCount();
    }
    public int getMateriasAprobadasCount() {
        if (this.semaforo == null) {
            return 0;
        }
        return semaforo.getMateriasAprobadasCount();
    }
   
    public int getMateriasReprobadasCount() {
        if (this.semaforo == null) {
            return 0;
        }
        return semaforo.getMateriasReprobadasCount();
    }
    public int getMateriasNoCursadasCount() {
        if (this.semaforo == null) {
            return 0;
        }
        return semaforo.getMateriasNoCursadasCount();
    }
    /**
     * Representación en string del estudiante.
     * @return string con información básica del estudiante
     */
    @Override
    public String toString() {
        return String.format("Student{id='%s', username='%s', codigo='%s', numSolicitudes=%d}",
                            getId(), getUsername(), codigo,
                            solicitudes != null ? solicitudes.size() : 0);
    }
    /**
     * Obtiene las materias de un semestre específico.
     *
     * @param semestre el semestre a consultar
     * @return lista de materias del semestre
     */
    public List<SubjectDecorator> getMateriasPorSemestre(int semestre) {
        if (this.semaforo == null) {
            return new ArrayList<>();
        }
        return semaforo.getMateriasPorSemestre(semestre);
    }
 
    /**
     * Calcula el total de créditos por estado del semáforo.
     *
     * @param color el color del semáforo a filtrar
     * @return total de créditos
     */
    public int getCreditosPorColor(SemaforoColores color) {
        if (this.semaforo == null) {
            return 0;
        }
        return semaforo.getCreditosPorColor(color);
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
        if (nuevoGrupo == null || semaforo == null) {
            return false;
        }
 
        return getMateriasCursando().stream()
            .filter(materia -> materia.getGroup() != null)
            .anyMatch(materia -> materia.getGroup().conflictoConHorario(nuevoGrupo));
    }
   
   
}
 