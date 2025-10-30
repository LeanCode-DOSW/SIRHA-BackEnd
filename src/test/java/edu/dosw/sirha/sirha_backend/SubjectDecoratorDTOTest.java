package edu.dosw.sirha.sirha_backend;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubjectDecoratorDTOTest {

    @Test
    void gettersSettersAndToString() {
        SubjectDecoratorDTO dto = new SubjectDecoratorDTO();
        dto.setId("sub-1");
        dto.setName("Matemáticas");
        dto.setCredits(4);
        dto.setSemester(2);
        dto.setSemaforoColor(SemaforoColores.VERDE);
        dto.setGrade(85);

        assertEquals("sub-1", dto.getId());
        assertEquals("Matemáticas", dto.getName());
        assertEquals(4, dto.getCredits());
        assertEquals(2, dto.getSemester());
        assertEquals(SemaforoColores.VERDE, dto.getSemaforoColor());
        assertEquals(85, dto.getGrade());

        String s = dto.toString();
        assertTrue(s.contains("Matemáticas"));
        assertTrue(s.contains("VERDE") || s.contains("VERDE")); // ensure enum appears somehow
    }
}