package edu.dosw.sirha.sirha_backend.domain.model.stateGroup;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.port.GroupState;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Entidad del dominio que representa un grupo académico en el sistema SIRHA.
 *
 * Un grupo es una instancia específica de una materia en un semestre determinado,
 * con un profesor asignado, un aula, schedules específicos y una capacidad máxima
 * de estudiantes. Esta clase implementa el patrón State para manejar los diferentes
 * estados del grupo (Abierto, Cerrado, Lleno).
 *
 * Características principales:
 * - Gestión de capacidad y cupos disponibles
 * - Estado dinámico que controla las inscripciones (State Pattern)
 * - Lista de estudiantes inscritos
 * - Información del profesor y curso asignado
 * - Control de inscripciones duplicadas
 *
 * Estados del grupo:
 * - ABIERTO: Acepta nuevas inscripciones si hay cupos
 * - CERRADO: No acepta mas inscripciones
 * @see GroupState
 * @see Professor
 * @see Subject
 * @see Student
 */
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String code;
    private int capacidad;
    private int inscritos;
    private GroupState estadoGrupo; // State Pattern
    private Professor profesor;
    private Subject curso;
    private List<Schedule> schedules;
    private String aula;
    private AcademicPeriod currentPeriod;
    private List<Student> estudiantes;


    /**
     * Constructor principal para crear un nuevo grupo académico.
     *
     * Inicializa el grupo con una capacidad específica y lo establece
     * en estado ABIERTO por defecto. La lista de estudiantes se inicializa vacía.
     * @throws SirhaException 
     */
    public Group(Subject subject,int capacidad, AcademicPeriod currentPeriod) throws SirhaException {
        if (capacidad <= 0) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_CAPACITY_GROUP, "La capacidad del grupo debe ser mayor a cero");
        }
        curso = subject;
        setCapacidad(capacidad);
        setCode();
        setCurrentPeriod(currentPeriod);
        this.inscritos = 0;
        this.estadoGrupo = new StatusOpen(); // Estado inicial: abierto
        this.estudiantes = new ArrayList<>();
        schedules = new ArrayList<>();
        addToSubject();
    }
    void setEstadoGrupo(GroupState estado) throws SirhaException {
        if (estado == null) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El estado del grupo no puede ser null");
        }
        this.estadoGrupo = estado;
    }
    private void addToSubject() {
        curso.addGroup(this);   
    }

    /**
     * Obtiene el estado actual del grupo.
     * @return estado actual del grupo, nunca null
     */
    public GroupState getGroupState() {
        return estadoGrupo;
    }

    /**
     * Intenta inscribir un estudiante en el grupo.
     *
     * Delega la operación de inscripción al estado actual del grupo,
     * que determina si la inscripción es posible según las reglas específicas
     * de cada estado (abierto, cerrado, lleno).
     *
     * El estado se encarga de:
     * - Validar si se puede realizar la inscripción
     * - Actualizar la lista de estudiantes y contador
     * - Cambiar el estado del grupo si es necesario
     *
     * @param estudiante estudiante a inscribir en el grupo. No debe ser null.
     * @throws SirhaException 
     */
    public void inscribirEstudiante(Student estudiante) throws SirhaException {
        if (estudiante == null) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El estudiante no puede ser null");
        }

        estadoGrupo.addStudent(this, estudiante);
    }
    private void setCode(){
        this.code = curso.getName().substring(0,3).toUpperCase() + "-" + (curso.getGroupCount() + 1);
    }

    /**
     * Calcula el número de cupos disponibles en el grupo.
     *
     * @return número de cupos libres (capacidad - inscritos).
     *         Retorna 0 si el grupo está lleno.
     */
    public int getCuposDisponibles() {
        return Math.max(0, capacidad - inscritos);
    }

    /**
     * Agrega un estudiante directamente a la lista del grupo. Se usa internamente por los estados.
     *
     * Este método es utilizado internamente por los estados del grupo
     * para realizar la inscripción efectiva. Actualiza tanto la lista
     * de estudiantes como el contador de inscritos.
     *
     * Validaciones realizadas:
     * - Verifica que el estudiante no esté ya inscrito
     * - Mantiene consistencia entre la lista y el contador
     * @throws SirhaException 
     *
     */
    void addStudent(Student estudiante) throws SirhaException {
        if (estudiante == null) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El estudiante no puede ser null");
        }

        if (estudiantes.contains(estudiante)) {
            throw SirhaException.of(ErrorCodeSirha.STUDENT_ALREADY_IN_GROUP);
        }

        estudiantes.add(estudiante);
        inscritos++;
    }

    /**
     * Remueve un estudiante del grupo. Se usa internamente por los estados.
     *
     * Elimina al estudiante de la lista y actualiza el contador.
     * Puede cambiar el estado del grupo si es necesario.
     * @throws SirhaException 
     */
    void removeStudent(Student estudiante) throws SirhaException {
        if (!estudiantes.contains(estudiante) || estudiante == null || estudiante.hasSubject(curso)) {
            throw SirhaException.of(ErrorCodeSirha.STUDENT_NOT_IN_GROUP);
        }

        estudiantes.remove(estudiante);
        inscritos--;
  
    }
    public boolean enrollStudent(Student estudiante) throws SirhaException {
        return estadoGrupo.addStudent(this, estudiante);
    }
    public boolean unenrollStudent(Student student) throws SirhaException {
        return estadoGrupo.removeStudent(this, student);
    }
    public String getCode() {
        return code;
    }

    /**
     * Verifica si el grupo está lleno.
     * @return true si no hay cupos disponibles, false en caso contrario
     */
    public boolean isFull() {
        return inscritos >= capacidad;
    }

    public boolean isOpen() {
        return estadoGrupo instanceof StatusOpen;
    }

    /**
     * Verifica si un estudiante está inscrito en el grupo.
     * @param estudiante estudiante a verificar
     * @return true si está inscrito, false en caso contrario
     */
    public boolean contieneEstudiante(Student estudiante) {
        return estudiante != null && estudiantes.contains(estudiante);
    }

    // Getters y Setters con documentación

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public AcademicPeriod getCurrentPeriod() {
        return currentPeriod;
    }
    public void setCurrentPeriod(AcademicPeriod currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    /**
     * Obtiene la capacidad máxima del grupo.
     * @return capacidad máxima de estudiantes
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Establece la capacidad máxima del grupo.
     * Solo debe modificarse si no hay estudiantes inscritos.
     *
     * @param capacidad nueva capacidad. Debe ser mayor a cero.
     * @throws SirhaException 
     */
    public void setCapacidad(int capacidad) throws SirhaException {
        if (capacidad <= 0) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_CAPACITY_GROUP, "La capacidad debe ser mayor a cero");
        }
        canEditGroup();
        if (capacidad < inscritos) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_CAPACITY_GROUP, "La nueva capacidad no puede ser menor que el número actual de inscritos");
        }
        this.capacidad = capacidad;
    }

    /**
     * Obtiene el número de estudiantes inscritos.
     * @return número actual de inscritos
     */
    public int getInscritos() {
        return inscritos;
    }

    public Professor getProfesor() {
        return profesor;
    }

    public void setProfesor(Professor profesor) {
        this.profesor = profesor;
    }

    public Subject getCurso() {
        return curso;
    }

    public void setCurso(Subject curso) {
        this.curso = curso;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    /**
     * Obtiene una copia de la lista de estudiantes inscritos.
     * @return lista inmutable de estudiantes para evitar modificaciones externas
     */
    public List<Student> getEstudiantes() {
        return Collections.unmodifiableList(estudiantes);
    }

    /**
     * Compara este grupo con otro objeto para determinar igualdad.
     * Dos grupos son iguales si tienen el mismo ID.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Group group = (Group) obj;
        return Objects.equals(currentPeriod, group.currentPeriod)
                && Objects.equals(code, group.code)
                && Objects.equals(curso, group.curso)
                && Objects.equals(profesor, group.profesor)
                && Objects.equals(aula, group.aula)
                && Objects.equals(schedules, group.schedules);
    }

    @Override
    public String toString() {
        return String.format("Group{id='%s', capacidad=%d, inscritos=%d, aula='%s', estado=%s}",
                id, capacidad, inscritos, aula,
                estadoGrupo != null ? estadoGrupo.getClass().getSimpleName() : "null");
    }
    public void removeGroup(){
        curso = null;
    }

    public void addSchedule(Schedule horario) throws SirhaException {
        canEditGroup();
        for (Schedule existente : schedules) {
            if (existente.seSolapaCon(horario)) {
                throw SirhaException.of(ErrorCodeSirha.SCHEDULE_CONFLICT);
            }
        }
        schedules.add(horario);
    }

    public boolean canEditGroup() throws SirhaException {
        if (inscritos != 0) {
            throw SirhaException.of(ErrorCodeSirha.OPERATION_NOT_ALLOWED, "No se puede modificar el grupo con estudiantes inscritos");
        }
        return inscritos == 0;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public boolean conflictoConHorario(Schedule horario) {
        for (Schedule existente : schedules) {
            if (existente.seSolapaCon(horario)) {
                return true;
            }
        }
        return false;
    }


    public boolean conflictoConHorario(Group otroGrupo){
        if (otroGrupo == null || otroGrupo.getSchedules() == null) {
            return false;
        }
        for (Schedule horarioOtro : otroGrupo.getSchedules()) {
            if (conflictoConHorario(horarioOtro)) {
                return true;
            }
        }
        return false;
    }

    public void closeGroup() throws SirhaException {
        setEstadoGrupo(new StatusClosed());
    }
    public void openGroup() throws SirhaException {
        setEstadoGrupo(new StatusOpen());
    }
    public boolean sameAcademicPeriod(AcademicPeriod period){
        return this.currentPeriod.equals(period);
    }
}