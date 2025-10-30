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
    private Map<String, Subject> subjects;


    public StudyPlan(Careers career) {
        this.career = career;
        this.subjects = new HashMap<>();
        this.name = career.getDisplayName();
        calculateTotalCredits();
    }

    public int calculateTotalCredits() {
        int total = 0;
        for (Subject subject : subjects.values()) {
            total += subject.getCredits();
        }
        return total;
    }

    public void addSubject(Subject m) {
        subjects.put(m.getName(), m);
        calculateTotalCredits();
    }

    public Map<String, Subject> getSubjects() {
        return subjects;
    }

    public String getName() { return name; }
    
    public boolean hasSubject(Subject subject) {
        return subjects.containsKey(subject.getName());
    }
    public Subject getSubjectByName(String name) {
        return subjects.get(name);
    }
    
    public Careers getCareer() { return career; }
}

