package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

/**
 * Representa una materia o asignatura dentro del sistema académico.
 *
 * Reglas:
 * - Una materia puede tener múltiples grupos.
 * - Cada materia debe tener un nombre único y un identificador.
 */
public class Subject {
    @Id
    private int id;
    private String name;
    private int creditos;
    private List<Group> groups;

    public Subject(int id, String name, int creditos) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío");
        }
        if (creditos <= 0) {
            throw new IllegalArgumentException("La materia debe tener al menos 1 crédito");
        }
        this.id = id;
        this.name = name;
        this.creditos = creditos;
        this.groups = new ArrayList<>();
    }

    /**
     * Asocia un grupo a la materia.
     */
    public void addGroup(Group g) {
        if (g == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo");
        }
        groups.add(g);
    }

    /**
     * Elimina un grupo de la materia.
     */
    public boolean removeGroup(Group g) {
        return groups.remove(g);
    }

    // ---------- Getters y Setters ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.name = name;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        if (creditos <= 0) {
            throw new IllegalArgumentException("La materia debe tener al menos 1 crédito");
        }
        this.creditos = creditos;
    }

    public List<Group> getGroups() {
        return groups;
    }

    // ---------- equals, hashCode y toString ----------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creditos=" + creditos +
                ", grupos=" + groups.size() +
                '}';
    }
    public String getNombre(){
        return name;
    }

    public List<Group> getGrupos(){
        return groups;
    }
    public boolean isHasGroup(Group group){
        return groups.contains(group);
    }
}