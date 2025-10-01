package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "academic_periods")
public class AcademicPeriod {
    @Id
    private String id;
    private String periodo; // "2024-1"
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate startDateInscripciones;
    private LocalDate endDateInscripciones;

    public AcademicPeriod() {
    }
    public AcademicPeriod(String periodo, LocalDate startDate, LocalDate endDate) {
        this();
        this.periodo = periodo;
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

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getStartDateInscripciones() { return startDateInscripciones; }
    public void setStartDateInscripciones(LocalDate startDateInscripciones) { 
        this.startDateInscripciones = startDateInscripciones; 
    }

    public LocalDate getEndDateInscripciones() { return endDateInscripciones; }
    public void setEndDateInscripciones(LocalDate endDateInscripciones) { 
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
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return String.format("AcademicPeriod{id='%s', periodo='%s', activo=%s, fechas=%s a %s}", 
                           id, periodo, isActivo(), startDate, endDate);
    }
}
