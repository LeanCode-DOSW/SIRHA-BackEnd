package edu.dosw.sirha.sirha_backend;


import edu.dosw.sirha.sirha_backend.dto.StudentScheduleDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class StudentScheduleDTOTest {

    @Test
    void testConstructorAndGetters() {
        StudentScheduleDTO dto = new StudentScheduleDTO(1L, Arrays.asList("Math", "Science"));

        assertEquals(1L, dto.getStudentId());
        assertEquals(2, dto.getSubjects().size());
        assertTrue(dto.getSubjects().contains("Math"));
    }

    @Test
    void testSetters() {
        StudentScheduleDTO dto = new StudentScheduleDTO();
        dto.setStudentId(2L);
        dto.setSubjects(Arrays.asList("History"));

        assertEquals(2L, dto.getStudentId());
        assertEquals(1, dto.getSubjects().size());
        assertEquals("History", dto.getSubjects().get(0));
    }
}
