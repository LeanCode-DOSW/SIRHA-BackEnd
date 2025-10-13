package edu.dosw.sirha.sirha_backend.domain.model;
import java.time.LocalTime;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;

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

    /** Día de la semana (ej: "Lunes", "Martes"). */
    private DiasSemana dia;

    /** Hora de inicio en formato 24h (ej: 8 para las 08:00). */
    private LocalTime horaInicio;

    /** Hora de fin en formato 24h (ej: 10 para las 10:00). */
    private LocalTime horaFin;

    /**
     * Crea un nuevo horario.
     *
     * @param dia        Día de la semana (no distingue mayúsculas/minúsculas).
     * @param horaInicio Hora de inicio (en 24h).
     * @param horaFin    Hora de fin (en 24h, debe ser mayor que la hora de inicio).
     * @throws IllegalArgumentException si la hora de inicio no es menor a la hora de fin.
     */
    public Schedule(DiasSemana dia, LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio.isAfter(horaFin) || horaInicio.equals(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin");
        }
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Verifica si este horario se solapa con otro.
     * Dos horarios se solapan si son el mismo día y
     * los rangos de horas tienen alguna intersección.
     *
     * @param otro Otro horario a comparar.
     * @return {@code true} si los horarios se solapan, {@code false} en caso contrario.
     */
    public boolean seSolapaCon(Schedule otro) {
        if (!dia.equals(otro.dia)) {
            return false;
        }
        return this.horaInicio.isBefore(otro.horaFin) && otro.horaInicio.isBefore(this.horaFin);
    }

    /**
     * Obtiene la hora de fin del horario.
     *
     * @return Hora de fin en formato 24h.
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Obtiene la hora de inicio del horario.
     *
     * @return Hora de inicio en formato 24h.
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Obtiene el día de la semana del horario.
     *
     * @return Día en texto (ej: "Lunes").
     */
    public DiasSemana getDia() {
        return dia;
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
        return horaInicio.equals(that.horaInicio) &&
                horaFin.equals(that.horaFin) &&
                dia.equals(that.dia);
    }


    /**
     * Representación en texto del horario.
     *
     * @return Cadena con el día, hora de inicio y hora de fin.
     */
    @Override
    public String toString() {
        return "Schedule{" +
                "dia='" + dia + '\'' +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
}