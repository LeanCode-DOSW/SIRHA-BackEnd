package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

/**
 * Entidad del dominio que representa a un profesor dentro del sistema SIRHA.
 *
 * Un profesor es un usuario con rol docente que puede estar asignado a uno o más
 * grupos académicos. Además, cuenta con un horario de disponibilidad que permite
 * gestionar asignaciones de clases y evitar conflictos.
 *
 * @see User
 * @see Group
 */
public class Professor extends User {

    /** Horario asignado al profesor (por ejemplo, "Lunes 8-10am, Miércoles 2-4pm"). */
    private String schedule;

    /**
     * Constructor vacío requerido por frameworks de persistencia.
     */
    public Professor() {
        super();
    }

    /**
     * Constructor completo para crear un profesor.
     *
     * @param id identificador único del profesor
     * @param username nombre de usuario
     * @param passwordHash contraseña en formato hash
     * @param role rol del usuario en el sistema (ej. "PROFESSOR")
     * @param schedule horario asignado al profesor
     */
    public Professor(String id, String username, String passwordHash, String role, String schedule) {
        super(id, username, passwordHash, role);
        this.schedule = schedule;
    }

    /**
     * Obtiene el horario del profesor.
     * @return horario como cadena de texto
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * Establece el horario del profesor.
     * @param schedule nuevo horario en formato de texto
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return String.format("Professor{id='%s', username='%s', schedule='%s'}",
                getId(), getUsername(), schedule);
    }
}
