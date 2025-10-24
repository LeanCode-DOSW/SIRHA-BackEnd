package edu.dosw.sirha.sirha_backend.util;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class GroupMapper {
    private GroupMapper() {
    }
    
    /**
     * Convierte de GroupDTO a Group (entidad)
     */
    public static Group toEntity(Subject subject, GroupDTO dto) throws SirhaException {
        if (dto == null) {
            return null;
        }
        Group group = new Group(subject, dto.getCapacidad()
                            , dto.getCurrentPeriod());
            group.setProfessor(dto.getProfesor());
            group.setAula(dto.getAula());
            for (Schedule schedule : dto.getSchedules()) {
                group.addSchedule(schedule);
            }

            for (Student student : dto.getEstudiantes()) {
            group.enrollStudent(student);
            }
            return group;
    }
    
    /**
     * Convierte de Group (entidad) a GroupDTO
     */
    public static GroupDTO toDTO(Group group) {
        if (group == null) {
            return null;
        }
        GroupDTO dto = new GroupDTO(
            group.getCapacity(),
            group.getInscritos(),
            group.getProfessor(),
            group.getCurrentPeriod()
        );
        dto.setAula(group.getAula());
        dto.setSchedules(group.getSchedules());
        dto.setEstudiantes(group.getEstudiantes());
        return dto;
    }
}