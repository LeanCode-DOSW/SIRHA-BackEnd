package edu.dosw.sirha.sirha_backend.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.domain.port.PrerequisiteRule;

/**
 * Representa una materia o asignatura dentro del sistema académico.
 *
 * Reglas:
 * - Una materia puede tener múltiples grupos.
 * - Cada materia debe tener un nombre único y un identificador.
 */
@Document(collection = "subjects")
public class Subject {
    @Id
    private String id;
    private String name;
    private int credits;
    private List<Group> groups;
    private List<PrerequisiteRule> prerequisites;

    public Subject(String id, String name, int credits) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío");
        }
        if (credits <= 0) {
            throw new IllegalArgumentException("La materia debe tener al menos 1 crédito");
        }
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.groups = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
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
    public int getGroupCount() {return groups.size();}

    /**
     * Elimina un grupo de la materia.
     */
    public boolean removeGroup(Group g) {
        g.removeGroup();
        return groups.remove(g);
    }
    public boolean hasGroup(Group group){return groups.contains(group);}

    public Group getGroupByCode(String code) {
        for (Group g : groups) {
            if (g.getCode().equals(code)) {
                return g;
            }
        }
        return null;
    }

    public void setId(String id) {this.id = id;}
    public void setName(String name) {
        if (name == null || name.isBlank()) {throw new IllegalArgumentException("El nombre no puede estar vacío");}
        this.name = name;
    }
    public void setCredits(int credits) {
        if (credits <= 0) {throw new IllegalArgumentException("La materia debe tener al menos 1 crédito");}
        this.credits = credits;
    }
    public void setPrerequisites(List<PrerequisiteRule> prerequisites) {this.prerequisites = prerequisites;}

    

    public String getId() {return id;}
    public String getName() {return name;}
    public List<PrerequisiteRule> getPrerequisites() {return prerequisites;}
    public List<Group> getGroups() {return groups;}
    public void deleteGroups() {this.groups.clear();}
    public int getCredits() {return credits;}

    public boolean canEnroll(AcademicProgress progress) {
        if (prerequisites.isEmpty()) {
            return true;
        }
        return prerequisites.stream().allMatch(rule -> rule.canEnroll(this, progress));
    }
    public boolean hasPrerequisites() {return !prerequisites.isEmpty();}
    public void addPrerequisite(PrerequisiteRule prerequisite) {this.prerequisites.add(prerequisite);}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id);
    }
    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", grupos=" + groups.size() +
                '}';
    }

    public List<Group> getOpenGroups() {
        List<Group> openGroups = new ArrayList<>();
        for (Group g : groups) {
            if (g.isOpen()) {
                openGroups.add(g);
            }
        }
        return openGroups;
    }
}