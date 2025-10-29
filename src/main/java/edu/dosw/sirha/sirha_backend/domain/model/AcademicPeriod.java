package edu.dosw.sirha.sirha_backend.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "academic_periods")
public class AcademicPeriod {

    @Id
    private String id;
    private String period; // Ejemplo: "2025-2"
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate startDateInscripciones;
    private LocalDate endDateInscripciones;

    public AcademicPeriod() {}

    public AcademicPeriod(String period, LocalDate startDate, LocalDate endDate) {
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Indica si el periodo académico está activo actualmente.
     */
    @JsonIgnore
    public boolean isActivo() {
        if (startDate == null || endDate == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return !startDate.isAfter(hoy) && !endDate.isBefore(hoy);
    }

    /**
     * Indica si el periodo está activo y dentro de las fechas definidas.
     */
    @JsonIgnore
    public boolean isActive() {
        if (startDate == null || endDate == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return isActivo() && !hoy.isBefore(startDate) && !hoy.isAfter(endDate);
    }

    /**
     * Indica si las inscripciones están abiertas actualmente.
     */
    public boolean isPeriodInscripcionesAbiertas() {
        if (!isActivo() || startDateInscripciones == null || endDateInscripciones == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(startDateInscripciones) && !hoy.isAfter(endDateInscripciones);
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getStartDateInscripciones() { return startDateInscripciones; }
    public void setStartDateInscripciones(LocalDate startDateInscripciones) { this.startDateInscripciones = startDateInscripciones; }

    public LocalDate getEndDateInscripciones() { return endDateInscripciones; }
    public void setEndDateInscripciones(LocalDate endDateInscripciones) { this.endDateInscripciones = endDateInscripciones; }

    /**
     * Método auxiliar para asignar ambas fechas de inscripción a la vez.
     */
    public void setStartDatesInscripciones(LocalDate startDateInscripciones, LocalDate endDateInscripciones) {
        this.startDateInscripciones = startDateInscripciones;
        this.endDateInscripciones = endDateInscripciones;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AcademicPeriod that = (AcademicPeriod) obj;
        return Objects.equals(period, that.period)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, startDate, endDate);
    }

    @Override
    public String toString() {
        return String.format(
                "AcademicPeriod{id='%s', period='%s', activo=%s, fechas=%s a %s}",
                id, period, isActivo(), startDate, endDate
        );
    }
}