package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;

public class Subject {
    private String codigo;
    private String nombre;
    private List<Group> grupos = new ArrayList<>();

    public void addGrupo(Group g) {
        grupos.add(g);
    }

}
