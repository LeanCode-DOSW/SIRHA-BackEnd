package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para creación/transferencia de StudyPlan.
 */
public class StudyPlanDTO {

    private String id;

    private String name;

    @NotNull(message = "La carrera es obligatoria")
    private Careers career;

    private List<SubjectDTO> subjects;

    public StudyPlanDTO() {}

    public StudyPlanDTO(Careers career) { this.career = career; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Careers getCareer() { return career; }

    public List<SubjectDTO> getSubjects() { return subjects; }

    public void setSubjects(List<SubjectDTO> subjects) { this.subjects = subjects; }
}
