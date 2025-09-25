package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentDTO;
import edu.dosw.sirha.SIRHA_BackEnd.util.MapperUtils;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;
import java.util.*;
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
    
    @Test
    void testStudentAcademicProgress() {
        // Crear estudiante completo
        Student student = new Student("STU002", "maria.lopez", "maria@example.com", "hash", "20231002");
        
        // Plan de estudios
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        Subject calculo = new Subject("CAL001", "Cálculo I", 4);
        studyPlan.addMateria(calculo);
        
        // Materia decorada
        SubjectDecorator calculoDecorator = new SubjectDecorator(calculo);
        calculoDecorator.setEstadoColor(SemaforoColores.GRIS); // No cursada inicialmente
        
        // Semáforo
        Semaforo semaforo = new Semaforo(studyPlan);
        semaforo.addSubject(calculoDecorator);
        
        // Asignar al estudiante
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(semaforo);
        
        // Simular progreso: estudiante se inscribe
        calculoDecorator.setEstadoColor(SemaforoColores.AMARILLO);
        calculoDecorator.setSemestreMateria(1);
        
        // Verificar estado
        assertEquals(SemaforoColores.AMARILLO, calculoDecorator.getEstadoColor());
        assertEquals(1, calculoDecorator.getSemestre());
        
        // Simular aprobación
        calculoDecorator.setEstadoColor(SemaforoColores.VERDE);
        
        // Verificar estado final
        assertEquals(SemaforoColores.VERDE, calculoDecorator.getEstadoColor());
    }
    
    @Test
    void testSubjectDecoratorStateTransitions() {
        // Crear materia y decorador
        Subject bases = new Subject("BD001", "Bases de Datos", 4);
        SubjectDecorator basesDecorator = new SubjectDecorator(bases);
        
        // Estado inicial: no cursada
        basesDecorator.setEstadoColor(SemaforoColores.GRIS);
        assertEquals(SemaforoColores.GRIS, basesDecorator.getEstadoColor());
        
        // Se inscribe: cursando
        basesDecorator.setEstadoColor(SemaforoColores.AMARILLO);
        basesDecorator.setSemestreMateria(3);
        assertEquals(SemaforoColores.AMARILLO, basesDecorator.getEstadoColor());
        assertEquals(3, basesDecorator.getSemestre());
        
        // Aprueba la materia
        basesDecorator.setEstadoColor(SemaforoColores.VERDE);
        assertEquals(SemaforoColores.VERDE, basesDecorator.getEstadoColor());
    }
    
    @Test
    void testCompleteStudentSemaforoIntegration() {
        // Crear estudiante
        Student student = new Student("STU003", "carlos.ruiz", "carlos@example.com", "hash", "20231003");
        
        // Plan con múltiples materias
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        Subject[] subjects = {
            new Subject("MAT001", "Matemáticas I", 4),
            new Subject("PRG001", "Programación I", 5),
            new Subject("FIS001", "Física I", 3)
        };
        
        for (Subject subject : subjects) {
            studyPlan.addMateria(subject);
        }
        
        // Crear decoradores y semáforo
        Map<String, SubjectDecorator> decorators = new HashMap<>();
        for (Subject subject : subjects) {
            SubjectDecorator decorator = new SubjectDecorator(subject);
            decorator.setEstadoColor(SemaforoColores.GRIS);
            decorators.put(subject.getName(), decorator);
        }
        
        Semaforo semaforo = new Semaforo(studyPlan);
        semaforo.setSubjects(decorators);
        
        // Asignar al estudiante
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(semaforo);
        
        // Verificar integración completa
        assertEquals(3, student.getPlanGeneral().getMaterias().size());
        assertEquals(3, student.getSemaforo().getSubjectsCount());
        
        // Verificar que cada decorador corresponde a una materia del plan
        for (Subject subject : subjects) {
            SubjectDecorator decorator = decorators.get(subject.getName());
            assertNotNull(decorator);
            assertEquals(subject.getName(), decorator.getName());
            assertEquals(subject.getCreditos(), decorator.getCreditos());
        }
    }

    // ============== PRUEBAS PARA CAPA DE PERSISTENCIA ==============
    
    @Test
    void testStudentInscribirEnMateria() {
        // Crear estudiante y plan de estudios
        Student student = new Student("STU004", "ana.garcia", "ana@example.com", "hash", "20231004");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        // Agregar materia al plan
        Subject algoritmos = new Subject("ALG001", "Algoritmos", 4);
        studyPlan.addMateria(algoritmos);
        student.setPlanGeneral(studyPlan);
        
        // Crear semáforo (esto inicializa todas las materias del plan en GRIS)
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Verificar estado inicial
        assertNotNull(student.getSemaforo());
        assertEquals(1, student.getSemaforo().getSubjectsCount());
        assertEquals(0, student.getMateriasCursandoCount()); // Inicialmente no hay materias cursando
        
        // Inscribir en materia (actualizar estado a AMARILLO)
        student.actualizarEstadoMateria("Algoritmos", SemaforoColores.AMARILLO);
        student.getSemaforo().setSemestreMateria("Algoritmos", 1);
        
        // Verificar inscripción
        assertEquals(1, student.getMateriasCursandoCount());
        assertEquals(SemaforoColores.AMARILLO, student.getMateriasCursando().get(0).getEstadoColor());
    }
    
    @Test
    void testStudentActualizarEstadoMateria() {
        // Configurar estudiante con plan y semáforo
        Student student = new Student("STU005", "pedro.lopez", "pedro@example.com", "hash", "20231005");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        Subject estructuras = new Subject("EST001", "Estructuras de Datos", 5);
        studyPlan.addMateria(estructuras);
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Inscribir en materia (cambiar a AMARILLO)
        student.actualizarEstadoMateria("Estructuras de Datos", SemaforoColores.AMARILLO);
        student.getSemaforo().setSemestreMateria("Estructuras de Datos", 3);
        
        // Verificar estado inicial
        assertEquals(1, student.getMateriasCursandoCount());
        assertEquals(0, student.getMateriasAprobadasCount());
        
        // Aprobar la materia
        student.actualizarEstadoMateria("Estructuras de Datos", SemaforoColores.VERDE);
        
        // Verificar cambio de estado
        assertEquals(0, student.getMateriasCursandoCount());
        assertEquals(1, student.getMateriasAprobadasCount());
    }
    
    @Test
    void testStudentReprobarMateria() {
        // Configurar estudiante
        Student student = new Student("STU006", "lucia.martinez", "lucia@example.com", "hash", "20231006");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        Subject baseDatos = new Subject("BD001", "Base de Datos", 4);
        studyPlan.addMateria(baseDatos);
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Inscribir y luego reprobar la materia
        student.actualizarEstadoMateria("Base de Datos", SemaforoColores.AMARILLO);
        student.getSemaforo().setSemestreMateria("Base de Datos", 4);
        student.actualizarEstadoMateria("Base de Datos", SemaforoColores.ROJO);
        
        // Verificar estado final
        assertEquals(0, student.getMateriasCursandoCount());
        assertEquals(0, student.getMateriasAprobadasCount());
        assertEquals(1, student.getMateriasReprobadasCount());
    }
    
    @Test
    void testStudentGetMateriasPorSemestre() {
        // Configurar estudiante con plan de estudios
        Student student = new Student("STU007", "diego.ramirez", "diego@example.com", "hash", "20231007");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        // Agregar materias al plan
        Subject calculo1 = new Subject("CAL001", "Cálculo I", 4);
        Subject prog1 = new Subject("PRG001", "Programación I", 5);
        Subject calculo2 = new Subject("CAL002", "Cálculo II", 4);
        Subject prog2 = new Subject("PRG002", "Programación II", 5);
        
        studyPlan.addMateria(calculo1);
        studyPlan.addMateria(prog1);
        studyPlan.addMateria(calculo2);
        studyPlan.addMateria(prog2);
        
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Configurar semestres
        student.getSemaforo().setSemestreMateria("Cálculo I", 1);
        student.getSemaforo().setSemestreMateria("Programación I", 1);
        student.getSemaforo().setSemestreMateria("Cálculo II", 2);
        student.getSemaforo().setSemestreMateria("Programación II", 2);
        
        // Verificar agrupación por semestre
        List<SubjectDecorator> semestre1 = student.getMateriasPorSemestre(1);
        List<SubjectDecorator> semestre2 = student.getMateriasPorSemestre(2);
        
        assertEquals(2, semestre1.size());
        assertEquals(2, semestre2.size());
        
        // Verificar que las materias están en el semestre correcto
        assertTrue(semestre1.stream().anyMatch(m -> m.getName().equals("Cálculo I")));
        assertTrue(semestre1.stream().anyMatch(m -> m.getName().equals("Programación I")));
        assertTrue(semestre2.stream().anyMatch(m -> m.getName().equals("Cálculo II")));
        assertTrue(semestre2.stream().anyMatch(m -> m.getName().equals("Programación II")));
    }
    
    @Test
    void testStudentCalculoCreditosPorColor() {
        // Configurar estudiante con plan de estudios
        Student student = new Student("STU008", "sofia.hernandez", "sofia@example.com", "hash", "20231008");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        // Crear materias con diferentes créditos
        Subject materia1 = new Subject("MAT001", "Materia 1", 4); // Para aprobar
        Subject materia2 = new Subject("MAT002", "Materia 2", 3); // Para reprobar
        Subject materia3 = new Subject("MAT003", "Materia 3", 5); // Para cursar
        Subject materia4 = new Subject("MAT004", "Materia 4", 2); // Para cursar
        
        studyPlan.addMateria(materia1);
        studyPlan.addMateria(materia2);
        studyPlan.addMateria(materia3);
        studyPlan.addMateria(materia4);
        
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Cambiar estados (por defecto todas inician en GRIS)
        student.actualizarEstadoMateria("Materia 1", SemaforoColores.VERDE); // 4 créditos aprobados
        student.actualizarEstadoMateria("Materia 2", SemaforoColores.ROJO);  // 3 créditos reprobados
        student.actualizarEstadoMateria("Materia 3", SemaforoColores.AMARILLO); // 5 créditos cursando
        student.actualizarEstadoMateria("Materia 4", SemaforoColores.AMARILLO); // 2 créditos cursando
        
        // Verificar cálculos
        assertEquals(4, student.getCreditosPorColor(SemaforoColores.VERDE));
        assertEquals(3, student.getCreditosPorColor(SemaforoColores.ROJO));
        assertEquals(7, student.getCreditosPorColor(SemaforoColores.AMARILLO));
    }
    
    @Test
    void testStudentConflictosHorario() {
        // Configurar estudiante
        Student student = new Student("STU009", "carlos.perez", "carlos@example.com", "hash", "20231009");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        // Crear materias 
        Subject materia1 = new Subject("MAT001", "Materia 1", 4);
        Subject materia2 = new Subject("MAT002", "Materia 2", 3);
        
        // Agregar materias al plan de estudios
        studyPlan.addMateria(materia1);
        studyPlan.addMateria(materia2);
        
        // Configurar estudiante con plan y semáforo
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Crear grupos con horarios que se solapan (ambos Lunes 8-10)
        Group grupo1 = new Group(30);
        grupo1.addHorario(new Schedule("Lunes", 8, 10)); // Lunes 8:00-10:00
        
        Group grupo2 = new Group(25);
        grupo2.addHorario(new Schedule("Lunes", 9, 11)); // Lunes 9:00-11:00 (se solapa con grupo1)
        
        // Crear decoradores con grupos
        SubjectDecorator decorator1 = new SubjectDecorator(materia1);
        decorator1.setGroup(grupo1);
        
        SubjectDecorator decorator2 = new SubjectDecorator(materia2);
        decorator2.setGroup(grupo2);
        
        // Inscribir en ambas materias
        student.inscribirMateria(decorator1, 1);
        student.inscribirMateria(decorator2, 1);
        
        // Verificar inscripciones exitosas
        assertEquals(2, student.getMateriasCursando().size());
        
        // Verificar que hay conflicto entre los horarios
        assertTrue(decorator1.tieneConflictoConHorario(decorator2));
        assertTrue(decorator2.tieneConflictoConHorario(decorator1));
    }
    
    @Test 
    void testStudentSinConflictosHorario() {
        // Configurar estudiante
        Student student = new Student("STU010", "maria.rodriguez", "maria@example.com", "hash", "20231010");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        // Crear materias
        Subject materia1 = new Subject("MAT001", "Materia 1", 4);
        Subject materia2 = new Subject("MAT002", "Materia 2", 3);
        
        // Agregar materias al plan de estudios
        studyPlan.addMateria(materia1);
        studyPlan.addMateria(materia2);
        
        // Configurar estudiante con plan y semáforo
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Crear grupos con horarios que NO se solapan
        Group grupo1 = new Group(30);
        grupo1.addHorario(new Schedule("Lunes", 8, 10)); // Lunes 8:00-10:00
        
        Group grupo2 = new Group(25);
        grupo2.addHorario(new Schedule("Martes", 8, 10)); // Martes 8:00-10:00 (diferente día)
        
        // Crear decoradores con grupos
        SubjectDecorator decorator1 = new SubjectDecorator(materia1);
        decorator1.setGroup(grupo1);
        
        SubjectDecorator decorator2 = new SubjectDecorator(materia2);
        decorator2.setGroup(grupo2);
        
        // Inscribir en ambas materias
        student.inscribirMateria(decorator1, 1);
        student.inscribirMateria(decorator2, 1);
        
        // Verificar que las materias están inscritas
        assertEquals(2, student.getMateriasCursando().size());
        assertTrue(student.getSemaforo().estaInscrito("Materia 1"));
        assertTrue(student.getSemaforo().estaInscrito("Materia 2"));
        
        // Verificar que NO hay conflicto entre los horarios
        assertFalse(decorator1.tieneConflictoConHorario(decorator2));
        assertFalse(decorator2.tieneConflictoConHorario(decorator1));
    }
    
    @Test
    void testStudentResumenAcademico() {
        // Configurar estudiante con un historial académico variado
        Student student = new Student("STU011", "alejandro.torres", "alejandro@example.com", "hash", "20231011");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        // Crear materias
        Subject[] materias = {
            new Subject("MAT001", "Matemáticas I", 4),
            new Subject("PRG001", "Programación I", 5),
            new Subject("FIS001", "Física I", 3),
            new Subject("QUI001", "Química", 3),
            new Subject("EST001", "Estructuras", 4)
        };
        
        // Agregar materias al plan de estudios
        for (Subject materia : materias) {
            studyPlan.addMateria(materia);
        }
        
        // Configurar estudiante con plan y semáforo
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Inscribir todas las materias usando los nombres correctos
        for (int i = 0; i < materias.length; i++) {
            SubjectDecorator decorator = new SubjectDecorator(materias[i]);
            student.inscribirMateria(decorator, (i % 2) + 1); // Semestres 1 y 2
        }
        
        // Configurar diferentes estados usando los nombres correctos de las materias
        student.actualizarEstadoMateria("Matemáticas I", SemaforoColores.VERDE); // Aprobada
        student.actualizarEstadoMateria("Programación I", SemaforoColores.VERDE); // Aprobada  
        student.actualizarEstadoMateria("Física I", SemaforoColores.ROJO);  // Reprobada
        student.actualizarEstadoMateria("Química", SemaforoColores.AMARILLO);  // Cursando
        student.actualizarEstadoMateria("Estructuras", SemaforoColores.AMARILLO);  // Cursando
        
        // Obtener resumen
        String resumen = student.getResumenAcademico();
        
        // Verificar que el resumen contiene información correcta
        assertNotNull(resumen);
        assertTrue(resumen.contains("20231011")); // Código del estudiante
        assertTrue(resumen.contains("Aprobadas: 2")); // 2 materias aprobadas
        assertTrue(resumen.contains("Cursando: 2"));  // 2 materias cursando
        assertTrue(resumen.contains("Reprobadas: 1")); // 1 materia reprobada
        assertTrue(resumen.contains("(9 créditos)")); // Créditos aprobados (4+5)
        assertTrue(resumen.contains("(7 créditos)")); // Créditos cursando (3+4)
    }
    
    @Test
    void testStudentNoPermiteInscripcionDuplicada() {
        // Configurar estudiante
        Student student = new Student("STU012", "valentina.castro", "valentina@example.com", "hash", "20231012");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        Subject calculo = new Subject("CAL001", "Cálculo I", 4);
        
        // Agregar materia al plan de estudios
        studyPlan.addMateria(calculo);
        
        // Configurar estudiante con plan y semáforo
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        SubjectDecorator decorator = new SubjectDecorator(calculo);
        
        // Primera inscripción
        student.inscribirMateria(decorator, 1);
        assertEquals(1, student.getMateriasCursando().size());
        assertTrue(student.getSemaforo().estaInscrito("Cálculo I"));
        
        // Segunda inscripción de la misma materia
        student.inscribirMateria(decorator, 1);
        assertEquals(1, student.getMateriasCursando().size()); // Tamaño no cambia (no permite doble inscripción)
    }

    @Test
    void testStudentDeteccionConflictosHorarios() {
        // Configurar estudiante con semáforo
        Student student = new Student("STU013", "alejandro.gomez", "alejandro@example.com", "hash", "20231013");
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        // Crear materias ya inscritas con horarios
        Subject materia1 = new Subject("CAL001", "Cálculo I", 4);
        Subject materia3 = new Subject("ALG001", "Álgebra", 3);
        
        // Agregar materias al plan de estudios
        studyPlan.addMateria(materia1);
        studyPlan.addMateria(materia3);
        
        // Configurar estudiante con plan y semáforo
        student.setPlanGeneral(studyPlan);
        student.setSemaforo(new Semaforo(studyPlan));
        
        // Crear grupos con horarios específicos
        Group grupo1 = new Group(30);
        grupo1.addHorario(new Schedule("Lunes", 8, 10)); // Lunes 8-10
        
        Group grupo3 = new Group(25);
        grupo3.addHorario(new Schedule("Martes", 10, 12)); // Martes 10-12
        
        SubjectDecorator decorator1 = new SubjectDecorator(materia1);
        decorator1.setGroup(grupo1);
        
        SubjectDecorator decorator3 = new SubjectDecorator(materia3);
        decorator3.setGroup(grupo3);
        
        // Inscribir las materias existentes (quedan en estado AMARILLO = cursando)
        student.inscribirMateria(decorator1, 1);
        student.inscribirMateria(decorator3, 1);
        
        // Verificar que están cursando
        assertEquals(2, student.getMateriasCursando().size());
        
        // Crear nueva materia para probar conflictos
        Subject materiaConflictiva = new Subject("FIS001", "Física I", 4);      
        Group grupoConflictivo = new Group(20);
        grupoConflictivo.addHorario(new Schedule("Lunes", 9, 11)); // Se solapa con Cálculo I (Lunes 8-10)
        
        SubjectDecorator decoratorConflictivo = new SubjectDecorator(materiaConflictiva);
        decoratorConflictivo.setGroup(grupoConflictivo);
        
        // Probar detección de conflictos usando el método de Student
        // Debe detectar conflicto con Cálculo I que está cursando
        assertTrue(student.tieneConflictosConHorario(decoratorConflictivo));
        
        // Crear materia sin conflictos
        Subject materiaSinConflicto = new Subject("MAT001", "Matemáticas", 3);      
        Group grupoSinConflicto = new Group(25);
        grupoSinConflicto.addHorario(new Schedule("Miércoles", 14, 16)); // No se solapa con ninguna materia cursando
        
        SubjectDecorator decoratorSinConflicto = new SubjectDecorator(materiaSinConflicto);
        decoratorSinConflicto.setGroup(grupoSinConflicto);
        
        // Verificar que no hay conflictos
        assertFalse(student.tieneConflictosConHorario(decoratorSinConflicto));
    }
}