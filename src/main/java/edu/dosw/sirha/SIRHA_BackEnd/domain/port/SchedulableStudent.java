package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.util.*;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;

public interface SchedulableStudent {
    Optional<StudyPlan> obtenerPlanEstudioActual(String studentId);
    List<StudyPlan> obtenerHistorialPlanesEstudio(String studentId);
    Optional<StudyPlan> obtenerPlanEstudioPorSemestre(String studentId, int semestre);

    List<SubjectDecorator> obtenerMateriasPorSemestre(String studentId, int semestre);
    List<SubjectDecorator> obtenerMateriasPorColor(String studentId, String color);
}
