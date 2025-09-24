package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "study_plans")
public class StudyPlan {
     @Field("nombre")
    private String nombre;
    @Field("materias")
    private List<Subject> materias = new ArrayList<>();


    public StudyPlan(String nombre) {
        this.nombre = nombre;
    }

    public void addMateria(Subject m) {
        materias.add(m);
    }

    public List<Subject> getMaterias() {
        return materias;
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

