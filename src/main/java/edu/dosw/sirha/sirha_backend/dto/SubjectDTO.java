package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;



/**
 * DTO para transferencia de datos de Subject.
 * No debe contener entidades del dominio (Group, PrerequisiteRule).
 */
public class SubjectDTO {

    @NotBlank(message = "El nombre de la materia es obligatorio")
    private String name;

    @NotNull(message = "Los créditos son obligatorios")
    @Positive(message = "Los créditos deben ser un número positivo")
    private Integer credits;

    // Solo IDs, no entidades completas
    private List<String> groupIds;
    private List<PrerequisiteDTO> prerequisites;

    public SubjectDTO() {}

    public SubjectDTO(String name, Integer credits) {
        this.name = name;
        this.credits = credits;
    }

    public SubjectDTO(String name, Integer credits, List<String> groupIds, List<PrerequisiteDTO> prerequisites) {
        this.name = name;
        this.credits = credits;
        this.groupIds = groupIds;
        this.prerequisites = prerequisites;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Integer getCredits() {
        return credits;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public List<PrerequisiteDTO> getPrerequisites() {
        return prerequisites;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public void setPrerequisites(List<PrerequisiteDTO> prerequisites) {
        this.prerequisites = prerequisites;
    }
}