package edu.dosw.sirha.sirha_backend.util;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class GroupMapper {

    /**
     * Convierte GroupDTO a entidad Group.
     * Requiere Subject y AcademicPeriod como dependencias externas.
     */
    public static Group toEntity(GroupDTO dto, Subject subject, AcademicPeriod period) throws SirhaException {
        if (dto == null) {
            return null;
        }

        Group group = new Group(subject, dto.getCapacidad(), period);

        if (dto.getAula() != null) {
            group.setAula(dto.getAula());
        }

        return group;
    }

    /**
     * Convierte entidad Group a GroupDTO.
     */
    public static GroupDTO toDTO(Group entity) {
        if (entity == null) {
            return null;
        }

        GroupDTO dto = new GroupDTO(
                entity.getCapacity(),
                entity.getCurrentPeriod() != null ? entity.getCurrentPeriod().getId() : null
        );

        dto.setAula(entity.getAula());
        dto.setProfessorId(entity.getProfessor() != null ? entity.getProfessor().getId() : null);

        return dto;
    }
}