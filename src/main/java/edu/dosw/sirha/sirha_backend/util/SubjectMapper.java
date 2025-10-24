package edu.dosw.sirha.sirha_backend.util;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.dto.SubjectDTO;

public class SubjectMapper {
    
    private SubjectMapper() {
    }
    
    /**
     * Convierte de SubjectDTO a Subject (entidad)
     */
    public static Subject toEntity(SubjectDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Subject subject = new Subject(
            dto.getName(),
            dto.getCredits()
        );
        subject.setPrerequisites(dto.getPrerequisites());
        subject.setGroups(dto.getGroups());
        return subject;
    }
    
    /**
     * Convierte de Subject (entidad) a SubjectDTO
     */
    public static SubjectDTO toDTO(Subject subject) {
        if (subject == null) {
            return null;
        }
        
        return new SubjectDTO(
            subject.getName(),
            subject.getCredits(),
            subject.getGroups(),
            subject.getPrerequisites()
        );
    }
}