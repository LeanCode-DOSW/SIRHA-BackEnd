package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

/**
 * Representa una solicitud de cambio de grupo para una materia específica.
 *
 * Esta solicitud permite al estudiante cambiar de un grupo a otro,
 * siempre que no existan conflictos de horario ni restricciones
 * académicas que lo impidan.
 *
 * Reglas de negocio:
 * - El grupo de origen y el grupo de destino deben pertenecer a la misma materia.
 * - El estudiante debe estar previamente inscrito en el grupo de origen.
 * - El grupo de destino debe tener cupo disponible.
 * - No debe existir conflicto de horarios con otras materias cursadas.
 */
public class ChangeGroup extends BaseRequest {

    /** Identificador único del grupo actual del estudiante. */
    private String fromGroupId;

    /** Identificador único del grupo al cual desea cambiarse. */
    private String toGroupId;

    /**
     * Constructor para inicializar una solicitud de cambio de grupo.
     *
     * @param prioridad nivel de prioridad de la solicitud (1 a 5).
     * @param fromGroupId identificador único del grupo actual.
     * @param toGroupId identificador único del grupo destino.
     */
    public ChangeGroup(int prioridad, String fromGroupId, String toGroupId) {
        super(prioridad);
        this.fromGroupId = fromGroupId;
        this.toGroupId = toGroupId;
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
     * Aplica el cambio de grupo, modificando la inscripción
     * del estudiante al nuevo grupo.
     *
     * @return {@code true} si el cambio se aplicó correctamente, {@code false} en caso contrario.
     */
    @Override
    public boolean aplicar() {
        return false; // TODO: implementar lógica de aplicación
    }

    /**
     * Marca la solicitud como aprobada y aplica el cambio de grupo.
     */
    @Override
    public void aprobar() {
        // TODO: implementar lógica de aprobación
    }

    /**
     * Marca la solicitud como rechazada y mantiene al estudiante
     * en su grupo actual.
     */
    @Override
    public void rechazar() {
        // TODO: implementar lógica de rechazo
    }
}
