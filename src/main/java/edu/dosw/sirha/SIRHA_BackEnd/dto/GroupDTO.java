package edu.dosw.sirha.SIRHA_BackEnd.dto;

/**
 * DTO que representa un grupo académico en el sistema SIRHA.
 *
 * Este objeto se utiliza para transferir información básica de los grupos
 * entre el backend y el cliente, sin exponer toda la lógica interna de dominio.
 *
 * Campos principales:
 * - id: Identificador único del grupo.
 * - capacity: Número máximo de estudiantes permitidos.
 * - enrolled: Número actual de estudiantes inscritos.
 * - professorId: Identificador del profesor asignado al grupo.
 */
public class GroupDTO {

    private String id;
    private int capacity;
    private int enrolled;
    private String professorId;

    /**
     * Constructor vacío para frameworks de serialización.
     */
    public GroupDTO() {}

    /**
     * Constructor completo para inicializar un DTO de grupo.
     *
     * @param id identificador único del grupo
     * @param capacity capacidad máxima del grupo
     * @param enrolled número actual de estudiantes inscritos
     * @param professorId identificador del profesor asignado
     */
    public GroupDTO(String id, int capacity, int enrolled, String professorId) {
        this.id = id;
        this.capacity = capacity;
        this.enrolled = enrolled;
        this.professorId = professorId;
    }

    // ---------- Getters y Setters ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    /**
     * Representación en texto del DTO de grupo.
     * @return cadena con los valores básicos del grupo
     */
    @Override
    public String toString() {
        return String.format(
                "GroupDTO{id='%s', capacity=%d, enrolled=%d, professorId='%s'}",
                id, capacity, enrolled, professorId
        );
    }
}
