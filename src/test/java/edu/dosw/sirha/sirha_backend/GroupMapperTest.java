package edu.dosw.sirha.sirha_backend;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.util.GroupMapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GroupMapperTest {

    private Professor professor;
    @Test
    void testToEntity_NullDto_ReturnsNull() throws SirhaException {
        Subject subject = new Subject("Matemáticas I", "MAT101", 4);
        Group result = GroupMapper.toEntity(subject, null);
        assertNull(result);
    }

    @Test
    void testToEntity_MapsBasicFieldsAndCollections() throws SirhaException {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(5));
        Subject subject = new Subject("Física I", "FIS101", 4);
        try {
            professor = new Professor("Dr. Profesor", "profesor@universidad.edu", "hash");
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el profesor: " + e.getMessage());
        }

        GroupDTO dto = new GroupDTO(25, 0, professor, period);
        dto.setAula("A101");
        dto.setSchedules(new ArrayList<>());     // listas vacías
        dto.setEstudiantes(new ArrayList<>());

        Group group = GroupMapper.toEntity(subject, dto);

        assertNotNull(group);
        assertEquals(25, group.getCapacity());
        assertEquals(professor, group.getProfessor());
        assertEquals("A101", group.getAula());
        assertEquals(period, group.getCurrentPeriod());
        assertEquals(0, group.getInscritos());
        assertNotNull(group.getEstudiantes());
        assertTrue(group.getEstudiantes().isEmpty());
        assertNotNull(group.getSchedules());
        assertTrue(group.getSchedules().isEmpty());

        GroupDTO groupDTO = GroupMapper.toDTO(group);

        assertNotNull(groupDTO);
        assertEquals(25, groupDTO.getCapacidad());
        assertEquals(professor, groupDTO.getProfesor());
        assertEquals("A101", groupDTO.getAula());
        assertEquals(period, groupDTO.getCurrentPeriod());
        assertEquals(0, groupDTO.getInscritos());
        assertNotNull(groupDTO.getEstudiantes());
        assertTrue(groupDTO.getEstudiantes().isEmpty());
        assertNotNull(groupDTO.getSchedules());
        assertTrue(groupDTO.getSchedules().isEmpty());
    }

    @Test
    void testToEntity_EnrollsStudents() throws SirhaException {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(5));
        Subject subject = new Subject("Programación I", "PROG101", 3);

        Student student = new Student("juan.perez", "juan@universidad.edu", "password123", "2024001");

        GroupDTO dto = new GroupDTO(30, 0, professor, period);
        dto.setAula("B202");
        dto.setSchedules(new ArrayList<>());
        dto.setEstudiantes(Arrays.asList(student));

        Group group = GroupMapper.toEntity(subject, dto);

        assertNotNull(group);
        assertEquals(1, group.getInscritos());
        assertTrue(group.getEstudiantes().contains(student));
        assertEquals("B202", group.getAula());
    }
}