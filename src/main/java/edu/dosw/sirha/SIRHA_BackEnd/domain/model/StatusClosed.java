package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;

/**
 * Estado de un grupo académico cuando está cerrado.
 *
 * Implementa el patrón State dentro de la clase {@link Group}.
 *
 * Características:
 * - No permite nuevas inscripciones de estudiantes.
 * - Mantiene el grupo en estado cerrado hasta que cambie
 *   a otro estado válido.
 *
 * @see Group
 * @see GroupState
 */
public class StatusClosed implements GroupState {

    /**
     * Intenta inscribir un estudiante en un grupo cerrado.
     *
     * Como el grupo está cerrado, esta operación no está permitida
     * y se muestra un mensaje en la consola.
     *
     * @param group   grupo donde se intenta inscribir
     * @param student estudiante a inscribir
     */
    @Override
    public void enrollStudent(Group group, Student student) {
        System.out.println("No es posible inscribir. El grupo está cerrado.");
    }
}
