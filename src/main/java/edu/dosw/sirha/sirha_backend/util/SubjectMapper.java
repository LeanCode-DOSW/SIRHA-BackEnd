package edu.dosw.sirha.sirha_backend.util;

import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.dto.SubjectDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class SubjectMapper {

    public static Subject toEntity(SubjectDTO dto) throws SirhaException {
        if (dto == null) {
            return null;
        }
        return new Subject(dto.getName(), dto.getCredits());
    }

    public static SubjectDTO toDTO(Subject entity) {
        if (entity == null) {
            return null;
        }

        SubjectDTO dto = new SubjectDTO(
                entity.getName(),
                entity.getCredits()
        );

        // Si necesitas incluir los IDs de grupos
        if (entity.getGroups() != null) {
            dto.setGroupIds(
                    entity.getGroups().stream()
                            .map(group -> group.getId())
                            .toList()
            );
        }

        return dto;
    }
}