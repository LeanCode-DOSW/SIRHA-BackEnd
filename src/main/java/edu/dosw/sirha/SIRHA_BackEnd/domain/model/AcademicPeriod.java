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
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaInicioInscripciones;
    private LocalDate fechaFinInscripciones;

    public AcademicPeriod() {
    }
    public AcademicPeriod(String periodo, LocalDate fechaInicio, LocalDate fechaFin) {
        this();
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public boolean esPeriodoActivo() {
        LocalDate hoy = LocalDate.now();
        return isActivo() && !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }

    public boolean esPeriodoInscripcionesAbiertas() {
        LocalDate hoy = LocalDate.now();
        return esPeriodoActivo() && 
               fechaInicioInscripciones != null && 
               fechaFinInscripciones != null &&
               !hoy.isBefore(fechaInicioInscripciones) && 
               !hoy.isAfter(fechaFinInscripciones);
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    
    public LocalDate getFechaInicioInscripciones() { return fechaInicioInscripciones; }
    public void setFechaInicioInscripciones(LocalDate fechaInicioInscripciones) { 
        this.fechaInicioInscripciones = fechaInicioInscripciones; 
    }
    
    public LocalDate getFechaFinInscripciones() { return fechaFinInscripciones; }
    public void setFechaFinInscripciones(LocalDate fechaFinInscripciones) { 
        this.fechaFinInscripciones = fechaFinInscripciones; 
    }
    
    public boolean isActivo() { 
        return !fechaInicio.isAfter(LocalDate.now()) && !fechaFin.isBefore(LocalDate.now());
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
                           id, periodo, isActivo(), fechaInicio, fechaFin);
    }
}
