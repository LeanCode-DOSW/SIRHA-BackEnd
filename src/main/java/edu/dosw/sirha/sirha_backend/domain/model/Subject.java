package edu.dosw.sirha.sirha_backend.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.domain.port.PrerequisiteRule;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

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

    public Subject() {
        this.groups = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
    }

    public Subject(String id, String name, int credits) throws SirhaException {
        this(name, credits);
        this.id = id;
    }

    public Subject(String name, int credits) throws SirhaException {
        if (name == null || name.isBlank()) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre no puede estar vacío");
        }
        if (credits <= 0) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "La materia debe tener al menos 1 crédito");
        }
        this.name = name;
        this.credits = credits;
        this.groups = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
    }

    public int getNextGroupSequence() {
        int maxSeq = 0;
        for (Group g : groups) {
            String[] parts = g.getCode().split("-");
            if (parts.length == 2) {
                try {
                    int seq = Integer.parseInt(parts[1]);
                    if (seq > maxSeq) {
                        maxSeq = seq;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar códigos no conformes
                }
            }
        }
        return maxSeq + 1;
    }

    /**
     * Asocia un grupo a la materia.
     * @throws SirhaException
     */
    public void addGroup(Group g) throws SirhaException {
        if (g == null) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El grupo no puede ser nulo");
        }
        if (groups.contains(g)) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El grupo ya está asociado a la materia");
        }
        groups.add(g);
    }

    public int getGroupCount() {return groups.size();}

    /**
     * Elimina un grupo de la materia.
     */
    public boolean removeGroup(Group g) {
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
    public void setName(String name) throws SirhaException {
        if (name == null || name.isBlank()) {throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre no puede estar vacío");}
        this.name = name;
    }
    public void setCredits(int credits) throws SirhaException {
        if (credits <= 0) {throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "La materia debe tener al menos 1 crédito");}
        this.credits = credits;
    }

    public void setPrerequisites(List<PrerequisiteRule> prerequisites) {this.prerequisites = prerequisites;}
    public void setGroups(List<Group> groups) {this.groups = groups;}

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
        return this.id.equals(subject.id) && this.name.equals(subject.name)
                && this.credits == subject.credits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, credits);
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