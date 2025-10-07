package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface ScheduleManager {

    boolean tieneConflictoConHorario(Group nuevoGrupo);
    
    List<Schedule> getHorariosActuales();
    
    boolean tieneMateriasEnCurso();
}
