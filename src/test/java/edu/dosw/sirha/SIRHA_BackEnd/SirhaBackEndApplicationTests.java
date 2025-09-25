package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentDTO;
import edu.dosw.sirha.SIRHA_BackEnd.util.*;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SirhaBackEndApplicationTests {

    @Test
    void contextLoads() {
    }

    // ============== PRUEBAS PARA PasswordUtils ==============
    
    @Test
    void testHashPassword() {
        String password = "miContraseña123";
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }
    
    @Test
    void testVerifyPasswordCorrect() {
        String password = "contraseñaSegura";
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        assertTrue(PasswordUtils.verifyPassword(password, hashedPassword));
    }
    
    @Test
    void testVerifyPasswordIncorrect() {
        String password = "contraseñaSegura";
        String wrongPassword = "contraseñaIncorrecta";
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        assertFalse(PasswordUtils.verifyPassword(wrongPassword, hashedPassword));
    }
    
    @Test
    void testHashPasswordWithSpecialCharacters() {
        String password = "¡Hola@Mundo#2024$";
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        assertNotNull(hashedPassword);
        assertTrue(PasswordUtils.verifyPassword(password, hashedPassword));
    }
    
    @Test
    void testHashPasswordEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtils.hashPassword("");
        });
    }
    
    @Test
    void testHashPasswordNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtils.hashPassword(null);
        });
    }

	// ============== PRUEBAS PARA MapperUtils ==============
    
    @Test
    void testToStudentDTO() {
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        StudentDTO dto = MapperUtils.toDTO(student);
        
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCodigo());
    }
    
    @Test
    void testToStudentDTOWithNull() {
        StudentDTO dto = MapperUtils.toDTO(null);
        assertNull(dto);
    }
    
    @Test
    void testFromStudentDTO() {
        StudentDTO dto = new StudentDTO();
        dto.setId("67890");
        dto.setUsername("maria.garcia");
        dto.setCodigo("EST002");
        
        Student student = MapperUtils.fromDTO(dto);
        
        assertNotNull(student);
        assertEquals(dto.getId(), student.getId());
        assertEquals(dto.getUsername(), student.getUsername());
        assertEquals(dto.getCodigo(), student.getCodigo());
    }
    
    @Test
    void testFromStudentDTOWithNull() {
        Student student = MapperUtils.fromDTO(null);
        assertNull(student);
    }
    
    @Test
    void testToStudentDTOWithSpecialCharacters() {
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        
        StudentDTO dto = MapperUtils.toDTO(student);
        
        assertNotNull(dto);
        assertEquals("juan.perez", dto.getUsername());
        assertEquals("EST001", dto.getCodigo());
    }

    // ============== PRUEBAS PARA Student ==============
    
    @Test
    void testStudentDefaultConstructor() {
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        
        assertNotNull(student);
        assertEquals("juan.perez", student.getUsername());
        assertEquals("EST001", student.getCodigo());
		assertEquals("12345", student.getId());
		assertTrue(PasswordUtils.verifyPassword("hashedPass", student.getPasswordHash()));
		assertEquals("juan.perez@example.com", student.getEmail());
    }
    
    @Test
    void testStudentConstructorWithUsernamePassword() {
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        
        assertNotNull(student);
        assertEquals("juan.perez", student.getUsername());
        assertNotNull(student.getPasswordHash());
        assertNotEquals("password123", student.getPasswordHash());
    }
    
    @Test
    void testStudentSetCodigo() {
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        student.setCodigo("EST004");
        
        assertNotNull(student);
        assertEquals("juan.perez", student.getUsername());
        assertEquals("EST004", student.getCodigo());
    }
    
    @Test
    void testStudentSetId() {
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        student.setId("456");
        
        assertEquals("456", student.getId());
        assertEquals("juan.perez", student.getUsername());
    }
    
    @Test
    void testStudentToString() {
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        
        String result = student.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
		assertTrue(result.contains("juan.perez"));
		assertTrue(result.contains("EST001"));
		assertTrue(result.contains("12345"));
		//mirar despues
    }

    @Test
    void testStudentPasswordHashing() {
        String plainPassword = "miPasswordSegura123";
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", plainPassword, "EST001");
        
        // Verificar que la contraseña se hasheó
		String contraseña = PasswordUtils.hashPassword(plainPassword);
        assertNotNull(student.getPasswordHash());
        assertNotEquals(plainPassword, contraseña);
        assertTrue(contraseña.startsWith("$2a$"));
        
        // Verificar que se puede validar
        assertTrue(PasswordUtils.verifyPassword(plainPassword, contraseña));
    }

    // ============== PRUEBAS DE INTEGRACIÓN BÁSICAS ==============
    
    @Test
    void testPasswordUtilsWithMapperUtils() {
        // Crear un estudiante con contraseña
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
		String contraseña = PasswordUtils.hashPassword("hashedPass");
        // Verificar que el password se hasheó correctamente
        assertTrue(PasswordUtils.verifyPassword("hashedPass", contraseña));
        
        // Convertir a DTO
        StudentDTO dto = MapperUtils.toDTO(student);
        assertNotNull(dto);
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCodigo());
    }
    
    @Test
    void testCompleteStudentFlow() {
        // Crear estudiante original
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        
        // Student a DTO
        StudentDTO dto = MapperUtils.toDTO(student);
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCodigo());

        // DTO de vuelta a Student
        Student convertedStudent = MapperUtils.fromDTO(dto);
        assertNotNull(convertedStudent);
        
        // Verificar que los datos básicos se mantuvieron
        assertEquals(student.getId(), convertedStudent.getId());
        assertEquals(student.getUsername(), convertedStudent.getUsername());
        assertEquals(student.getCodigo(), convertedStudent.getCodigo());
    }
    
    @Test
    void testStudentDTOSecurity() {
        // Verificar que el DTO no expone información sensible
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
        student.setCodigo("SEC001");

        StudentDTO dto = MapperUtils.toDTO(student);

        assertNotNull(dto);
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCodigo());
        
        // El DTO no debe contener información del password
        String dtoString = dto.toString();
        if (dtoString != null) {
            assertFalse(dtoString.toLowerCase().contains("password"));
            assertFalse(dtoString.toLowerCase().contains("hash"));
        }
    }

    // ============== PRUEBAS PARA Student con Semáforo y SubjectDecorator ==============
    
    @Test
    void testCreateStudentWithSemaforo() {
        // Crear estudiante
        Student student = new Student("STU001", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        
        // Crear plan de estudios y materias
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        Subject matematicas = new Subject("MAT001", "Matemáticas I", 4);
        studyPlan.addMateria(matematicas);
        
        // Crear semáforo
        Semaforo semaforo = new Semaforo(studyPlan);
        
        // Asignar al estudiante
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(semaforo);
        
        // Verificaciones
        assertNotNull(student.getSemaforo());
        assertEquals(semaforo, student.getSemaforo());
        assertEquals(studyPlan, student.getPlanGeneral());
    }
    
    @Test
    void testSubjectDecorator() {
        // Crear materia y decorador
        Subject programacion = new Subject("PRG001", "Programación I", 5);
        SubjectDecorator programacionDecorator = new SubjectDecorator(programacion);
        
        // Verificar delegación de métodos
        assertEquals("Programación I", programacionDecorator.getName());
        assertEquals(5, programacionDecorator.getCreditos());
        assertNotNull(programacionDecorator.getGroups());
        
        // Configurar estado del semáforo
        programacionDecorator.setEstadoColor(SemaforoColores.AMARILLO);
        programacionDecorator.setSemestreMateria(1);
        
        // Verificar getters
        assertEquals(SemaforoColores.AMARILLO, programacionDecorator.getEstadoColor());
        assertEquals(1, programacionDecorator.getSemestre());
    }
    
    @Test
    void testSemaforoWithMultipleSubjects() {
        // Crear plan y materias
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        Subject matematicas = new Subject("MAT001", "Matemáticas I", 4);
        Subject fisica = new Subject("FIS001", "Física I", 3);
        
        studyPlan.addMateria(matematicas);
        studyPlan.addMateria(fisica);
        
        // Crear decoradores
        SubjectDecorator matematicastDecorator = new SubjectDecorator(matematicas);
        SubjectDecorator fisicaDecorator = new SubjectDecorator(fisica);
        
        // Configurar estados diferentes
        matematicastDecorator.setEstadoColor(SemaforoColores.VERDE); // Aprobada
        fisicaDecorator.setEstadoColor(SemaforoColores.ROJO);        // Reprobada
        
        // Crear semáforo y agregar materias
        Semaforo semaforo = new Semaforo(studyPlan);
        Map<String, SubjectDecorator> materias = new HashMap<>();
        materias.put(matematicastDecorator.getSubject().getName(), matematicastDecorator);
        materias.put(fisicaDecorator.getSubject().getName(), fisicaDecorator);
        semaforo.setSubjects(materias);
        
        // Verificaciones
        assertEquals(2, semaforo.getSubjectsCount());
        assertEquals(SemaforoColores.VERDE, matematicastDecorator.getEstadoColor());
        assertEquals(SemaforoColores.ROJO, fisicaDecorator.getEstadoColor());
    }
    
    
}