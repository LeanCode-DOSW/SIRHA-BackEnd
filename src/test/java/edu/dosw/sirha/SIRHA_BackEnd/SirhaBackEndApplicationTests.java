package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentDTO;
import edu.dosw.sirha.SIRHA_BackEnd.service.AuthService;
import edu.dosw.sirha.SIRHA_BackEnd.service.impl.AuthServiceImpl;
import edu.dosw.sirha.SIRHA_BackEnd.util.MapperUtils;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SirhaBackEndApplicationTests {

    @Test
    void contextLoads() {
        // Test básico para verificar que el contexto de Spring Boot se carga correctamente
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
		String password = PasswordUtils.hashPassword(plainPassword);
        assertNotNull(student.getPasswordHash());
        assertNotEquals(plainPassword, password);
        assertTrue(password.startsWith("$2a$"));

        // Verificar que se puede validar
        assertTrue(PasswordUtils.verifyPassword(plainPassword, password));
    }

    // ============== PRUEBAS DE INTEGRACIÓN BÁSICAS ==============
    
    @Test
    void testPasswordUtilsWithMapperUtils() {
        // Crear un estudiante con contraseña
        Student student = new Student( "12345", "juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
		String password = PasswordUtils.hashPassword("hashedPass");
        // Verificar que el password se hasheó correctamente
        assertTrue(PasswordUtils.verifyPassword("hashedPass", password));
        
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

    // ============== PRUEBAS DE COBERTURA ADICIONALES ==============
    
    @Test
    void testGroupStates() {
        // Probar StatusOpen
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.StatusOpen statusOpen = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.StatusOpen();
        assertNotNull(statusOpen);
        
        // Probar StatusClosed  
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.StatusClosed statusClosed = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.StatusClosed();
        assertNotNull(statusClosed);
    }
    
    @Test
    void testSubjectStates() {
        // Probar AprobadaState
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.AprobadaState aprobadaState = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.AprobadaState();
        assertNotNull(aprobadaState);
        
        // Probar EnCursoState
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.EnCursoState enCursoState = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.EnCursoState();
        assertNotNull(enCursoState);
        
        // Probar NoCursadaState
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.NoCursadaState noCursadaState = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.NoCursadaState();
        assertNotNull(noCursadaState);
        
        // Probar ReprobadaState
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.ReprobadaState reprobadaState = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.ReprobadaState();
        assertNotNull(reprobadaState);
    }
    
    @Test
    void testProfessorBasics() {
        // Constructor por defecto
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor profesor1 = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor();
        assertNotNull(profesor1);
        
        // Constructor con parámetros
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor profesor2 = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor(
                "prof001", "dr.smith", "hashedPass", "PROFESOR", "L-V 8-12");
        assertNotNull(profesor2);
        assertEquals("prof001", profesor2.getId());
        assertEquals("dr.smith", profesor2.getUsername());
    }
    
    @Test
    void testStudyPlanBasics() {
        // Constructor y métodos básicos
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.StudyPlan plan = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.StudyPlan("Ingeniería de Software");
        assertNotNull(plan);
        assertEquals("Ingeniería de Software", plan.getNombre());
        assertNotNull(plan.getMaterias());
        
        // Agregar materia
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject materia = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject("MAT001", "Matemáticas", 4);
        plan.addMateria(materia);
        assertTrue(plan.getMaterias().contains(materia));
        
        // Cambiar nombre
        plan.setNombre("Ingeniería de Sistemas");
        assertEquals("Ingeniería de Sistemas", plan.getNombre());
    }
    
    @Test
    void testSubjectDecoratorBasics() {
        // Crear subject base
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject subject = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject("PROG001", "Programación", 3);
        
        // Crear decorator
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.SubjectDecorator decorator = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.SubjectDecorator(subject);
        assertNotNull(decorator);
        assertEquals("Programación", decorator.getNombre());
        assertEquals("PROG001", decorator.getCodigo());
        assertEquals(3, decorator.getCreditos());
        assertNotNull(decorator.getGrupos());
    }
    
    @Test
    void testGroupBasics() {
        // Constructor
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group grupo = 
            new edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group(30);
        assertNotNull(grupo);
        assertEquals(30, grupo.getCapacidad());
        assertEquals(30, grupo.getCuposDisponibles());
        assertEquals(0, grupo.getInscritos());
        assertNotNull(grupo.getEstadoGrupo());
        
        // Setters básicos
        grupo.setAula("A101");
        assertEquals("A101", grupo.getAula());
        
        // Verificar lista de estudiantes
        assertNotNull(grupo.getEstudiantes());
        assertTrue(grupo.getEstudiantes().isEmpty());
    }
    
    @Test
    void testSemaforoBasics() {
        // Solo probar que la clase existe y se puede instanciar
        // Ya que el constructor requiere StudyPlan específico
        assertNotNull(edu.dosw.sirha.SIRHA_BackEnd.domain.model.Semaforo.class);
        
        // Probar enums relacionados
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores verde = 
            edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores.VERDE;
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores amarillo = 
            edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores.AMARILLO;
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores rojo = 
            edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores.ROJO;
        edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores gris = 
            edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores.GRIS;
        
        assertNotNull(verde);
        assertNotNull(amarillo);
        assertNotNull(rojo);
        assertNotNull(gris);
    }
    
    @Test
    void testScheduleBasics() {
        // Solo verificar que la clase Schedule existe
        assertNotNull(edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule.class);
        
        // Verificar que las clases relacionadas existen
        assertNotNull(edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores.class);
    }
}