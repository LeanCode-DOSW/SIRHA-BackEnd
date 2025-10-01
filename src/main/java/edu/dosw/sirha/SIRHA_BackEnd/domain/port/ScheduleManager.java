package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface ScheduleManager {

    // Verificación de conflictos
    boolean tieneConflictoConHorario(Group nuevoGrupo);
    
    // Consulta de horarios
    List<Schedule> getHorariosActuales();
    
    // Estado de inscripción
    boolean tieneMateriasEnCurso();
}
