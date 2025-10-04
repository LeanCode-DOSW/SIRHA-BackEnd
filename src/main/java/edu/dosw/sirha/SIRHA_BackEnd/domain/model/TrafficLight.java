package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.HashMap;
import java.util.Map;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.TrafficLightColor;

/**
 * Representa el semáforo académico de un estudiante dentro del sistema SIRHA.
 *
 * Este mecanismo permite llevar un control del estado de cada materia
 * mediante colores:
 * - GREEN: materia aprobada
 * - YELLOW: materia en curso
 * - RED: materia reprobada
 * - GRAY: materia no cursada
 *
 * La clase mantiene un mapa que relaciona el código de la materia con su estado actual.
 */
public class TrafficLight {

    /** Mapa de materias y sus estados de semáforo académico. */
    private Map<String, TrafficLightColor> states = new HashMap<>();

    /**
     * Obtiene el color del semáforo para una materia específica.
     *
     * @param courseCode código único de la materia
     * @return color actual de la materia, o GRAY si no tiene estado asignado
     */
    public TrafficLightColor getColor(String courseCode) {
        return states.getOrDefault(courseCode, TrafficLightColor.GRAY);
    }

    /**
     * Actualiza el estado de una materia en el semáforo académico.
     *
     * @param courseCode código de la materia
     * @param color nuevo color a asignar
     */
    public void updateState(String courseCode, TrafficLightColor color) {
        states.put(courseCode, color);
    }

    /**
     * Devuelve todos los estados de materias registrados en el semáforo.
     *
     * @return mapa con códigos de materia y sus estados
     */
    public Map<String, TrafficLightColor> getAllStates() {
        return new HashMap<>(states);
    }
}
