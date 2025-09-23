package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.ArrayList;
import java.util.List;

public class StudyPlan {
    private String nombre;
    private List<Subject> materias = new ArrayList<>();

    public void addMateria(Subject m) {
        materias.add(m);
    }

    public List<Subject> getMaterias() {
        return materias;
    }
}

