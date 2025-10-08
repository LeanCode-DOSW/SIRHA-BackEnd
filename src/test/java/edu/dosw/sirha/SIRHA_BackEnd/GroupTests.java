package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.DiasSemana;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusClosed;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusOpen;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GroupTests {

    private AcademicPeriod academicPeriod;
    private Group group;
    private Student student1;
    private Student student2;
    private Student student3;
    private Professor professor;
    private Subject subject;
    private Schedule schedule1;
    private Schedule schedule2;
    private Schedule scheduleConflict;

    @BeforeEach
    void setUp() {
        // Período académico común
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        
        // Estudiantes de prueba
        student1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        student2 = new Student("maria", "maria@test.com", "hash456", "20231002");
        student3 = new Student("carlos", "carlos@test.com", "hash789", "20231003");
        
        // Profesor de prueba
        professor = new Professor("Dr. Smith", "smith@university.edu", "hash", "Matemáticas");
        
        // Materia de prueba
        subject = new Subject("101", "Cálculo I", 4);
        
        // Grupo principal para las pruebas
        group = new Group(subject, 5, academicPeriod);
        
        // Horarios de prueba
        schedule1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        schedule2 = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
        scheduleConflict = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
    }

    // ============ PRUEBAS DE CONSTRUCCIÓN Y CONFIGURACIÓN BÁSICA ============

    @Test
    @DisplayName("Crear grupo con capacidad válida")
    void groupTest() {
        assertEquals(5, group.getCapacidad());
        assertTrue(group.getGroupState() instanceof StatusOpen);
        assertEquals(0, group.getInscritos());
        assertNotNull(group.getEstudiantes());
        assertTrue(group.getEstudiantes().isEmpty());
    }

    @Test
    @DisplayName("Crear grupo con capacidad inválida debe fallar")
    void capacidadInvalidaTest() {
        assertThrows(IllegalArgumentException.class, () -> new Group(subject, 0, academicPeriod));
        assertThrows(IllegalArgumentException.class, () -> new Group(subject, -5, academicPeriod));
    }

    @Test
    @DisplayName("Configurar ID del grupo")
    void verificateIdTest() {
        group.setId(11111);
        assertEquals(11111, group.getId());
    }

    @Test
    @DisplayName("Configurar aula del grupo")
    void verificarAulaTest() {
        group.setAula("Bloque A");
        assertEquals("Bloque A", group.getAula());
    }

    @Test
    @DisplayName("Asignar profesor al grupo")
    void setProfesorTest() {
        group.setProfesor(professor);
        assertEquals(professor, group.getProfesor());
    }

    @Test
    @DisplayName("Asignar materia al grupo")
    void setCursoTest() {
        group.setCurso(subject);
        assertEquals(subject, group.getCurso());
    }

    // ============ PRUEBAS DE ESTADO DEL GRUPO ============

    @Test
    @DisplayName("Estado inicial debe ser abierto")
    void estadoInicialEsAbiertoTest() {
        assertTrue(group.getGroupState() instanceof StatusOpen);
    }

    @Test
    @DisplayName("Cambiar estado del grupo")
    void setEstadoTest() {
        GroupState estadoCerrado = new StatusClosed();
        group.setEstadoGrupo(estadoCerrado);
        assertEquals(estadoCerrado, group.getGroupState());
    }

    @Test
    @DisplayName("Establecer estado null debe fallar")
    void setEstadoNullTest() {
        assertThrows(IllegalArgumentException.class, () -> group.setEstadoGrupo(null));
    }

    @Test
    @DisplayName("Grupo se cierra automáticamente cuando se llena")
    void testGroupStatusClosedWhenFull() {
        Group smallGroup = new Group(subject, 2, academicPeriod);
        
        smallGroup.inscribirEstudiante(student1);
        smallGroup.inscribirEstudiante(student2);
        
        assertTrue(smallGroup.getGroupState() instanceof StatusClosed);
        assertTrue(smallGroup.isFull());
        assertEquals(0, smallGroup.getCuposDisponibles());
        student1.getAllSchedules();
        
        assertThrows(RuntimeException.class, () -> 
            smallGroup.inscribirEstudiante(student3));
    }

    @Test
    @DisplayName("Grupo se abre cuando se libera cupo")
    void testGroupDesinscribirEstudiante() {
        Group fullGroup = new Group(subject, 2, academicPeriod);
        
        fullGroup.inscribirEstudiante(student1);
        fullGroup.inscribirEstudiante(student2);
        assertTrue(fullGroup.getGroupState() instanceof StatusClosed);

        fullGroup.unenrollStudent(student1);
        assertEquals(1, fullGroup.getInscritos());
        assertTrue(fullGroup.getGroupState() instanceof StatusOpen);
        assertFalse(fullGroup.isFull());
    }

    // ============ PRUEBAS DE INSCRIPCIÓN DE ESTUDIANTES ============

    @Test
    @DisplayName("Inscribir estudiante exitosamente")
    void inscribirEstudianteTest() {
        group.enrollStudent(student1);
        List<Student> estudiantes = group.getEstudiantes();
        
        assertTrue(estudiantes.contains(student1));
        assertEquals(1, group.getInscritos());
        assertEquals(4, group.getCuposDisponibles());
    }

    @Test
    @DisplayName("Inscribir estudiante vía State Pattern")
    void inscribirEstudianteViaStatePatternTest() {
        group.inscribirEstudiante(student1);
        
        assertTrue(group.contieneEstudiante(student1));
        assertEquals(1, group.getInscritos());
        assertEquals(4, group.getCuposDisponibles());
    }

    @Test
    @DisplayName("No permitir inscribir estudiante duplicado")
    void enrollStudentDuplicadoTest() {
        group.enrollStudent(student1);
        assertThrows(IllegalArgumentException.class, () -> group.enrollStudent(student1));
    }

    @Test
    @DisplayName("No permitir inscribir estudiante null")
    void inscribirEstudianteNullTest() {
        assertThrows(IllegalArgumentException.class, () -> group.inscribirEstudiante(null));
    }

    @Test
    @DisplayName("Desinscribir estudiante exitosamente")
    void unenrollStudentTest() {
        group.enrollStudent(student1);
        group.enrollStudent(student2);

        boolean removido = group.unenrollStudent(student1);

        assertTrue(removido);
        assertEquals(1, group.getInscritos());
        assertFalse(group.contieneEstudiante(student1));
        assertTrue(group.contieneEstudiante(student2));
    }

    @Test
    @DisplayName("Desinscribir estudiante no inscrito debe fallar")
    void unenrollStudentNoExisteTest() {
        group.enrollStudent(student1);
        assertThrows(IllegalArgumentException.class, () -> group.unenrollStudent(student2));
        assertEquals(1, group.getInscritos());
    }

    @Test
    @DisplayName("Verificar si grupo contiene estudiante")
    void contieneEstudianteTest() {
        group.enrollStudent(student1);
        assertTrue(group.contieneEstudiante(student1));
        assertFalse(group.contieneEstudiante(student2));
    }

    @Test
    @DisplayName("Contiene estudiante null debe retornar false")
    void contieneEstudianteNullTest() {
        assertFalse(group.contieneEstudiante(null));
    }

    // ============ PRUEBAS DE CAPACIDAD Y CUPOS ============

    @Test
    @DisplayName("Calcular cupos disponibles correctamente")
    void cuposDisponiblesTest() {
        group.enrollStudent(student1);
        assertEquals(4, group.getCuposDisponibles());
    }

    @Test
    @DisplayName("Verificar si grupo está lleno")
    void isFullTest() {
        Group smallGroup = new Group(subject, 2, academicPeriod);
        smallGroup.enrollStudent(student1);
        smallGroup.enrollStudent(student2);
        assertTrue(smallGroup.isFull());
    }

    @Test
    @DisplayName("Verificar grupo no lleno")
    void invalidisFullTest() {
        group.enrollStudent(student1);
        group.enrollStudent(student2);
        assertFalse(group.isFull());
    }

    @Test
    @DisplayName("Cupos disponibles nunca deben ser negativos")
    void cuposDisponiblesNuncaNegativoTest() {
        // Llenar el grupo completamente
        for(int i = 0; i < 5; i++) {
            Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
            group.enrollStudent(s);
        }

        assertEquals(0, group.getCuposDisponibles());
        assertTrue(group.getCuposDisponibles() >= 0);
    }

    @Test
    @DisplayName("No permitir cambiar capacidad con estudiantes inscritos")
    void setCapacidadConEstudiantesInscritosTest() {
        group.enrollStudent(student1);
        assertThrows(IllegalStateException.class, () -> group.setCapacidad(10));
    }

    @Test
    @DisplayName("No permitir capacidad menor que estudiantes inscritos")
    void setCapacidadMenorQueInscritosTest() {
        group.enrollStudent(student1);
        group.enrollStudent(student2);
        
        // Remover estudiantes para permitir el cambio
        group.unenrollStudent(student1);
        group.unenrollStudent(student2);
        
        // Volver a agregar uno
        group.enrollStudent(student1);
        
        // Intentar poner capacidad menor que inscritos actuales
        assertThrows(IllegalArgumentException.class, () -> group.setCapacidad(0));
    }

    @Test
    @DisplayName("No permitir establecer capacidad inválida")
    void setCapacidadInvalidaTest() {
        assertThrows(IllegalArgumentException.class, () -> group.setCapacidad(0));
        assertThrows(IllegalArgumentException.class, () -> group.setCapacidad(-1));
    }

    // ============ PRUEBAS DE HORARIOS Y CONFLICTOS ============

    @Test
    @DisplayName("Detectar conflictos de horarios entre grupos")
    void testGroupScheduleConflicts() {
        Group group1 = new Group(subject, 10, academicPeriod);
        Group group2 = new Group(subject, 10, academicPeriod);
        
        group1.addSchedule(schedule1);
        group2.addSchedule(scheduleConflict); // Se solapa con schedule1
        
        assertTrue(group1.conflictoConHorario(group2));
        assertTrue(group2.conflictoConHorario(group1));
    }

    @Test
    @DisplayName("No detectar conflictos cuando no los hay")
    void testGroupNoScheduleConflicts() {
        Group group1 = new Group(subject, 10, academicPeriod);
        Group group2 = new Group(subject, 10, academicPeriod);

        group1.addSchedule(schedule1); // Lunes 8-10
        group2.addSchedule(schedule2); // Martes 10-12
        
        assertFalse(group1.conflictoConHorario(group2));
        assertFalse(group2.conflictoConHorario(group1));
    }

    // ============ PRUEBAS DE COMPARACIÓN Y UTILIDADES ============

    @Test
    void notEqualsTest() {
        Group group1 = new Group(subject, 5, academicPeriod);
        Group group2 = new Group(subject, 5, academicPeriod);

        group1.setId(1);
        group2.setId(2);

        assertNotEquals(group1, group2);
    }

    @Test
    @DisplayName("Lista de estudiantes debe ser inmutable")
    void getEstudiantesInmutableTest() {
        group.enrollStudent(student1);
        List<Student> estudiantes = group.getEstudiantes();

        assertThrows(UnsupportedOperationException.class, () -> estudiantes.add(student2));
    }

    @Test
    @DisplayName("toString() debe contener información relevante")
    void toStringTest() {
        group.setId(100000);
        group.setAula("A101");
        group.enrollStudent(student1);

        String resultado = group.toString();

        assertTrue(resultado.contains("100000"));  // ID
        assertTrue(resultado.contains("5"));       // capacidad
        assertTrue(resultado.contains("1"));       // inscritos
        assertTrue(resultado.contains("A101"));    // aula
    }

    // ============ PRUEBAS DE FUNCIONAMIENTO DEL STATE PATTERN ============

    @Test
    @DisplayName("Estado abierto permite inscripciones")
    void testGroupStatusOpen() {
        assertTrue(group.getGroupState() instanceof StatusOpen);
        assertFalse(group.isFull());
        
        group.inscribirEstudiante(student1);
        assertEquals(1, group.getInscritos());
        assertEquals(4, group.getCuposDisponibles());
        
        group.inscribirEstudiante(student2);
        assertEquals(2, group.getInscritos());
        assertEquals(3, group.getCuposDisponibles());
    }

    @Test
    @DisplayName("Lista de estudiantes inicializada correctamente")
    void listaEstudiantesInicializadaTest() {
        assertNotNull(group.getEstudiantes());
        assertTrue(group.getEstudiantes().isEmpty());
        assertEquals(0, group.getInscritos());
    }
}