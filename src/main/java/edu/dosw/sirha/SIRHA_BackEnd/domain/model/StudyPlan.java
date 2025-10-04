package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad del dominio que representa el plan de estudios de un estudiante.
 *
 * El plan de estudios contiene todas las asignaturas (subjects) que un estudiante
 * debe cursar a lo largo de su carrera académica. Permite gestionar la lista de
 * materias asociadas al estudiante.
 *
 * Características principales:
 * - Almacena un nombre identificador del plan de estudios
 * - Mantiene la lista de materias del plan
 * - Provee métodos para agregar y consultar asignaturas
 *
 * @see Subject
 */
public class StudyPlan {
    private String name;
    private List<Subject> subjects = new ArrayList<>();

    /**
     * Agrega una nueva materia al plan de estudios.
     *
     * @param subject materia a agregar, no debe ser null
     */
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    /**
     * Obtiene la lista de materias del plan de estudios.
     *
     * @return lista de asignaturas registradas
     */
    public List<Subject> getSubjects() {
        return subjects;
    }

    /**
     * Obtiene el nombre del plan de estudios.
     *
     * @return nombre del plan
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del plan de estudios.
     *
     * @param name nuevo nombre del plan
     */
    public void setName(String name) {
        this.name = name;
    }
}

