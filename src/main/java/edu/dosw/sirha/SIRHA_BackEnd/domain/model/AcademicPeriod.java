package edu.dosw.sirha.sirha_backend.domain.model;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "academic_periods")
public class AcademicPeriod {
    @Id
    private String id;
    private String period; // "2024-1"
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate startDateInscripciones;
    private LocalDate endDateInscripciones;

    public AcademicPeriod() {
    }
    public AcademicPeriod(String period, LocalDate startDate, LocalDate endDate) {
        this();
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDate hoy = LocalDate.now();
        return isActivo() && !hoy.isBefore(startDate) && !hoy.isAfter(endDate);
    }

    public boolean isPeriodInscripcionesAbiertas() {
        LocalDate hoy = LocalDate.now();
        return isActive() && 
               startDateInscripciones != null && 
               endDateInscripciones != null &&
               !hoy.isBefore(startDateInscripciones) && 
               !hoy.isAfter(endDateInscripciones);
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPeriod() { return period; }

    public LocalDate getStartDate() { return startDate; }

    public LocalDate getEndDate() { return endDate; }

    public LocalDate getStartDateInscripciones() { return startDateInscripciones; }
    public LocalDate getEndDateInscripciones() { return endDateInscripciones; }
    public void setStartDatesInscripciones(LocalDate startDateInscripciones, LocalDate endDateInscripciones) { 
        this.startDateInscripciones = startDateInscripciones; 
        this.endDateInscripciones = endDateInscripciones;
    }

    
    
    public boolean isActivo() { 
        return !startDate.isAfter(LocalDate.now()) && !endDate.isBefore(LocalDate.now());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AcademicPeriod that = (AcademicPeriod) obj;
        return Objects.equals(period, that.period) &&
               Objects.equals(startDate, that.startDate) &&
               Objects.equals(endDate, that.endDate);
    }

    @Override
    public String toString() {
        return String.format("AcademicPeriod{id='%s', period='%s', activo=%s, fechas=%s a %s}", 
                           id, period, isActivo(), startDate, endDate);
    }
}
