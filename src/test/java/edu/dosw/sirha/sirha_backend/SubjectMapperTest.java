package edu.dosw.sirha.sirha_backend;

import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.PrerequisiteRule;
import edu.dosw.sirha.sirha_backend.dto.SubjectDTO;
import edu.dosw.sirha.sirha_backend.util.SubjectMapper;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubjectMapperTest {

    @Test
    void toEntity_NullDto_ReturnsNull() {
        assertNull(SubjectMapper.toEntity(null));
    }

    @Test
    void toEntity_MapsFieldsCorrectly() {
        List<Group> emptyGroups = new ArrayList<>();
        List<PrerequisiteRule> emptyPrereqs = new ArrayList<>();
        SubjectDTO dto = new SubjectDTO("Matemáticas", 4, emptyGroups, emptyPrereqs);

        Subject subject = SubjectMapper.toEntity(dto);
        assertNotNull(subject);
        assertEquals("Matemáticas", subject.getName());
        assertEquals(4, subject.getCredits());
        assertNotNull(subject.getGroups());
        assertNotNull(subject.getPrerequisites());
        assertTrue(subject.getGroups().isEmpty());
        assertTrue(subject.getPrerequisites().isEmpty());
    }

    @Test
    void toDTO_NullSubject_ReturnsNull() {
        assertNull(SubjectMapper.toDTO(null));
    }

    @Test
    void toDTO_MapsFieldsCorrectly() {
        try {
            Subject subject = new Subject("101", "Física I", 3);
            // asignar listas (simular grupos/prerrequisitos)
            subject.setGroups(new ArrayList<>());
            subject.setPrerequisites(new ArrayList<>());

            SubjectDTO dto = SubjectMapper.toDTO(subject);
            assertNotNull(dto);
            assertEquals("Física I", dto.getName());
            assertEquals(3, dto.getCredits());
            assertNotNull(dto.getGroups());
            assertNotNull(dto.getPrerequisites());
            assertTrue(dto.getGroups().isEmpty());
            assertTrue(dto.getPrerequisites().isEmpty());
        } catch (SirhaException e) {
            fail("No se esperaba excepción al crear Subject: " + e.getMessage());
        }
    }
}