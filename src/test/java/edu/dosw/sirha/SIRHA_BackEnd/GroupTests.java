package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.DiasSemana;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusClosed;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusOpen;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GroupTests {

    @Test
    void groupTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        assertEquals(5, g.getCapacidad());
    }

    @Test
    void setEstadoTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        GroupState estado = new StatusClosed();
        g.setEstadoGrupo(estado);
        assertEquals(estado, g.getGroupState());
    }

    @Test
    void inscribirEstudianteTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student jacobo = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        g.enrollStudent(jacobo);
        List<Student> estudiantes = g.getEstudiantes();
        assertTrue(estudiantes.contains(jacobo));
    }

    @Test
    void cuposDisponiblesTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student jacobo = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        g.enrollStudent(jacobo);
        assertEquals(4, g.getCuposDisponibles());
    }

    @Test
    void isFullTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(4, period);
        for(int i = 0; i < 4; i++){
            Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
            g.enrollStudent(s);
        }
        assertTrue(g.isFull());
    }

    @Test
    void invalidisFullTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(4, period);
        for(int i = 0; i < 3; i++){
            Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
            g.enrollStudent(s);
        }
        assertFalse(g.isFull());
    }

    @Test
    void contieneEstudianteTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(4, period);
        Student s = new Student("test", "test@email.com", "hash", "20231001");
        g.enrollStudent(s);
        assertTrue(g.contieneEstudiante(s));
    }

    @Test
    void verificateIdTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(4, period);
        g.setId(11111);
        assertEquals(11111, g.getId());
    }

    @Test
    void verificarAulaTest(){
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(4, period);
        g.setAula("Bloque A");
        assertEquals("Bloque A", g.getAula());
    }

    // ==================== PRUEBAS ADICIONALES COMPLETADAS ====================

    @Test
    void unenrollStudentTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.enrollStudent(s1);
        g.enrollStudent(s2);

        boolean removido = g.unenrollStudent(s1);

        assertTrue(removido);
        assertEquals(1, g.getInscritos());
        assertFalse(g.contieneEstudiante(s1));
        assertTrue(g.contieneEstudiante(s2));
    }

    @Test
    void unenrollStudentNoExisteTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.enrollStudent(s1);
        assertThrows(IllegalArgumentException.class, () -> {
                g.unenrollStudent(s2);
            });
        

        assertEquals(1, g.getInscritos());
    }

    @Test
    void inscribirEstudianteViaStatePatternTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");

        g.inscribirEstudiante(s1);

        assertTrue(g.contieneEstudiante(s1));
        assertEquals(1, g.getInscritos());
        assertEquals(4, g.getCuposDisponibles());
    }

    @Test
    void setProfesorTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Professor profesor = new Professor("Dr. Smith", "smith@university.edu", "hash", "Matemáticas");

        g.setProfesor(profesor);

        assertEquals(profesor, g.getProfesor());
    }

    @Test
    void setCursoTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Subject curso = new Subject("101", "Cálculo I", 4);
        
        g.setCurso(curso);

        assertEquals(curso, g.getCurso());
    }

    @Test
    void setCapacidadConEstudiantesInscritosTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        g.enrollStudent(s1);

        // No debería permitir cambiar capacidad con estudiantes inscritos
        assertThrows(IllegalStateException.class, () -> {
            g.setCapacidad(10);
        });
    }

    @Test
    void setCapacidadMenorQueInscritosTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student( "jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.enrollStudent(s1);
        g.enrollStudent(s2);

        // Remover estudiantes para permitir el cambio
        g.unenrollStudent(s1);
        g.unenrollStudent(s2);

        // Volver a agregar uno
        g.enrollStudent(s1);

        // Intentar poner capacidad menor que inscritos actuales
        assertThrows(IllegalArgumentException.class, () -> {
            g.setCapacidad(0);
        });
    }

    @Test
    void enrollStudentDuplicadoTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");

        g.enrollStudent(s1);

        assertThrows(IllegalArgumentException.class, () -> {
            g.enrollStudent(s1);
        });
    }


    @Test
    void inscribirEstudianteNullTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);

        assertThrows(IllegalArgumentException.class, () -> {
            g.inscribirEstudiante(null);
        });
    }

    @Test
    void setEstadoNullTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);

        assertThrows(IllegalArgumentException.class, () -> {
            g.setEstadoGrupo(null);
        });
    }

    @Test
    void contieneEstudianteNullTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        assertFalse(g.contieneEstudiante(null));
    }

    @Test
    void getEstudiantesInmutableTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student( "jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.enrollStudent(s1);
        List<Student> estudiantes = g.getEstudiantes();

        // La lista devuelta debe ser inmutable
        assertThrows(UnsupportedOperationException.class, () -> {
            estudiantes.add(s2);
        });
    }

    @Test
    void cuposDisponiblesNuncaNegativoTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        // Llenar el grupo completamente
        for(int i = 0; i < 5; i++) {
            Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
            g.enrollStudent(s);
        }

        // Los cupos disponibles nunca deben ser negativos
        assertEquals(0, g.getCuposDisponibles());
        assertTrue(g.getCuposDisponibles() >= 0);
    }

    @Test
    void toStringTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        Student s1 = new Student( "jacobo", "jacobo@test.com", "hash123", "20231001");

        g.setId(100000);
        g.setAula("A101");
        g.enrollStudent(s1);

        String resultado = g.toString();

        assertTrue(resultado.contains("100000"));
        assertTrue(resultado.contains("5")); // capacidad
        assertTrue(resultado.contains("1")); // inscritos
        assertTrue(resultado.contains("A101")); // aula
    }

    @Test
    void equalsTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group group1 = new Group(5, period);
        Group group2 = new Group(5, period);

        group1.setId(1);
        group2.setId(1);

        assertEquals(group1, group2);
    }

    @Test
    void notEqualsTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group group1 = new Group(5, period);
        Group group2 = new Group(5, period);

        group1.setId(1);
        group2.setId(2);

        assertNotEquals(group1, group2);
    }

    @Test
    void capacidadInvalidaTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        assertThrows(IllegalArgumentException.class, () -> {
            new Group(0, period);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Group(-5, period);
        });
    }

    @Test
    void setCapacidadInvalidaTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);

        assertThrows(IllegalArgumentException.class, () -> {
            g.setCapacidad(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            g.setCapacidad(-1);
        });
    }

    @Test
    void estadoInicialEsAbiertoTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        // Verificar que el estado inicial es StatusOpen
        assertTrue(g.getGroupState() instanceof StatusOpen);
    }

    @Test
    void listaEstudiantesInicializadaTest() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group g = new Group(5, period);
        // La lista de estudiantes debe estar inicializada
        assertNotNull(g.getEstudiantes());
        assertTrue(g.getEstudiantes().isEmpty());
        assertEquals(0, g.getInscritos());
    }
    @Test
    void testGroupStatusOpen() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group group = new Group(5, period);
        
        assertTrue(group.getGroupState() instanceof StatusOpen);
        assertFalse(group.isFull());
        
        group.inscribirEstudiante(new Student("testuser", "testuser@example.com", "password", "EST001"));
        assertEquals(1, group.getInscritos());
        assertEquals(4, group.getCuposDisponibles());
        group.inscribirEstudiante(new Student("testuser2", "testuser2@example.com", "password", "EST002"));
        assertEquals(2, group.getInscritos());
        assertEquals(3, group.getCuposDisponibles());
    }

    @Test
    void testGroupStatusClosedWhenFull() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group group = new Group(2, period); // Capacidad pequeña
        
        group.inscribirEstudiante(new Student("testuser", "testuser@example.com", "password", "EST001"));
        group.inscribirEstudiante(new Student("testuser2", "testuser2@example.com", "password", "EST002"));
        
        assertTrue(group.getGroupState() instanceof StatusClosed);
        assertTrue(group.isFull());
        assertEquals(0, group.getCuposDisponibles());
        
        assertThrows(RuntimeException.class, () -> group.inscribirEstudiante(new Student("testuser3", "testuser3@example.com", "password", "EST003")));
    }

    @Test
    void testGroupDesinscribirEstudiante() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group group = new Group(3, period);
        Student student = new Student("testuser", "testuser@example.com", "password", "EST001");
        group.inscribirEstudiante(student);
        group.inscribirEstudiante(new Student("testuser2", "testuser2@example.com", "password", "EST002"));
        group.inscribirEstudiante(new Student("testuser3", "testuser3@example.com", "password", "EST003"));
        assertTrue(group.getGroupState() instanceof StatusClosed);

        group.unenrollStudent(student);
        assertEquals(2, group.getInscritos());
        assertTrue(group.getGroupState() instanceof StatusOpen);
        assertFalse(group.isFull());
    }

    @Test
    void testGroupScheduleConflicts() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group group1 = new Group(10, period);
        Group group2 = new Group(10, period);
        
        // Agregar horarios que se solapan
        Schedule horario1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        Schedule horario2 = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0)); // Conflicto

        group1.addSchedule(horario1);
        group2.addSchedule(horario2);
        
        // Verificar conflicto
        assertTrue(group1.conflictoConHorario(group2));
        assertTrue(group2.conflictoConHorario(group1));
    }

    @Test
    void testGroupNoScheduleConflicts() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Group group1 = new Group(10, period);
        Group group2 = new Group(10, period);
        
        // Agregar horarios que NO se solapan
        Schedule horario1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        Schedule horario2 = new Schedule(DiasSemana.MARTES, LocalTime.of(8, 0), LocalTime.of(10, 0)); // Diferente día

        group1.addSchedule(horario1);
        group2.addSchedule(horario2);
        
        // No debería haber conflicto
        assertFalse(group1.conflictoConHorario(group2));
        assertFalse(group2.conflictoConHorario(group1));
    }

}