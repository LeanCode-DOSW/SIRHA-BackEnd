package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "study_plans")
public class StudyPlan {
    @Field("nombre")
    private String name;
    @Field("materias")
    private Map<String, Subject> subjects;


    public StudyPlan(String name) {
        this.name = name;
        this.subjects = new HashMap<>();
    }

    public void addSubject(Subject m) {
        subjects.put(m.getName(), m);
    }

    public Map<String, Subject> getSubjects() {
        return subjects;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean hasSubject(Subject subject) {
        return subjects.containsKey(subject.getName());
    }
    public Subject getSubjectByName(String name) {
        return subjects.get(name);
    }
    
}

