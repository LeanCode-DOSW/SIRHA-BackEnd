package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.PrerequisiteRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SubjectDTO {
    
    @NotBlank(message = "El nombre de la materia es obligatorio")
    private String name;
    
    @NotNull(message = "Los créditos son obligatorios")
    @Positive(message = "Los créditos deben ser un número positivo")
    private Integer credits;
    
    private List<Group> groups;
    private List<PrerequisiteRule> prerequisites;
    
    public SubjectDTO() {}
    
    public SubjectDTO(String name, Integer credits, List<Group> groups, List<PrerequisiteRule> prerequisites) {
        this.name = name;
        this.credits = credits;
        this.groups = groups;
        this.prerequisites = prerequisites;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public List<Group> getGroups() {
        return groups;
    }

    public List<PrerequisiteRule> getPrerequisites() {
        return prerequisites;
    }

}