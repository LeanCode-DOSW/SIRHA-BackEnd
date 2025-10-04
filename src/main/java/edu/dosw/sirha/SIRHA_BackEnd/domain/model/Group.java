package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;

/**
 * Entidad del dominio que representa un grupo académico en el sistema SIRHA.
 *
 * Un grupo es una instancia específica de una materia en un semestre determinado,
 * con un profesor asignado, un aula, horarios específicos y una capacidad máxima
 * de estudiantes. Esta clase implementa el patrón State para manejar los diferentes
 * estados del grupo (Open, Closed, Full).
 *
 * Características principales:
 * - Gestión de capacidad y cupos disponibles
 * - Estado dinámico que controla las inscripciones (State Pattern)
 * - Lista de estudiantes inscritos
 * - Información del profesor y curso asignado
 * - Control de inscripciones duplicadas
 *
 * Estados del grupo:
 * - OPEN: acepta nuevas inscripciones si hay cupos
 * - CLOSED: no acepta más inscripciones
 *
 * @see GroupState
 * @see Professor
 * @see Subject
 * @see Student
 */
public class Group {
    private String id;
    private int capacity;
    private int enrolled;
    private GroupState groupState; // State Pattern
    private Professor professor;
    private Subject subject;
    private String classroom;
    private List<Student> students;

    /**
     * Constructor principal para crear un nuevo grupo académico.
     *
     * @param capacity capacidad máxima de estudiantes para el grupo.
     *                 Debe ser un valor positivo mayor a cero.
     */
    public Group(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("La capacidad del grupo debe ser mayor a cero");
        }
        this.capacity = capacity;
        this.enrolled = 0;
        this.groupState = new StatusOpen(); // Estado inicial: abierto
        this.students = new ArrayList<>();
    }

    public void setGroupState(GroupState state) {
        if (state == null) {
            throw new IllegalArgumentException("El estado del grupo no puede ser null");
        }
        this.groupState = state;
    }

    /**
     * Obtiene el estado actual del grupo.
     */
    public GroupState getGroupState() {
        return groupState;
    }

    /**
     * Intenta inscribir un estudiante en el grupo.
     *
     * @param student estudiante a inscribir en el grupo.
     */
    public void enrollStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }
        groupState.enrollStudent(this, student);
    }

    /**
     * Calcula el número de cupos disponibles en el grupo.
     *
     * @return número de cupos libres.
     */
    public int getAvailableSeats() {
        return Math.max(0, capacity - enrolled);
    }

    /**
     * Agrega un estudiante directamente a la lista del grupo.
     * Usado por los estados internos.
     */
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }
        if (students.contains(student)) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en el grupo");
        }
        students.add(student);
        enrolled++;
    }

    /**
     * Remueve un estudiante del grupo.
     *
     * @return true si fue removido, false si no estaba inscrito.
     */
    public boolean removeStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }
        boolean removed = students.remove(student);
        if (removed) {
            enrolled--;
        }
        return removed;
    }

    /**
     * Verifica si el grupo está lleno.
     */
    public boolean isFull() {
        return enrolled >= capacity;
    }

    /**
     * Verifica si un estudiante está inscrito en el grupo.
     */
    public boolean containsStudent(Student student) {
        return student != null && students.contains(student);
    }

    // ---------- Getters & Setters ----------

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }
        if (enrolled > 0) {
            throw new IllegalStateException("No se puede cambiar la capacidad con estudiantes inscritos");
        }
        this.capacity = capacity;
    }

    public int getEnrolled() { return enrolled; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    // ---------- equals, hashCode & toString ----------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Group group = (Group) obj;
        return Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Group{id='%s', capacity=%d, enrolled=%d, classroom='%s', state=%s}",
                id, capacity, enrolled, classroom,
                groupState != null ? groupState.getClass().getSimpleName() : "null");
    }

    /**
     * Calcula el número de cupos disponibles en el grupo.
     *
     * Este método determina cuántos espacios quedan libres para inscribir
     * nuevos estudiantes en el grupo, considerando la capacidad máxima
     * y la cantidad actual de estudiantes inscritos.
     *
     * Reglas:
     * - Si el número de inscritos es menor a la capacidad, retorna la diferencia.
     * - Si el número de inscritos es mayor o igual a la capacidad, retorna 0.
     *
     * @return número de cupos disponibles en el grupo.
     *         Retorna 0 si el grupo ya está lleno.
     *
     * @example
     * Group g = new Group(30);
     * g.getAvailableSlots(); // Retorna 30 si aún no hay inscritos
     */
    public int getAvailableSlots() {
        return Math.max(0, capacity - enrolled);
    }

}