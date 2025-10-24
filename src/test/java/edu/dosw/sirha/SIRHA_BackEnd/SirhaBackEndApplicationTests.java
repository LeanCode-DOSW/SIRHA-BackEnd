package edu.dosw.sirha.sirha_backend;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.StatusClosed;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.StatusOpen;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.*;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SirhaBackEndApplicationTests {
    
    private Student student;
    private Student student2;
    private Student student3;
    private Professor professor;
    private AcademicPeriod academicPeriod;
    private Subject matematicas;
    private Subject fisica;
    private Subject quimica;
    private Subject programacion;
    private Group grupo1;
    private Group grupo2;
    private Group grupo3;
    private Schedule schedule1;
    private Schedule schedule2;
    private StudyPlan studyPlan;
    private Semaforo semaforo;
    private SubjectDecorator matemDecorator;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        
        academicPeriod.setStartDatesInscripciones(LocalDate.now(), LocalDate.now().plusMonths(1));
        try {
            student = new Student("juan.perez", "juan.perez@example.com", "hashedPass", "EST001");
            student.setId("12345");
            student2 = new Student("maria.garcia", "maria.garcia@example.com", "hashedPass2", "EST002");
            student2.setId("67890");
            student3 = new Student("carlos.lopez", "carlos.lopez@example.com", "hashedPass3", "EST003");
            student3.setId("11111");
            
            professor = new Professor("dr.smith", "dr.smith@example.com", "hashedPass");
            professor.setId("1");
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los usuarios: " + e.getMessage());
        }
        matematicas = new Subject("101", "Matemáticas I", 4);
        fisica = new Subject("102", "Física I", 3);
        quimica = new Subject("103", "Química I", 4);
        programacion = new Subject("CS101", "Programación I", 5);
        
        try {
            grupo1 = new Group(matematicas,30, academicPeriod);
            grupo1.setId("1");
            grupo1.setAula("A101");
            grupo1.setProfessor(professor);
            
            grupo2 = new Group(fisica, 25, academicPeriod);
            grupo2.setId("2");
            grupo2.setAula("B202");
            grupo2.setProfessor(professor);
            
            grupo3 = new Group(quimica, 35, academicPeriod);
            grupo3.setId("3");
            grupo3.setAula("C303");
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
        try {
            schedule1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            schedule2 = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los horarios: " + e.getMessage());
        }
        try {
            grupo1.addSchedule(schedule1);
            grupo2.addSchedule(schedule2);
        } catch (Exception e) {
            fail("No se esperaba una excepción al agregar horarios: " + e.getMessage());
        }        
        studyPlan = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
        studyPlan.addSubject(matematicas);
        studyPlan.addSubject(fisica);
        studyPlan.addSubject(quimica);
        studyPlan.addSubject(programacion);
        
        matematicas.addGroup(grupo1);
        fisica.addGroup(grupo2);
        quimica.addGroup(grupo3);
        
        
        try {
            semaforo = new Semaforo(studyPlan);
            matemDecorator = new SubjectDecorator(matematicas);
            student.setAcademicProgress(semaforo);
            student.setCurrentPeriod(academicPeriod);
        } catch (Exception e) {
            fail("No se esperaba una excepción al establecer el período actual: " + e.getMessage());
        }
        studentDTO = new StudentDTO(
            "67890",
            "maria.garcia",
            "maria.garcia@example.com",
            "EST002",
            Careers.INGENIERIA_DE_SISTEMAS
        );
    }

    @Test
    void contextLoads() {
        // Test básico para verificar que el contexto de Spring Boot se carga correctamente
    }

    
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
        StudentDTO dto = StudentMapper.toDTO(student);
        
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCode());
    }
    
    @Test
    void testToStudentDTOWithNull() {
        StudentDTO dto = StudentMapper.toDTO(null);
        assertNull(dto);
    }
    
    @Test
    void testFromStudentDTO() {
        try {
            Student convertedStudent = StudentMapper.fromDTOnewStudent(studentDTO);
        
            assertNotNull(convertedStudent);
            assertEquals(studentDTO.getUsername(), convertedStudent.getUsername());
            assertEquals(studentDTO.getCode(), convertedStudent.getCodigo());
        } catch (Exception e) {
            fail("No se esperaba una excepción al convertir de DTO a entidad: " + e.getMessage());
        }
    }
    
    @Test
    void testFromStudentDTOWithNull() {
        try {
            Student convertedStudent = StudentMapper.fromDTOnewStudent(null);
            assertNull(convertedStudent);
        } catch (Exception e) {
            fail("No se esperaba una excepción al convertir de DTO a entidad: " + e.getMessage());
        }
    }
    
    @Test
    void testToStudentDTOWithSpecialCharacters() {
        StudentDTO dto = StudentMapper.toDTO(student);
        
        assertNotNull(dto);
        assertEquals("juan.perez", dto.getUsername());
        assertEquals("EST001", dto.getCode());
    }

    // ============== PRUEBAS PARA Student ==============
    
    @Test
    void testStudentDefaultConstructor() {
        assertNotNull(student);
        assertEquals("juan.perez", student.getUsername());
        assertEquals("EST001", student.getCodigo());
        assertEquals("12345", student.getId());
        assertTrue(PasswordUtils.verifyPassword("hashedPass", student.getPasswordHash()));
        assertEquals("juan.perez@example.com", student.getEmail());
    }
    
    @Test
    void testStudentConstructorWithUsernamePassword() {
        assertNotNull(student);
        assertEquals("juan.perez", student.getUsername());
        assertNotNull(student.getPasswordHash());
        assertNotEquals("password123", student.getPasswordHash());
    }
    
    @Test
    void testStudentSetCodigo() {
        try {
            student.setCodigo("EST004");
        
            assertNotNull(student);
            assertEquals("juan.perez", student.getUsername());
            assertEquals("EST004", student.getCodigo());
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al establecer el código: " + e.getMessage());
        }
    }
    
    @Test
    void testStudentSetId() {
        student.setId("456");
        
        assertEquals("456", student.getId());
        assertEquals("juan.perez", student.getUsername());
    }
    
    @Test
    void testStudentToString() {
        String result = student.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("juan.perez"));
        assertTrue(result.contains("EST001"));
        assertTrue(result.contains("12345"));
    }

    @Test
    void testStudentPasswordHashing() {
        String plainPassword = "miPasswordSegura123";
        try {
            Student newStudent = new Student("test.user", "test@example.com", plainPassword, "TEST001");
            
            String password = PasswordUtils.hashPassword(plainPassword);
            assertNotNull(newStudent.getPasswordHash());
            assertNotEquals(plainPassword, password);
            assertTrue(password.startsWith("$2a$"));

            assertTrue(PasswordUtils.verifyPassword(plainPassword, password));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear un nuevo estudiante: " + e.getMessage());
        }
    }

    // ============== PRUEBAS DE INTEGRACIÓN BÁSICAS ==============
    
    @Test
    void testPasswordUtilsWithMapperUtils() {
        String password = PasswordUtils.hashPassword("hashedPass");
        assertTrue(PasswordUtils.verifyPassword("hashedPass", password));
        
        StudentDTO dto = StudentMapper.toDTO(student);
        assertNotNull(dto);
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCode());
    }
    
    @Test
    void testCompleteStudentFlow() {
        StudentDTO dto = StudentMapper.toDTO(student);
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCode());

        try { 
            Student convertedStudent = StudentMapper.fromDTOnewStudent(dto);
            assertNotNull(convertedStudent);

            assertEquals(student.getUsername(), convertedStudent.getUsername());
            assertEquals(student.getCodigo(), convertedStudent.getCodigo());
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al convertir de DTO a Student: " + e.getMessage());
        }
    }
    
    @Test
    void testStudentDTOSecurity() {
        try {
            student.setCodigo("SEC001");
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al establecer el código: " + e.getMessage());
        }
        StudentDTO dto = StudentMapper.toDTO(student);

        assertNotNull(dto);
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getCodigo(), dto.getCode());
        
        String dtoString = dto.toString();
        if (dtoString != null) {
            assertFalse(dtoString.toLowerCase().contains("password"));
            assertFalse(dtoString.toLowerCase().contains("hash"));
        }
    }

    // ============== PRUEBAS PARA Student con Semáforo y SubjectDecorator ==============
    
    @Test
    void testCreateStudentWithSemaforo() {
        assertNotNull(student.getAcademicProgress());
        assertEquals(semaforo, student.getAcademicProgress());
        assertEquals(studyPlan, student.getPlanGeneral());
    }

    
    
    // ============== PRUEBAS DE COBERTURA ADICIONALES ==============
    
    @Test
    void testGroupStates() {
        StatusOpen statusOpen = new StatusOpen();
        assertNotNull(statusOpen);
        
        StatusClosed statusClosed = new StatusClosed();
        assertNotNull(statusClosed);
    }
    
    
    
    @Test
    void testProfessorBasics() {
        Professor profesor1 = new Professor();
        assertNotNull(profesor1);

        assertNotNull(professor);
        assertEquals("1", professor.getId());
        assertEquals("dr.smith", professor.getUsername());
    }
    
    @Test
    void testStudyPlanBasics() {
        assertNotNull(studyPlan);
        assertEquals("Ingeniería de Sistemas", studyPlan.getName());
        assertNotNull(studyPlan.getSubjects());

        assertTrue(studyPlan.getSubjects().containsKey(matematicas.getName()));

        studyPlan.setName("Ingeniería de Software");
        assertEquals("Ingeniería de Software", studyPlan.getName());
    }
    @Test
    void testSubjectStates() {
        AprobadaState aprobadaState = new AprobadaState();
        assertNotNull(aprobadaState);
        
        EnCursoState enCursoState = new EnCursoState();
        assertNotNull(enCursoState);
        
        NoCursadaState noCursadaState = new NoCursadaState();
        assertNotNull(noCursadaState);
        
        ReprobadaState reprobadaState = new ReprobadaState();
        assertNotNull(reprobadaState);
    }
    @Test
    void testSubjectDecoratorBasics() {
        assertNotNull(matemDecorator);
        assertEquals("Matemáticas I", matemDecorator.getName());
        assertEquals("101", matemDecorator.getId());
        assertEquals(4, matemDecorator.getCredits());
        assertNotNull(matemDecorator.getGroups());
    }
    
    @Test
    void testGroupBasics() {
        assertNotNull(grupo1);
        assertEquals(30, grupo1.getCapacity());
        assertEquals(30, grupo1.getCuposDisponibles());
        assertEquals(0, grupo1.getInscritos());
        assertNotNull(grupo1.getGroupState());
        assertEquals("A101", grupo1.getAula());
        assertNotNull(grupo1.getEstudiantes());
        assertTrue(grupo1.getEstudiantes().isEmpty());
    }
    
    @Test
    void testSemaforoBasics() {
        SemaforoColores verde = SemaforoColores.VERDE;
        SemaforoColores amarillo = SemaforoColores.AMARILLO;
        SemaforoColores rojo = SemaforoColores.ROJO;
        SemaforoColores gris = SemaforoColores.GRIS;

        assertNotNull(verde);
        assertNotNull(amarillo);
        assertNotNull(rojo);
        assertNotNull(gris);
    }
    

    @Test
    void testAcademicPeriod() {
        assertTrue(academicPeriod.isActive());
        assertEquals(academicPeriod.getId(), grupo1.getCurrentPeriod().getId());
    }


    @Test
    void testNoCursadaStateTransitions() {
        assertTrue(matemDecorator.getState() instanceof NoCursadaState);
        assertEquals(SemaforoColores.GRIS, matemDecorator.getEstadoColor());
        
        try {
            matemDecorator.inscribir(grupo1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia: " + e.getMessage());
        }
        assertTrue(matemDecorator.getState() instanceof EnCursoState);
        assertEquals(SemaforoColores.AMARILLO, matemDecorator.getEstadoColor());
    }

    @Test
    void testEnCursoStateTransitions() {
        try {
            matemDecorator.inscribir(grupo1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia: " + e.getMessage());
        }
        assertTrue(matemDecorator.getState() instanceof EnCursoState);
        
        try {
            matemDecorator.aprobar();
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al aprobar la materia: " + e.getMessage());
        }
        assertTrue(matemDecorator.getState() instanceof AprobadaState);
        assertEquals(SemaforoColores.VERDE, matemDecorator.getEstadoColor());
    }

    @Test
    void testEnCursoStateToReprobada2() {
        try {
            matemDecorator.inscribir(grupo1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia: " + e.getMessage());
        }
        try {
            matemDecorator.reprobar();
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al reprobar la materia: " + e.getMessage());
        }
        assertTrue(matemDecorator.getState() instanceof ReprobadaState);
        assertEquals(SemaforoColores.ROJO, matemDecorator.getEstadoColor());
    }

    @Test
    void testEnCursoStateRetirar2() {
        try {
            matemDecorator.inscribir(grupo1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia: " + e.getMessage());
        }
        assertTrue(matemDecorator.getState() instanceof EnCursoState);
        
        try {
            matemDecorator.retirar();
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al retirar la materia: " + e.getMessage());
        }
        assertTrue(matemDecorator.getState() instanceof NoCursadaState);
        assertEquals(SemaforoColores.GRIS, matemDecorator.getEstadoColor());
    }

    @Test
    void testAprobadaStateImmutable() {
        try {
            matemDecorator.inscribir(grupo1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia: " + e.getMessage());
        }
        try {
            matemDecorator.aprobar();
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al aprobar la materia: " + e.getMessage());
        }

        assertThrows(SirhaException.class, () -> matemDecorator.inscribir(grupo1));
        assertThrows(SirhaException.class, () -> matemDecorator.reprobar());
        assertThrows(SirhaException.class, () -> matemDecorator.retirar());
    }

    @Test
    void testSubjectDecoratorStateValidations() {
        assertTrue(matemDecorator.canEnroll());

        try {
            matemDecorator.inscribir(grupo1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia: " + e.getMessage());
        }
        assertFalse(matemDecorator.canEnroll());
        assertTrue(matemDecorator.estaCursando());
        
        try {
            matemDecorator.aprobar();
        } catch (SirhaException e) {
            fail("No se esperaba una excepción al aprobar la materia: " + e.getMessage());
        }
        assertFalse(matemDecorator.estaCursando());
        assertFalse(matemDecorator.canEnroll());
    }

    @Test
    void testCompleteSubjectLifecycle() {
        
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertTrue(decorator.getState() instanceof NoCursadaState);
            assertTrue(decorator.canEnroll());

            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

            Group grupo = new Group(matematicas,30, academicPeriod);
            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            assertTrue(decorator.estaCursando());
            
            decorator.retirar();
            assertTrue(decorator.getState() instanceof NoCursadaState);

            Group grupo12 = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo12);
            assertTrue(decorator.getState() instanceof EnCursoState);
            
            decorator.aprobar();
            assertTrue(decorator.getState() instanceof AprobadaState);
            assertTrue(decorator.estaAprobada());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testFailureAndRetryLifecycle() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.reprobar();
            assertTrue(decorator.getState() instanceof ReprobadaState);
            assertTrue(decorator.estaReprobada());

            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            
            decorator.aprobar();
            assertTrue(decorator.getState() instanceof AprobadaState);
            assertTrue(decorator.estaAprobada());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateTransitionHistory() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            List<Class<?>> stateHistory = new ArrayList<>();
            
            stateHistory.add(decorator.getState().getClass());
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            stateHistory.add(decorator.getState().getClass());
            decorator.retirar();
            stateHistory.add(decorator.getState().getClass());
            decorator.inscribir(grupo);
            stateHistory.add(decorator.getState().getClass());
            decorator.reprobar();
            stateHistory.add(decorator.getState().getClass());
            
            assertEquals(NoCursadaState.class, stateHistory.get(0));
            assertEquals(EnCursoState.class, stateHistory.get(1));
            assertEquals(NoCursadaState.class, stateHistory.get(2));
            assertEquals(EnCursoState.class, stateHistory.get(3));
            assertEquals(ReprobadaState.class, stateHistory.get(4));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateColorConsistency() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertEquals(SemaforoColores.GRIS, decorator.getEstadoColor());

            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
            
            decorator.retirar();
            assertEquals(SemaforoColores.GRIS, decorator.getEstadoColor());
            
            decorator.inscribir(grupo);
            decorator.aprobar();
            assertEquals(SemaforoColores.VERDE, decorator.getEstadoColor());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateColorConsistencyAfterFailure() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.reprobar();
            assertEquals(SemaforoColores.ROJO, decorator.getEstadoColor());
            assertEquals(ReprobadaState.class, decorator.getState().getClass());
            
            decorator.inscribir(grupo);
            assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testMultipleSubjectsIndependentStates() {
        Subject math = new Subject("101", "Matemáticas", 4);
        Subject physics = new Subject("102", "Física", 3);
        Subject chemistry = new Subject("103", "Química", 4);

        try {
            SubjectDecorator mathDecorator = new SubjectDecorator(math);
            SubjectDecorator physicsDecorator = new SubjectDecorator(physics);
            SubjectDecorator chemistryDecorator = new SubjectDecorator(chemistry);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo = new Group(matematicas,30, academicPeriod1);
            mathDecorator.inscribir(grupo);
            mathDecorator.aprobar();

            physicsDecorator.inscribir(grupo);
            physicsDecorator.reprobar();
            
            
            assertTrue(mathDecorator.estaAprobada());
            assertTrue(physicsDecorator.estaReprobada());
            assertTrue(chemistryDecorator.canEnroll());
            
            assertEquals(SemaforoColores.VERDE, mathDecorator.getEstadoColor());
            assertEquals(SemaforoColores.ROJO, physicsDecorator.getEstadoColor());
            assertEquals(SemaforoColores.GRIS, chemistryDecorator.getEstadoColor());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateMethodDelegation() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            NoCursadaState initialState = (NoCursadaState) decorator.getState();
            assertNotNull(initialState);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            EnCursoState enrolledState = (EnCursoState) decorator.getState();
            assertNotNull(enrolledState);
            assertNotSame(initialState, enrolledState);
            
            decorator.aprobar();
            AprobadaState passedState = (AprobadaState) decorator.getState();
            assertNotNull(passedState);
            assertNotSame(enrolledState, passedState);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateInstanceUniqueness() {
        Subject subject1 = new Subject("101", "Matemáticas", 4);
        Subject subject2 = new Subject("102", "Física", 3);


        try { 
            SubjectDecorator decorator1 = new SubjectDecorator(subject1);
            SubjectDecorator decorator2 = new SubjectDecorator(subject2);
            
            assertNotSame(decorator1.getState(), decorator2.getState());
            
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator1.inscribir(grupo);
            decorator2.inscribir(grupo);

            assertNotSame(decorator1.getState(), decorator2.getState());
            
            assertEquals(decorator1.getState().getClass(), decorator2.getState().getClass());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testNoCursadaStateAllTransitions() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertTrue(decorator.getState() instanceof NoCursadaState);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            
            decorator.retirar();
            assertTrue(decorator.getState() instanceof NoCursadaState);
            
            assertThrows(SirhaException.class, decorator::aprobar);
            assertThrows(SirhaException.class, decorator::reprobar);
            assertThrows(SirhaException.class, decorator::retirar);
            
            assertTrue(decorator.getState() instanceof NoCursadaState);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testEnCursoStateAllTransitions() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            
            assertThrows(SirhaException.class, () -> decorator.inscribir(grupo));
            assertTrue(decorator.getState() instanceof EnCursoState);
            
            SubjectDecorator decorator2 = new SubjectDecorator(new Subject("102", "Física", 3));
            decorator2.inscribir(grupo);
            decorator2.aprobar();
            assertTrue(decorator2.getState() instanceof AprobadaState);
            
            SubjectDecorator decorator3 = new SubjectDecorator(new Subject("103", "Química", 4));
            decorator3.inscribir(grupo);
            decorator3.reprobar();
            assertTrue(decorator3.getState() instanceof ReprobadaState);
            
            decorator.retirar();
            assertTrue(decorator.getState() instanceof NoCursadaState);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testAprobadaStateAllTransitions() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.aprobar();
            
            assertTrue(decorator.getState() instanceof AprobadaState);
            
            assertThrows(SirhaException.class, () -> decorator.inscribir(grupo));
            assertThrows(SirhaException.class, decorator::reprobar);
            assertThrows(SirhaException.class, decorator::retirar);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testReprobadaStateAllTransitions() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.reprobar();
            assertTrue(decorator.getState() instanceof ReprobadaState);

            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            
            decorator.reprobar();
            assertTrue(decorator.getState() instanceof ReprobadaState);
            
            assertThrows(SirhaException.class, decorator::reprobar);
            assertThrows(SirhaException.class, decorator::retirar);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateTransitionConsistency() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertTrue(decorator.getState() instanceof NoCursadaState);
            assertTrue(decorator.canEnroll());
            assertFalse(decorator.estaCursando());
            assertFalse(decorator.estaAprobada());
            assertFalse(decorator.estaReprobada());

            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            assertFalse(decorator.canEnroll());
            assertTrue(decorator.estaCursando());
            assertFalse(decorator.estaAprobada());
            assertFalse(decorator.estaReprobada());
            
            decorator.reprobar();
            assertTrue(decorator.getState() instanceof ReprobadaState);
            assertTrue(decorator.canEnroll()); // Can re-enroll after failing
            assertFalse(decorator.estaCursando());
            assertFalse(decorator.estaAprobada());
            assertTrue(decorator.estaReprobada());

            decorator.inscribir(grupo);
            decorator.aprobar();
            assertTrue(decorator.getState() instanceof AprobadaState);
            assertFalse(decorator.canEnroll());
            assertFalse(decorator.estaCursando());
            assertTrue(decorator.estaAprobada());
            assertFalse(decorator.estaReprobada());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateColorTransitions() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertEquals(SemaforoColores.GRIS, decorator.getEstadoColor());

            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
            
            decorator.reprobar();
            assertEquals(SemaforoColores.ROJO, decorator.getEstadoColor());

            decorator.inscribir(grupo);
            assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
            
            decorator.aprobar();
            assertEquals(SemaforoColores.VERDE, decorator.getEstadoColor());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testInvalidTransitionsPreserveState() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            SemaforoColores originalColor = decorator.getEstadoColor();
            Class<?> originalStateClass = decorator.getState().getClass();
            
            assertThrows(SirhaException.class, decorator::aprobar);
            assertEquals(originalColor, decorator.getEstadoColor());
            assertEquals(originalStateClass, decorator.getState().getClass());
            
            assertThrows(SirhaException.class, decorator::reprobar);
            assertEquals(originalColor, decorator.getEstadoColor());
            assertEquals(originalStateClass, decorator.getState().getClass());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testReprobadaStateSpecificBehavior() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);

            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.reprobar();
            
            assertTrue(decorator.estaReprobada());
            assertTrue(decorator.canEnroll()); 
            assertFalse(decorator.estaCursando());
            assertFalse(decorator.estaAprobada());
            assertEquals(SemaforoColores.ROJO, decorator.getEstadoColor());

            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testEnCursoStateMultipleExitPaths() {

        try {
            Subject subject1 = new Subject("101", "Math", 4);
            SubjectDecorator decorator1 = new SubjectDecorator(subject1);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

            Group grupo = new Group(matematicas,30, academicPeriod);
            decorator1.inscribir(grupo);
            decorator1.aprobar();
            assertTrue(decorator1.getState() instanceof AprobadaState);

            Subject subject2 = new Subject("102", "Physics", 3);
            SubjectDecorator decorator2 = new SubjectDecorator(subject2);
            Group grupo21 = new Group(matematicas,30, academicPeriod1);
            decorator2.inscribir(grupo21);
            decorator2.reprobar();
            assertTrue(decorator2.getState() instanceof ReprobadaState);

            Subject subject3 = new Subject("103", "Chemistry", 4);
            SubjectDecorator decorator3 = new SubjectDecorator(subject3);
            Group grupo31 = new Group(matematicas,30, academicPeriod1);
            decorator3.inscribir(grupo31);
            decorator3.retirar();
            assertTrue(decorator3.getState() instanceof NoCursadaState);
        } catch (Exception e) { 
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateTransitionExceptionMessages() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            try {
                decorator.aprobar();
                fail("Should have thrown IllegalStateException");
            } catch (SirhaException e) {
                assertNotNull(e.getMessage());
            } catch (Exception e) {
                fail("No se esperaba una excepción al aprobar la materia: " + e.getMessage());
            }
            
            try {
                AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
                Group grupo = new Group(matematicas,30, academicPeriod1);
                decorator.inscribir(grupo);
                decorator.aprobar();
                try {
                    decorator.inscribir(grupo);
                    fail("Should have thrown IllegalStateException");
                } catch (SirhaException e) {
                    assertNotNull(e.getMessage());
                }
            } catch (Exception e) {
                fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testNoCursadaStateCanMethods() {
        try { 
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertTrue(decorator.getState() instanceof NoCursadaState);
            
            assertTrue(decorator.getState().canEnroll());
            assertFalse(decorator.getState().canApprove());
            assertFalse(decorator.getState().canFail());
            assertFalse(decorator.getState().canDropSubject());
            assertFalse(decorator.getState().hasAssignedGroup());
            assertEquals("No Cursada", decorator.getState().getStatusName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }
    
    @Test
    void testAprobadaSetSemestre() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4)); 
            academicPeriod1.equals(academicPeriod);
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.aprobar();
            assertTrue(decorator.getState() instanceof AprobadaState);

            assertThrows(SirhaException.class, () -> {
                decorator.setSemester(4);
            });
            Group grupo11 = new Group(matematicas,30, new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4)));
            assertThrows(SirhaException.class, () -> {decorator.setGroup(grupo11);});
            assertThrows(SirhaException.class, decorator::aprobar);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testReprobadaSetSemestre() {
        
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.reprobar();
            assertTrue(decorator.getState() instanceof ReprobadaState);

            assertThrows(SirhaException.class, () -> {
                decorator.setSemester(4);
            });
            Group grupo11 = new Group(matematicas, 30, new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4)));
            assertThrows(SirhaException.class, () -> {
                decorator.setGroup(grupo11);
            });
            assertThrows(SirhaException.class, () -> {
                decorator.setGrade(00);
            });
            assertEquals(SemaforoColores.ROJO, decorator.getLastStateProcess().getState());
            decorator.inscribir(grupo11);
            decorator.setGrade(50);
            decorator.setSemester(4);
            decorator.aprobar();
            assertTrue(decorator.getState() instanceof AprobadaState);
            assertEquals(4, decorator.getSemester());
            assertEquals(50, decorator.getGrade());
            assertEquals(SemaforoColores.VERDE, decorator.getLastStateProcess().getState());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }
    

    @Test
    void testNoCursadaStateSetGroupValid() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo = new Group(matematicas, 30, period);
            assertTrue(decorator.getState() instanceof NoCursadaState);
            assertNull(decorator.getGroup());

            assertThrows(SirhaException.class, () -> decorator.setGroup(grupo));
            assertNull(decorator.getGroup());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
        
    }

    @Test
    void testNoCursadaStateSetGroupNull() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertTrue(decorator.getState() instanceof NoCursadaState);

            SirhaException exception = assertThrows(SirhaException.class, () -> {
                decorator.getState().setGroup(decorator, null);
            });
            
            assertEquals("No se puede cambiar el grupo - No se puede asignar grupo a materia no cursada", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    
    @Test
    void testEnCursoStateCanMethods() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);

            assertTrue(decorator.getState() instanceof EnCursoState);
            
            assertFalse(decorator.getState().canEnroll());
            assertTrue(decorator.getState().canApprove());
            assertTrue(decorator.getState().canFail());
            assertTrue(decorator.getState().canDropSubject());
            assertTrue(decorator.getState().hasAssignedGroup());
            assertEquals("En Curso", decorator.getState().getStatusName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }
    
    @Test
    void testEnCursoStateSetSemestreThrowsException() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo = new Group(matematicas,30, academicPeriod1);

            SirhaException exception = assertThrows(SirhaException.class, () -> {
                decorator.setSemester(4);
            });
            decorator.inscribir(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            
            decorator.setSemester(4);
            
            assertEquals("No se puede cambiar el semestre - No se puede cambiar semestre de materia no cursada", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testEnCursoStateSetGroupThrowsException() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas, 30, period);
            SirhaException exception = assertThrows(SirhaException.class, () -> {
                decorator.setGroup(grupo);
            });
            decorator.inscribir(grupo);
            decorator.setGroup(grupo);
            assertTrue(decorator.getState() instanceof EnCursoState);
            assertEquals(grupo, decorator.getGroup());
            assertEquals("No se puede cambiar el grupo - No se puede asignar grupo a materia no cursada", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testAprobadaStateCanMethods() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.aprobar();
            assertTrue(decorator.getState() instanceof AprobadaState);
            
            assertFalse(decorator.getState().canEnroll());
            assertFalse(decorator.getState().canApprove());
            assertFalse(decorator.getState().canFail());
            assertFalse(decorator.getState().canDropSubject());
            assertTrue(decorator.getState().hasAssignedGroup());
            assertEquals("Aprobada", decorator.getState().getStatusName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testReprobadaStateCanMethods() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.reprobar();
            assertTrue(decorator.getState() instanceof ReprobadaState);
            
            assertTrue(decorator.getState().canEnroll()); // Puede reinscribirse
            assertTrue(decorator.getState().canApprove());
            assertFalse(decorator.getState().canFail());
            assertFalse(decorator.getState().canDropSubject());
            assertTrue(decorator.getState().hasAssignedGroup());
            assertEquals("Reprobada", decorator.getState().getStatusName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateCanMethodsConsistency() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas, 30, period);

            assertTrue(decorator.getState().canEnroll());
                    
            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo11 = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo11);
            decorator.setGroup(grupo);
            assertFalse(decorator.getState().canEnroll());
            assertTrue(decorator.getState().canApprove());
            assertTrue(decorator.getState().canFail());
            assertTrue(decorator.getState().canDropSubject());
            
            decorator.aprobar();
            assertFalse(decorator.getState().canEnroll());
            assertFalse(decorator.getState().canApprove());
            assertFalse(decorator.getState().canFail());
            assertFalse(decorator.getState().canDropSubject());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testAllStatesgetStatusName() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertEquals("No Cursada", decorator.getState().getStatusName());

            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            assertEquals("En Curso", decorator.getState().getStatusName());

            SubjectDecorator decorator2 = new SubjectDecorator(new Subject("102", "Física", 3));
            AcademicPeriod academicPeriod2 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo21 = new Group(fisica, 30, academicPeriod2);
            decorator2.inscribir(grupo21);
            decorator2.aprobar();
            assertEquals("Aprobada", decorator2.getState().getStatusName());

            SubjectDecorator decorator3 = new SubjectDecorator(new Subject("103", "Química", 4));
            AcademicPeriod academicPeriod3 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo31 = new Group(quimica, 30, academicPeriod3);
            decorator3.inscribir(grupo31);
            decorator3.reprobar();
            assertEquals("Reprobada", decorator3.getState().getStatusName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testhasAssignedGroupConsistency() {

        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group grupo = new Group(matematicas, 30, period);

            assertFalse(decorator.getState().hasAssignedGroup());
            
            
            assertFalse(decorator.getState().hasAssignedGroup());
            
            decorator.inscribir(grupo);
            decorator.setGroup(grupo);
            assertTrue(decorator.getState().hasAssignedGroup());
            
            decorator.aprobar();
            assertTrue(decorator.getState().hasAssignedGroup());
            
            SubjectDecorator decorator2 = new SubjectDecorator(new Subject("102", "Física", 3));
            Group grupo21 = new Group(matematicas, 30, period);
            
            assertFalse(decorator2.getState().hasAssignedGroup());
            decorator2.inscribir(grupo21);
            decorator2.setGroup(grupo21);
            decorator2.reprobar();
            assertTrue(decorator2.getState().hasAssignedGroup()); 
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testSubjectDecorator() {

        try {
        SubjectDecorator programacionDecorator = new SubjectDecorator(programacion);
        
        assertEquals("Programación I", programacionDecorator.getName());
        assertEquals(5, programacionDecorator.getCredits());
        assertNotNull(programacionDecorator.getGroups());

        AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo = new Group(matematicas,30, academicPeriod1);
            programacionDecorator.inscribir(grupo, 1);
            programacionDecorator.setSemester(2);
            
            assertEquals(SemaforoColores.AMARILLO, programacionDecorator.getEstadoColor());
            assertEquals(2, programacionDecorator.getSemester());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }
    @Test
    void testAcademicPeriodMethods() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);
        AcademicPeriod period = new AcademicPeriod("2024-1", startDate, endDate);
        period.setId("1");

        period.setStartDatesInscripciones(startDate.minusMonths(1), startDate.minusDays(10));

        assertEquals("2024-1", period.getPeriod());
        assertEquals(startDate, period.getStartDate());
        assertEquals(endDate, period.getEndDate());

        assertFalse(period.isActive());
        assertFalse(period.isPeriodInscripcionesAbiertas());
        assertEquals(startDate, period.getStartDate());
        assertEquals(endDate, period.getEndDate());
        assertEquals(startDate.minusMonths(1), period.getStartDateInscripciones());
        assertEquals(startDate.minusDays(10), period.getEndDateInscripciones());

        period.toString();

        assertNotEquals(null, period);
        assertNotEquals(period, new AcademicPeriod("2024-2", startDate, endDate));
        assertEquals(period, new AcademicPeriod("2024-1", startDate, endDate));
    }


    @Test
    void testCreateSolicitudCambioGroup(){
        try {
            student.enrollSubject(matematicas, grupo1);

            Group newGroup = new Group(matematicas, 25, academicPeriod);
            Schedule newSchedule = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
            newGroup.addSchedule(newSchedule);
            matematicas.addGroup(newGroup);
            assertNotEquals(newGroup, grupo1);

            CambioGrupo solicitud = student.createGroupChangeRequest(matematicas, newGroup);
            
            assertNotNull(solicitud);
            assertTrue(solicitud instanceof CambioGrupo);
            assertEquals(student, solicitud.getStudent());
            assertEquals(matematicas, solicitud.getSubject());
            assertEquals(newGroup, solicitud.getNewGroup());
            assertEquals(academicPeriod, solicitud.getCurrentPeriod());

            List<BaseRequest> solicitudes = student.getSolicitudes();
            assertEquals(1, solicitudes.size());
            assertEquals(solicitud, solicitudes.get(0));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear la solicitud de cambio de grupo: " + e.getMessage());
        }
    }

    @Test
    void testSubjectContainsDifferentGroupsWithSameProperties() {
        try {
            Group grupoA = new Group(matematicas, 25, academicPeriod);
            Group grupoB = new Group(matematicas, 25, academicPeriod);
            Group grupoC = new Group(matematicas, 25, academicPeriod);

            Schedule horario = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0));
            grupoA.addSchedule(horario);
            grupoB.addSchedule(new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0)));
            grupoC.addSchedule(new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0)));
            
            programacion.addGroup(grupoA);
            programacion.addGroup(grupoB);
            programacion.addGroup(grupoC);
            
            assertEquals(3, programacion.getGroups().size());
            assertNotSame(grupoA, grupoB);
            assertNotSame(grupoB, grupoC);
            assertNotSame(grupoA, grupoC);

            assertTrue(programacion.hasGroup(grupoA));
            assertTrue(programacion.hasGroup(grupoB));
            assertTrue(programacion.hasGroup(grupoC));

            Set<Group> uniqueGroups = new HashSet<>(programacion.getGroups());
            assertEquals(3, uniqueGroups.size());

            assertNotEquals(grupoA, grupoB);
            assertNotEquals(grupoB, grupoC);
            assertNotEquals(grupoA, grupoC);

            assertNotEquals(grupoA, grupoB);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos o asignarlos a la materia: " + e.getMessage());
        }
    }

    @Test
    void testCreateSolicitudCambioGroup2(){
        try {
            student.enrollSubject(matematicas, grupo1);

            Group newGroup = new Group(matematicas, 25, academicPeriod);
            Schedule newSchedule = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
            newGroup.addSchedule(newSchedule);
            matematicas.addGroup(newGroup);
            assertNotEquals(newGroup, grupo1);

            CambioGrupo solicitud = student.createGroupChangeRequest(matematicas, newGroup);
            
            assertNotNull(solicitud);
            assertTrue(solicitud instanceof CambioGrupo);
            assertEquals(student, solicitud.getStudent());
            assertEquals(matematicas, solicitud.getSubject());
            assertEquals(newGroup, solicitud.getNewGroup());
            assertEquals(academicPeriod, solicitud.getCurrentPeriod());

            List<BaseRequest> solicitudes = student.getSolicitudes();
            assertEquals(1, solicitudes.size());
            assertEquals(solicitud, solicitudes.get(0));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear la solicitud de cambio de grupo: " + e.getMessage());
        }
    }

    @Test
    void testSubjectContainsDifferentGroupsWithSameProperties2() {
        try {
            Group grupoA = new Group(matematicas, 25, academicPeriod);
            Group grupoB = new Group(matematicas, 25, academicPeriod);
            Group grupoC = new Group(matematicas, 25, academicPeriod);

            Schedule horario = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0));
            grupoA.addSchedule(horario);
            grupoB.addSchedule(new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0)));
            grupoC.addSchedule(new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0)));
            
            programacion.addGroup(grupoA);
            programacion.addGroup(grupoB);
            programacion.addGroup(grupoC);
            
            assertEquals(3, programacion.getGroups().size(), "La materia debe tener 3 grupos diferentes");
            assertNotSame(grupoA, grupoB);
            assertNotSame(grupoB, grupoC);
            assertNotSame(grupoA, grupoC);

            assertTrue(programacion.hasGroup(grupoA));
            assertTrue(programacion.hasGroup(grupoB));
            assertTrue(programacion.hasGroup(grupoC));

            Set<Group> uniqueGroups = new HashSet<>(programacion.getGroups());
            assertEquals(3, uniqueGroups.size(), "No debe haber grupos duplicados");
            
            assertNotEquals(grupoA, grupoB);
            assertNotEquals(grupoB, grupoC);
            assertNotEquals(grupoA, grupoC);

            assertNotEquals(grupoA, grupoB);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos o asignarlos a la materia: " + e.getMessage());
        }
    }

    @Test
    void testNoCursadaStateSpecificProperties() {
        try {
            Subject subject = new Subject("101", "Matemáticas", 4);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            NoCursadaState state = new NoCursadaState();
            
            assertTrue(decorator.getState() instanceof NoCursadaState);
            assertTrue(state.canEnroll());
            assertFalse(state.canApprove());
            assertFalse(state.canFail());
            assertFalse(state.canDropSubject());
            assertFalse(state.hasAssignedGroup());
            assertEquals("No Cursada", state.getStatusName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testEnCursoStateSpecificProperties() {
        EnCursoState state = new EnCursoState();
        
        assertFalse(state.canEnroll());
        assertTrue(state.canApprove());
        assertTrue(state.canFail());
        assertTrue(state.canDropSubject());
        assertTrue(state.hasAssignedGroup());
        assertEquals("En Curso", state.getStatusName());
    }

    @Test
    void testAprobadaStateSpecificProperties() {
        AprobadaState state = new AprobadaState();
        
        assertFalse(state.canEnroll());
        assertFalse(state.canApprove());
        assertFalse(state.canFail());
        assertFalse(state.canDropSubject());
        assertTrue(state.hasAssignedGroup());
        assertEquals("Aprobada", state.getStatusName());
    }

    @Test
    void testReprobadaStateSpecificProperties() {
        ReprobadaState state = new ReprobadaState();
        
        assertTrue(state.canEnroll());
        assertTrue(state.canApprove());
        assertFalse(state.canFail());
        assertFalse(state.canDropSubject());
        assertTrue(state.hasAssignedGroup());
        assertEquals("Reprobada", state.getStatusName());
    }

    @Test
    void testGroupMultipleStudentEnrollment() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group group = new Group(matematicas, 5, period);
            
            Student s1 = new Student("student1", "s1@test.com", "hash", "S001");
            Student s2 = new Student("student2", "s2@test.com", "hash", "S002");
            Student s3 = new Student("student3", "s3@test.com", "hash", "S003");
            
            group.enrollStudent(s1);
            group.enrollStudent(s2);
            group.enrollStudent(s3);
            
            assertEquals(3, group.getInscritos());
            assertEquals(2, group.getCuposDisponibles());
            assertFalse(group.isFull());
            
            assertTrue(group.contieneEstudiante(s1));
            assertTrue(group.contieneEstudiante(s2));
            assertTrue(group.contieneEstudiante(s3));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir estudiantes: " + e.getMessage());
        }
    }

    @Test
    void testGroupAutoCloseWhenFull() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group group = new Group(matematicas, 2, period);
            
            Student s1 = new Student("student1", "s1@test.com", "hash", "S001");
            Student s2 = new Student("student2", "s2@test.com", "hash", "S002");
            Student s3 = new Student("student3", "s3@test.com", "hash", "S003");
            
            assertTrue(group.getGroupState() instanceof StatusOpen);
            
            group.enrollStudent(s1);
            assertTrue(group.getGroupState() instanceof StatusOpen);
            
            group.enrollStudent(s2);
            assertTrue(group.getGroupState() instanceof StatusClosed);
            assertTrue(group.isFull());
            
            assertThrows(SirhaException.class, () -> group.enrollStudent(s3));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir estudiantes: " + e.getMessage());
        }
    }

    @Test
    void testGroupScheduleConflictDetection() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try{ 
            Group group1 = new Group(matematicas, 10, period);
            Group group2 = new Group(matematicas, 10, period);
            
            Schedule schedule11 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule schedule21 = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0)); // Conflicto
            
            group1.addSchedule(schedule11);
            group2.addSchedule(schedule21);
            
            assertTrue(group1.conflictoConHorario(group2));
            assertTrue(group2.conflictoConHorario(group1));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o asignar horarios: " + e.getMessage());
        }
    }

    @Test
    void testGroupNoScheduleConflict() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group group1 = new Group(matematicas, 10, period);
            Group group2 = new Group(matematicas, 10, period);
            
            Schedule schedule11 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule schedule21 = new Schedule(DiasSemana.MARTES, LocalTime.of(8, 0), LocalTime.of(10, 0)); // Diferente día
            
            group1.addSchedule(schedule11);
            group2.addSchedule(schedule21);
            
            assertFalse(group1.conflictoConHorario(group2));
            assertFalse(group2.conflictoConHorario(group1));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o asignar horarios: " + e.getMessage());
        }
    }

    @Test
    void testStudentAcademicValidations() {
        try {
            Student student1 = new Student("test.user", "test@example.com", "password", "T001");
            StudyPlan plan = new StudyPlan("Test Plan", Careers.INGENIERIA_DE_SISTEMAS);
            Subject subject = new Subject("101", "Test Subject", 3);
            plan.addSubject(subject);
            
            Semaforo semaforo1 = new Semaforo(plan);
            student1.setAcademicProgress(semaforo1);
            
            assertTrue(student1.getAcademicProgress().isSubjectNoCursada(subject));
            assertFalse(student1.getAcademicProgress().isSubjectCursando(subject));
            assertFalse(student1.getAcademicProgress().isSubjectApproved(subject));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el estudiante o plan de estudios: " + e.getMessage());
        }
    }

    @Test
    void testStudentSubjectEnrollment() {
        
        try {
            Student student1 = new Student("test.user", "test@example.com", "password", "T001");
            StudyPlan plan = new StudyPlan("Test Plan", Careers.INGENIERIA_DE_SISTEMAS);
            Subject subject = new Subject("101", "Test Subject", 3);
            plan.addSubject(subject);
            
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group group = new Group(matematicas, 30, period);
            subject.addGroup(group);
            
            Semaforo semaforo1 = new Semaforo(plan);
            student1.setAcademicProgress(semaforo1);
            student1.setCurrentPeriod(period);
            
            student1.enrollSubject(subject, group);
            
            assertTrue(student1.hasSubject(subject));
            assertTrue(student1.getAcademicProgress().isSubjectCursando(subject));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testScheduleValidations() {
        try {
            Schedule schedule = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            
            assertEquals(DiasSemana.LUNES, schedule.getDay());
            assertEquals(LocalTime.of(8, 0), schedule.getStartHour());
            assertEquals(LocalTime.of(10, 0), schedule.getEndHour());

            assertTrue(schedule.overlapsWith(
                new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0))
            ));

            assertFalse(schedule.overlapsWith(
                new Schedule(DiasSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(11, 0))
            ));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el horario: " + e.getMessage());
        }   
    }

    @Test
    void testSubjectBasicFunctionalities() {
        Subject subject = new Subject("CS101", "Programming I", 4);
        
        assertEquals("CS101", subject.getId());
        assertEquals("Programming I", subject.getName());
        assertEquals(4, subject.getCredits());
        assertNotNull(subject.getGroups());
        assertTrue(subject.getGroups().isEmpty());
        
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group group = new Group(matematicas, 30, period);
        
            subject.addGroup(group);
            assertEquals(1, subject.getGroups().size());
            assertTrue(subject.hasGroup(group));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }


    @Test
    void testAcademicPeriodDateValidations() {
        LocalDate start = LocalDate.of(2024, 1, 15);
        LocalDate end = LocalDate.of(2024, 6, 15);
        
        AcademicPeriod period = new AcademicPeriod("2024-1", start, end);
        
        assertEquals("2024-1", period.getPeriod());
        assertEquals(start, period.getStartDate());
        assertEquals(end, period.getEndDate());
        
        // Test with current date within period
        assertFalse(period.isActive()); // Depends on current date
        
        // Test inscription dates
        LocalDate inscStart = start.minusDays(30);
        LocalDate inscEnd = start.minusDays(1);
        period.setStartDatesInscripciones(inscStart, inscEnd);
        
        assertEquals(inscStart, period.getStartDateInscripciones());
        assertEquals(inscEnd, period.getEndDateInscripciones());
    }

    @Test
    void testProfessorBasicFunctionalities() {
        Professor prof1 = new Professor();
        assertNotNull(prof1);
        try {
            Professor prof2 = new Professor("dr.jones", "jones@univ.edu", "password");
            prof2.setId("PROF001");
            
            assertEquals("dr.jones", prof2.getUsername());
            assertEquals("jones@univ.edu", prof2.getEmail());
            assertEquals("PROF001", prof2.getId());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el profesor: " + e.getMessage());
        }
    }

    @Test
    void testStudyPlanSubjectManagement() {
        StudyPlan plan = new StudyPlan("Computer Science", Careers.INGENIERIA_DE_SISTEMAS);
        
        Subject cs101 = new Subject("CS101", "Programming I", 4);
        Subject cs102 = new Subject("CS102", "Programming II", 4);
        Subject math101 = new Subject("MATH101", "Calculus I", 4);
        
        plan.addSubject(cs101);
        plan.addSubject(cs102);
        plan.addSubject(math101);
        
        assertEquals(3, plan.getSubjects().size());
        assertTrue(plan.hasSubject(cs101));
        assertTrue(plan.hasSubject(cs102));
        assertTrue(plan.hasSubject(math101));
    
    }

    @Test
    void testMapperUtilsEdgeCases() {
        try {
            Student minimalStudent = new Student("user", "user@test.com", "pass", "U001");
            StudentDTO dto = StudentMapper.toDTO(minimalStudent);
            
            assertNotNull(dto);
            assertEquals("user", dto.getUsername());
            assertEquals("U001", dto.getCode());
            
            Student converted = StudentMapper.fromDTOnewStudent(dto);
            assertNotNull(converted);
            assertEquals("user", converted.getUsername());
            assertEquals("U001", converted.getCodigo());
        } catch (Exception e) {
            fail("No se esperaba una excepción al mapear el estudiante: " + e.getMessage());
        }
    }

    @Test
    void testGroupSpecialStateHandling() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try{
            Group group = new Group(matematicas, 1, period);
        
            Student studentTest = new Student("test", "test@test.com", "pass", "T001");
            
            assertTrue(group.getGroupState() instanceof StatusOpen);
            
            group.enrollStudent(studentTest);
            assertTrue(group.getGroupState() instanceof StatusClosed);
            assertTrue(group.isFull());

            group.unenrollStudent(studentTest);
            assertTrue(group.getGroupState() instanceof StatusOpen);
            assertFalse(group.isFull());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir/desinscribir estudiantes: " + e.getMessage());
        }
    }

    @Test
    void testSubjectDecoratorAdvancedConfiguration() {

        try {
            Subject subject = new Subject("ADV101", "Advanced Topics", 5);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertEquals(0, decorator.getSemester());
            assertEquals(0, decorator.getGrade());
            assertNull(decorator.getGroup());
            
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group group = new Group(matematicas, 20, period);
        
            decorator.inscribir(group, 3); // Inscribir en semestre 3
            decorator.setGrade(85);
            
            assertEquals(3, decorator.getSemester());
            assertEquals(85, decorator.getGrade());
            assertEquals(group, decorator.getGroup());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStateInvalidTransitionValidations() {

        try{
            Subject subject = new Subject("101", "Test", 3);
            SubjectDecorator decorator = new SubjectDecorator(subject);
            
            assertTrue(decorator.getState() instanceof NoCursadaState);
            
            assertThrows(SirhaException.class, decorator::aprobar);
            assertThrows(SirhaException.class, decorator::reprobar);
            assertThrows(SirhaException.class, decorator::retirar);

            AcademicPeriod academicPeriod1 = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group grupo = new Group(matematicas,30, academicPeriod1);
            decorator.inscribir(grupo);
            decorator.aprobar();
            
            assertThrows(SirhaException.class, () -> decorator.inscribir(grupo));
            assertThrows(SirhaException.class, decorator::aprobar);
            assertThrows(SirhaException.class, decorator::reprobar);
            assertThrows(SirhaException.class, decorator::retirar);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testStudentGets(){
        try {
            Student student1 = new Student("test.user", "test.user@example.com", "password", "T001");
            student1.getAcademicSummary();
            student1.getAcademicPensum();
            assertEquals(0, student1.getSubjectsInProgressCount());
            assertEquals(0, student1.getPassedSubjectsCount());
            assertEquals(0, student1.getFailedSubjectsCount());
            assertEquals(0, student1.getSubjectsNotTakenCount());
            assertFalse(student1.hasCoursesInProgress());
            assertEquals(0, student1.getSubjectsBySemester(0).size());
            assertFalse(student1.hasScheduleConflictWith(grupo1));
            assertEquals(0, student1.getCurrentSchedule().size());
            assertEquals(0, student1.getSolicitudes().size());
            assertEquals(0, student1.getCreditsInProgress());
            assertEquals(1, student1.getCurrentSemester());
            assertNull(student1.getCurrentPeriod());
            assertEquals(0, student1.getAllSchedules().size());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el estudiante: " + e.getMessage());
        }
    }

    @Test
    void testCompleteStudentIntegrationFlow() {
        // Setup completo
        
        try {
            Student student1 = new Student("integration.test", "int@test.com", "password", "INT001");
            StudyPlan plan = new StudyPlan("Integration Test Plan", Careers.INGENIERIA_DE_SISTEMAS);
            
            Subject math = new Subject("MATH101", "Mathematics", 4);
            Subject physics = new Subject("PHYS101", "Physics", 3);
            plan.addSubject(math);
            plan.addSubject(physics);
            
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            
            Group mathGroup = new Group(matematicas, 30, period);
            Group physicsGroup = new Group(physics, 25, period);
            
            math.addGroup(mathGroup);
            physics.addGroup(physicsGroup);
            
            Semaforo semaforo1 = new Semaforo(plan);
            student1.setAcademicProgress(semaforo1);
            student1.setCurrentPeriod(period);
            
            student1.enrollSubject(math, mathGroup);
            assertTrue(student1.hasSubject(math));
            assertTrue(student1.getAcademicProgress().isSubjectCursando(math));

            student1.enrollSubject(physics, physicsGroup);
            assertTrue(student1.hasSubject(physics));

        } catch (Exception e) {
            fail("No se esperaba una excepción en el flujo de integración: " + e.getMessage());
        }
    }
}