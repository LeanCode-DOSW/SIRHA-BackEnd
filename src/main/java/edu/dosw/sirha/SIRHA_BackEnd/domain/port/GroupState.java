package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;

/**
 * Interfaz que define el contrato para los estados de un grupo académico
 * dentro del patrón de diseño State.
 *
 * Cada implementación (ej. StatusOpen, StatusClosed) define el comportamiento
 * específico de la inscripción de estudiantes en función del estado actual
 * del grupo.
 *
 * Ejemplo de uso:
 * - StatusOpen permite inscribir estudiantes mientras haya cupos.
 * - StatusClosed rechaza nuevas inscripciones.
 */
public interface GroupState {

    /**
     * Intenta inscribir un estudiante en el grupo, aplicando las reglas
     * específicas del estado actual del grupo.
     *
     * @param group grupo académico sobre el que se realiza la inscripción.
     *              No debe ser null.
     * @param student estudiante a inscribir en el grupo. No debe ser null.
     */
    void enrollStudent(Group group, Student student);
}

