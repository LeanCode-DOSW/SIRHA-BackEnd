package edu.dosw.sirha.sirha_backend.domain.model;
import java.time.LocalTime;
import java.util.Objects;

import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Representa un horario de clase con un día y un rango de horas.
 * Se utiliza para verificar solapamientos entre horarios
 * de grupos o estudiantes.
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 *     Schedule s1 = new Schedule("Lunes", 8, 10);
 *     Schedule s2 = new Schedule("Lunes", 9, 11);
 *     boolean conflicto = s1.seSolapaCon(s2); // true
 * </pre>
 */
public class Schedule {

    private DiasSemana day;
    private LocalTime startHour;
    private LocalTime endHour;

    /**
     * Crea un nuevo horario.
     *
     * @param dia        Día de la semana (no distingue mayúsculas/minúsculas).
     * @param horaInicio Hora de inicio (en 24h).
     * @param horaFin    Hora de fin (en 24h, debe ser mayor que la hora de inicio).
     * @throws SirhaException 
     */
    public Schedule(DiasSemana day, LocalTime startHour, LocalTime endHour) throws SirhaException {
        if (startHour.isAfter(endHour) || startHour.equals(endHour)) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_DATE_RANGE,
                    "La hora de inicio debe ser menor que la hora de fin");
        }
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    /**
     * Verifica si este horario se solapa con otro.
     * Dos horarios se solapan si son el mismo día y
     * los rangos de horas tienen alguna intersección.
     *
     * @param otro Otro horario a comparar.
     * @return {@code true} si los horarios se solapan, {@code false} en caso contrario.
     */
    public boolean overlapsWith(Schedule other) {
        if (!day.equals(other.day)) {
            return false;
        }
        return this.startHour.isBefore(other.endHour) && other.startHour.isBefore(this.endHour);
    }

    /**
     * Obtiene la hora de fin del horario.
     *
     * @return Hora de fin en formato 24h.
     */
    public LocalTime getEndHour() {
        return endHour;
    }

    /**
     * Obtiene la hora de inicio del horario.
     *
     * @return Hora de inicio en formato 24h.
     */
    public LocalTime getStartHour() {
        return startHour;
    }

    /**
     * Obtiene el día de la semana del horario.
     *
     * @return Día en texto (ej: "Lunes").
     */
    public DiasSemana getDay() {
        return day;
    }

    /**
     * Compara este horario con otro basado en día, hora de inicio y hora de fin.
     * La comparación de día no distingue mayúsculas/minúsculas.
     *
     * @param o Objeto a comparar.
     * @return {@code true} si son equivalentes, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule that = (Schedule) o;
        return startHour.equals(that.startHour) &&
                endHour.equals(that.endHour) &&
                day.equals(that.day);
    }
    @Override
    public int hashCode() {
        return Objects.hash(day, startHour, endHour);
    }

    /**
     * Representación en texto del horario.
     *
     * @return Cadena con el día, hora de inicio y hora de fin.
     */
    @Override
    public String toString() {
        return "Schedule{" +
                "day='" + day + '\'' +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}