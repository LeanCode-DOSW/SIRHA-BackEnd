package edu.dosw.sirha.SIRHA_BackEnd.util;

import java.util.List;
import java.util.stream.Collectors;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.dto.ScheduleDTO;

/**
 * Utilidades para mapeo de horarios entre entidades de dominio y DTOs.
 */
public class ScheduleMapperUtils {
    
    // Constantes para evitar duplicación de literales
    private static final String SIN_ASIGNAR = "Sin asignar";
    private static final String SIN_MATERIA = "Sin materia";
    
    private ScheduleMapperUtils() {
        // Utility class - constructor privado
    }
    
    /**
     * Convierte un Schedule y SubjectDecorator a ScheduleDTO.
     */
    public static ScheduleDTO toDTO(Schedule schedule, SubjectDecorator materia) {
        Group grupo = materia.getGroup();
        
        return new ScheduleDTO(
            schedule.getDia(),
            schedule.getHoraInicio(),
            schedule.getHoraFin(),
            materia.getSubject().getId(),
            materia.getName(),
            grupo != null && grupo.getProfesor() != null ? grupo.getProfesor().getUsername() : SIN_ASIGNAR,
            grupo != null && grupo.getAula() != null ? grupo.getAula() : SIN_ASIGNAR,
            grupo != null ? grupo.getId() : null,
            materia.getCreditos()
        );
    }
    
    /**
     * Convierte un Schedule y Group a ScheduleDTO.
     */
    public static ScheduleDTO toDTO(Schedule schedule, Group grupo) {
        return new ScheduleDTO(
            schedule.getDia(),
            schedule.getHoraInicio(),
            schedule.getHoraFin(),
            grupo.getCurso() != null ? grupo.getCurso().getId() : null,
            grupo.getCurso() != null ? grupo.getCurso().getName() : SIN_MATERIA,
            grupo.getProfesor() != null ? grupo.getProfesor().getUsername() : SIN_ASIGNAR,
            grupo.getAula() != null ? grupo.getAula() : SIN_ASIGNAR,
            grupo.getId(),
            grupo.getCurso() != null ? grupo.getCurso().getCreditos() : 0
        );
    }
    
    /**
     * Convierte una lista de Schedule desde un grupo a lista de ScheduleDTO.
     */
    public static List<ScheduleDTO> toDTOList(Group grupo) {
        if (grupo == null || grupo.getHorarios() == null) {
            return List.of();
        }
        
        return grupo.getHorarios().stream()
            .map(schedule -> toDTO(schedule, grupo))
            .collect(Collectors.toList());
    }
    
    /**
     * Convierte una lista de materias con sus horarios a lista de ScheduleDTO.
     */
    public static List<ScheduleDTO> toDTOList(List<SubjectDecorator> materias) {
        if (materias == null) {
            return List.of();
        }
        
        return materias.stream()
            .filter(materia -> materia.getGroup() != null)
            .flatMap(materia -> materia.getGroup().getHorarios().stream()
                .map(schedule -> toDTO(schedule, materia)))
            .collect(Collectors.toList());
    }
}