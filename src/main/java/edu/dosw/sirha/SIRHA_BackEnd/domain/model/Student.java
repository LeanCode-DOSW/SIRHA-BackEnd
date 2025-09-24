package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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
public class Student extends User {
    private String codigo;
    private StudyPlan planGeneral;
    private Semaforo semaforo;
    private List<BaseRequest> solicitudes;

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
    }

    public SemaforoColores getColorMateria(String Materia) {
        return semaforo.getColor(Materia);
    }

    /**
     * Agrega una nueva solicitud a la lista del estudiante.
     * 
     * Añade una solicitud académica (cambio de grupo, cambio de materia, etc.)
     * a la lista de solicitudes del estudiante. La solicitud debe estar
     * completamente inicializada antes de agregarla.
     * 
     * @param solicitud la solicitud a agregar. No debe ser null.
     * @throws IllegalArgumentException si la solicitud es null
     */
    public void agregarSolicitud(BaseRequest solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null");
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
     * Establece la lista completa de solicitudes del estudiante.
     * @param solicitudes nueva lista de solicitudes. Si es null, se inicializa como lista vacía.
     */
    public void setSolicitudes(List<BaseRequest> solicitudes) {
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

    /**
     * Genera el hash code del estudiante basado en su código.
     * @return hash code del estudiante
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), codigo);
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
}
