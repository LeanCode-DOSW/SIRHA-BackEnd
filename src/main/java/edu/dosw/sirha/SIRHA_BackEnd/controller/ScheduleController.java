package edu.dosw.sirha.SIRHA_BackEnd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dosw.sirha.SIRHA_BackEnd.dto.ScheduleDTO;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentScheduleDTO;
import edu.dosw.sirha.SIRHA_BackEnd.service.ScheduleService;


@RestController
@RequestMapping("/api/students/{studentId}/schedule")
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    /**
     * Obtiene el horario actual del estudiante (materias en curso).
     * 
     * @param studentId ID del estudiante
     * @return horario actual con todas las materias en curso
     */
    @GetMapping("/current")
    public ResponseEntity<StudentScheduleDTO> getHorarioActual(@PathVariable String studentId) {
        Optional<StudentScheduleDTO> horario = scheduleService.getHorarioActual(studentId);
        
        return horario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene el horario de un semestre específico.
     * 
     * @param studentId ID del estudiante
     * @param semestre número del semestre
     * @return horario del semestre especificado
     */
    @GetMapping("/semester/{semestre}")
    public ResponseEntity<StudentScheduleDTO> getHorarioPorSemestre(
            @PathVariable String studentId, 
            @PathVariable int semestre) {
        
        Optional<StudentScheduleDTO> horario = scheduleService.getHorarioPorSemestre(studentId, semestre);
        
        return horario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene el historial completo de horarios del estudiante.
     * 
     * @param studentId ID del estudiante
     * @return lista de horarios por semestre
     */
    @GetMapping("/history")
    public ResponseEntity<List<StudentScheduleDTO>> getHistorialHorarios(@PathVariable String studentId) {
        List<StudentScheduleDTO> historial = scheduleService.getHistorialHorarios(studentId);
        
        if (historial.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(historial);
    }
    
    /**
     * Obtiene solo los horarios de las materias que está cursando actualmente.
     * 
     * @param studentId ID del estudiante
     * @return lista de horarios de materias en curso
     */
    @GetMapping("/current/classes")
    public ResponseEntity<List<ScheduleDTO>> getHorariosMateriasEnCurso(@PathVariable String studentId) {
        List<ScheduleDTO> horarios = scheduleService.getHorariosMateriasEnCurso(studentId);
        
        if (horarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(horarios);
    }
    
    /**
     * Verifica si existe conflicto de horario con un grupo específico.
     * 
     * @param studentId ID del estudiante
     * @param grupoId ID del grupo a verificar
     * @return true si hay conflicto, false en caso contrario
     */
    @GetMapping("/conflict-check")
    public ResponseEntity<Boolean> verificarConflictoHorario(
            @PathVariable String studentId,
            @RequestParam String grupoId) {
        
        boolean tieneConflicto = scheduleService.tieneConflictoHorario(studentId, grupoId);
        return ResponseEntity.ok(tieneConflicto);
    }
    
    /**
     * Obtiene grupos disponibles sin conflicto para una materia específica.
     * 
     * @param studentId ID del estudiante
     * @param materiaId ID de la materia
     * @return lista de IDs de grupos disponibles
     */
    @GetMapping("/available-groups")
    public ResponseEntity<List<String>> getGruposDisponibles(
            @PathVariable String studentId,
            @RequestParam String materiaId) {
        
        List<String> gruposDisponibles = scheduleService.getGruposDisponibles(studentId, materiaId);
        
        if (gruposDisponibles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(gruposDisponibles);
    }
}

/**
 * Controlador adicional para consultas de horarios de grupos.
 */
@RestController
@RequestMapping("/api/groups")
class GroupScheduleController {
    
    private final ScheduleService scheduleService;
    
    public GroupScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    /**
     * Obtiene el horario de un grupo específico.
     * 
     * @param grupoId ID del grupo
     * @return horarios del grupo
     */
    @GetMapping("/{grupoId}/schedule")
    public ResponseEntity<List<ScheduleDTO>> getHorarioGrupo(@PathVariable String grupoId) {
        List<ScheduleDTO> horarios = scheduleService.getHorarioGrupo(grupoId);
        
        if (horarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(horarios);
    }
}