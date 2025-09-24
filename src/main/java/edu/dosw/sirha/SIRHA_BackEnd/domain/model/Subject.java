package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;

import org.springframework.data.mongodb.core.mapping.Field;

public class Subject {
    @Field("codigo")
    private String codigo;
    @Field("nombre")
    private String nombre;
    @Field("creditos")
    private int creditos;
    @Field("grupos")
    private List<Group> grupos = new ArrayList<>();


    public Subject(String nombre, String codigo, int creditos) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
    }
    

    public void addGrupo(Group g) {
        grupos.add(g);
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public List<Group> getGrupos() { return grupos; }
    public void setGrupos(List<Group> grupos) { this.grupos = grupos; }

}
