package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.SIRHA_BackEnd.dto.ScheduleDTO;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentScheduleDTO;

/**
 * Servicio para la gestión y consulta de horarios de estudiantes.
 * 
 * Proporciona funcionalidades para consultar:
 * - Horario actual del estudiante
 * - Horarios de semestres anteriores
 * - Horarios por materia específica
 * - Conflictos de horarios
 */
public interface ScheduleService {
    
    /**
     * Obtiene el horario actual del estudiante (materias en curso).
     * 
     * @param studentId ID del estudiante
     * @return horario actual con todas las materias en curso
     */
    Optional<StudentScheduleDTO> getHorarioActual(String studentId);
    
    /**
     * Obtiene el horario de un semestre específico del estudiante.
     * 
     * @param studentId ID del estudiante
     * @param semestre número del semestre a consultar
     * @return horario del semestre especificado
     */
    Optional<StudentScheduleDTO> getHorarioPorSemestre(String studentId, int semestre);
    
    /**
     * Obtiene todos los horarios históricos del estudiante.
     * 
     * @param studentId ID del estudiante
     * @return lista de horarios por semestre
     */
    List<StudentScheduleDTO> getHistorialHorarios(String studentId);
    
    /**
     * Obtiene los horarios de todas las materias que está cursando actualmente.
     * 
     * @param studentId ID del estudiante
     * @return lista de horarios de materias en curso
     */
    List<ScheduleDTO> getHorariosMateriasEnCurso(String studentId);
    
    /**
     * Verifica si un estudiante tiene conflictos de horario con un nuevo grupo.
     * 
     * @param studentId ID del estudiante
     * @param grupoId ID del grupo a verificar
     * @return true si hay conflicto, false en caso contrario
     */
    boolean tieneConflictoHorario(String studentId, String grupoId);
    
    /**
     * Obtiene el horario de un grupo específico.
     * 
     * @param grupoId ID del grupo
     * @return lista de horarios del grupo
     */
    List<ScheduleDTO> getHorarioGrupo(String grupoId);
    
    /**
     * Busca grupos disponibles sin conflicto de horario para un estudiante.
     * 
     * @param studentId ID del estudiante
     * @param materiaId ID de la materia
     * @return lista de grupos sin conflicto
     */
    List<String> getGruposDisponibles(String studentId, String materiaId);
}