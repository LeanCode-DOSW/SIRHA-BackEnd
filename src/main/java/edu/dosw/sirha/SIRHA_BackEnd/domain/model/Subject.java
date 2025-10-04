package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad del dominio que representa una asignatura dentro del sistema SIRHA.
 *
 * Una asignatura es un curso específico dentro del plan de estudios.
 * Cada asignatura puede tener múltiples grupos (secciones) asociados.
 *
 * Características principales:
 * - Identificada por un código único
 * - Tiene un nombre descriptivo
 * - Mantiene la lista de grupos disponibles
 *
 * @see Group
 */
public class Subject {
    private String code;
    private String name;
    private List<Group> groups = new ArrayList<>();

    /**
     * Agrega un grupo a la asignatura.
     *
     * @param group grupo a agregar, no debe ser null
     */
    public void addGroup(Group group) {
        groups.add(group);
    }

    /**
     * Obtiene el código único de la asignatura.
     *
     * @return código de la asignatura
     */
    public String getCode() {
        return code;
    }

    /**
     * Establece el código único de la asignatura.
     *
     * @param code nuevo código de la asignatura
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene el nombre de la asignatura.
     *
     * @return nombre de la asignatura
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la asignatura.
     *
     * @param name nuevo nombre de la asignatura
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la lista de grupos de la asignatura.
     *
     * @return lista de grupos
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Establece la lista de grupos de la asignatura.
     *
     * @param groups nueva lista de grupos
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups != null ? groups : new ArrayList<>();
    }
}
