package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "academic_periods")
public class AcademicPeriod {
    @Id
    private int id;
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
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPeriodo() { return periodo; }

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
        return Objects.equals(periodo, that.periodo) &&
               Objects.equals(startDate, that.startDate) &&
               Objects.equals(endDate, that.endDate);
    }

    @Override
    public String toString() {
        return String.format("AcademicPeriod{id='%s', periodo='%s', activo=%s, fechas=%s a %s}", 
                           id, periodo, isActivo(), startDate, endDate);
    }
}
