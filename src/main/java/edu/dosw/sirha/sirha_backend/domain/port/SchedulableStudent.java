package edu.dosw.sirha.sirha_backend.domain.port;

import java.util.*;

import edu.dosw.sirha.sirha_backend.domain.model.*;

public interface SchedulableStudent {
    Optional<StudyPlan> obtenerPlanEstudioActual(String studentId);
    List<StudyPlan> obtenerHistorialPlanesEstudio(String studentId);
    Optional<StudyPlan> obtenerPlanEstudioPorSemestre(String studentId, int semestre);
}
