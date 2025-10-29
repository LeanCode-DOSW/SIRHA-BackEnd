package edu.dosw.sirha.sirha_backend.service;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.Semaforo;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Servicio para gestión de Planes de Estudio.
 * Responsable de todas las operaciones relacionadas con StudyPlan.
 */
public interface StudyPlanService {
    // CRUD básico
    StudyPlan saveStudyPlan(StudyPlan studyPlan) throws SirhaException;
    StudyPlan getStudyPlanByName(String name) throws SirhaException;
    StudyPlan getStudyPlanById(String id) throws SirhaException;
    List<StudyPlan> getAllStudyPlans() throws SirhaException;
    StudyPlan deleteStudyPlan(String name) throws SirhaException;

    // Factory method
    StudyPlan createStudyPlan(Careers career) throws SirhaException;

    // Operaciones de negocio
    StudyPlan addSubjectToStudyPlan(String studyPlanName, String subjectName) throws SirhaException;
    StudyPlan removeSubjectFromStudyPlan(String studyPlanName, String subjectName) throws SirhaException;

    // Queries
    List<StudyPlan> getStudyPlansByCareer(Careers career) throws SirhaException;
    boolean existsByName(String name);

    // ✅ NUEVO: Obtener materias completas de un plan
    List<Subject> getSubjectsOfStudyPlan(String studyPlanName) throws SirhaException;
    int calculateTotalCredits(String studyPlanName) throws SirhaException;
    /**
     * Crea un Semaforo inicializado para un estudiante.
     * @param studyPlanId ID del plan de estudios
     * @return Semaforo inicializado con todas las materias
     */
    Semaforo createSemaforoForStudent(String studyPlanId) throws SirhaException;
}