package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;

/**
 * Estado de un grupo académico cuando está abierto.
 *
 * Implementa el patrón State dentro de la clase {@link Group}.
 *
 * Características:
 * - Permite inscribir estudiantes mientras haya cupos disponibles.
 * - Cambia automáticamente el estado del grupo a cerrado
 *   cuando se llenan todos los cupos.
 *
 * @see Group
 * @see GroupState
 * @see StatusClosed
 */
public class StatusOpen implements GroupState {

    /**
     * Intenta inscribir un estudiante en un grupo abierto.
     *
     * - Si hay cupos disponibles, el estudiante es inscrito.
     * - Si al inscribir se llenan los cupos, el grupo cambia automáticamente a estado cerrado.
     * - Si no hay cupos disponibles, el grupo cambia a cerrado y delega la inscripción al nuevo estado.
     *
     * @param group   grupo donde se intenta inscribir
     * @param student estudiante a inscribir
     */
    @Override
    public void enrollStudent(Group group, Student student) {
        if (group.getAvailableSlots() > 0) {
            group.addStudent(student);
            System.out.println("Estudiante inscrito exitosamente en el grupo.");
            if (group.getAvailableSlots() == 0) {
                group.setGroupState(new StatusClosed());
            }
        } else {
            group.setGroupState(new StatusClosed());
            group.getGroupState().enrollStudent(group, student);
        }
    }
}
