package edu.dosw.sirha.sirha_backend.domain.model;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;

@Document(collection = "study_plans")
public class StudyPlan {
    @Id
    private String id;
    private String name;
    private Careers career;

    // ✅ CAMBIO: Solo guardar IDs de las materias
    private List<String> subjectIds; // IDs en lugar de objetos completos

    public StudyPlan() {
        this.subjectIds = new ArrayList<>();
    }

    public StudyPlan(Careers career) {
        this();
        this.career = career;
        this.name = career.getDisplayName();
    }

    // ✅ Agregar materia por ID
    public void addSubject(Subject subject) {
        if (subject == null || subject.getId() == null) {
            throw new IllegalArgumentException("Subject o su ID no pueden ser null");
        }
        if (!subjectIds.contains(subject.getId())) {
            subjectIds.add(subject.getId());
        }
    }

    // ✅ Remover materia por ID
    public void removeSubject(Subject subject) {
        if (subject != null && subject.getId() != null) {
            subjectIds.remove(subject.getId());
        }
    }

    public boolean hasSubject(String subjectId) {
        return subjectIds.contains(subjectId);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Careers getCareer() { return career; }
    public List<String> getSubjectIds() { return subjectIds; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCareer(Careers career) { this.career = career; }
    public void setSubjectIds(List<String> subjectIds) { this.subjectIds = subjectIds; }
}