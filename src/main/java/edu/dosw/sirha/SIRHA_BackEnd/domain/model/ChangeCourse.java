package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

/**
 * Representa una solicitud de cambio de materia dentro del sistema académico.
 *
 * Esta solicitud permite al estudiante cambiar una materia inscrita por otra,
 * siempre que se cumplan los requisitos académicos y no existan conflictos
 * de horario o prerrequisitos pendientes.
 *
 * Reglas de negocio:
 * - El curso de origen debe estar previamente inscrito.
 * - El curso de destino debe pertenecer al plan de estudios del estudiante.
 * - El estudiante debe cumplir con los prerrequisitos de la materia destino.
 * - No debe existir conflicto de horarios con otras materias inscritas.
 */
public class ChangeCourse extends BaseRequest {

    /** Identificador único del curso de origen (materia actual). */
    private String fromCourseId;

    /** Identificador único del curso de destino (materia deseada). */
    private String toCourseId;

    /**
     * Constructor para inicializar una solicitud de cambio de materia.
     *
     * @param prioridad nivel de prioridad de la solicitud (1 a 5).
     * @param fromCourseId identificador único del curso actual.
     * @param toCourseId identificador único del curso destino.
     */
    public ChangeCourse(int prioridad, String fromCourseId, String toCourseId) {
        super(prioridad);
        this.fromCourseId = fromCourseId;
        this.toCourseId = toCourseId;
    }

    /**
     * Valida si la solicitud cumple con las condiciones necesarias
     * para ser procesada.
     *
     * @return {@code true} si la solicitud es válida, {@code false} en caso contrario.
     */
    @Override
    public boolean validar() {
        return false; // TODO: implementar lógica de validación
    }

    /**
     * Aplica el cambio de materia, actualizando la inscripción
     * del estudiante al curso destino.
     *
     * @return {@code true} si el cambio se aplicó correctamente, {@code false} en caso contrario.
     */
    @Override
    public boolean aplicar() {
        return false; // TODO: implementar lógica de aplicación
    }

    /**
     * Marca la solicitud como aprobada y aplica el cambio de materia.
     */
    @Override
    public void aprobar() {
        // TODO: implementar lógica de aprobación
    }

    /**
     * Marca la solicitud como rechazada y mantiene al estudiante
     * en su materia actual.
     */
    @Override
    public void rechazar() {
        // TODO: implementar lógica de rechazo
    }
}
