package edu.dosw.sirha.sirha_backend.domain.port;

import java.util.List;
import java.util.Map;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface ScheduleManager {

    boolean hasScheduleConflictWith(Group nuevoGrupo) throws SirhaException;
    
    List<Schedule> getCurrentSchedule();
    
    boolean hasCoursesInProgress();

    Map<AcademicPeriod, List<Schedule>> getAllSchedules();

    List<Schedule> getScheduleForPeriod(AcademicPeriod period);
}
