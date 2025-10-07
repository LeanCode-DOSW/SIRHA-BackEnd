package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.util.List;
import java.util.Map;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.AcademicPeriod;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface ScheduleManager {

    boolean tieneConflictoConHorario(Group nuevoGrupo);
    
    List<Schedule> getCurrentSchedule();
    
    boolean tieneMateriasEnCurso();

    Map<AcademicPeriod, List<Schedule>> getAllSchedules();

    List<Schedule> getScheduleForPeriod(AcademicPeriod period);
}
