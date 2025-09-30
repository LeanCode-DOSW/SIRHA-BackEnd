package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "study_plans")
public class StudyPlan {
    @Field("nombre")
    private String nombre;
    @Field("materias")
    private Map<String, Subject> materias;


    public StudyPlan(String nombre) {
        this.nombre = nombre;
        this.materias = new HashMap<>();
    }

    public void addMateria(Subject m) {
        materias.put(m.getName(), m);
    }

    public Map<String, Subject> getMaterias() {
        return materias;
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

