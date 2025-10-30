package edu.dosw.sirha.sirha_backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para la creación/transferencia de AcademicPeriod.
 */
public class AcademicPeriodDTO {

    private String id;

    @NotBlank(message = "El código del período es obligatorio")
    private String period; // e.g. "2025-1"

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;

    private LocalDate startDateInscripciones;
    private LocalDate endDateInscripciones;

    public AcademicPeriodDTO() {}

    public AcademicPeriodDTO(String period, LocalDate startDate, LocalDate endDate) {
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPeriod() { return period; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public LocalDate getStartDateInscripciones() { return startDateInscripciones; }
    public LocalDate getEndDateInscripciones() { return endDateInscripciones; }

    public void setStartDateInscripciones(LocalDate startDateInscripciones) { this.startDateInscripciones = startDateInscripciones; }
    public void setEndDateInscripciones(LocalDate endDateInscripciones) { this.endDateInscripciones = endDateInscripciones; }

}
