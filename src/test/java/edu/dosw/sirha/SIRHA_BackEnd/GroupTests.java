package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GroupTests {

    @Test
    public void groupTest(){
        Group g = new Group(5);
        assertEquals(5, g.getCapacidad());
    }

    @Test
    public void setEstadoTest(){
        Group g = new Group(5);
        GroupState estado = new StatusClosed();
        g.setEstadoGrupo(estado);
        assertEquals(estado, g.getEstadoGrupo());
    }

    @Test
    public void inscribirEstudianteTest(){
        Group g = new Group(5);
        Student jacobo = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        g.addEstudiante(jacobo);
        List<Student> estudiantes = g.getEstudiantes();
        assertTrue(estudiantes.contains(jacobo));
    }

    @Test
    public void cuposDisponiblesTest(){
        Group g = new Group(5);
        Student jacobo = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        g.addEstudiante(jacobo);
        assertEquals(4, g.getCuposDisponibles());
    }

    @Test
    public void estaLLenoTest(){
        Group g = new Group(4);
        for(int i = 0; i < 4; i++){
            Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
            g.addEstudiante(s);
        }
        assertTrue(g.estaLleno());
    }

    @Test
    public void inavlidEstaLLenoTest(){
        Group g = new Group(4);
        for(int i = 0; i < 3; i++){
            Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
            g.addEstudiante(s);
        }
        assertFalse(g.estaLleno());
    }

    @Test
    public void contieneEstudianteTest(){
        Group g = new Group(4);
        Student s = new Student("test", "test@email.com", "hash", "20231001");
        g.addEstudiante(s);
        assertTrue(g.contieneEstudiante(s));
    }

    @Test
    public void verificateIdTest(){
        Group g = new Group(4);
        g.setId(11111);
        assertEquals(11111, g.getId());
    }

    @Test
    public void verificarAulaTest(){
        Group g = new Group(4);
        g.setAula("Bloque A");
        assertEquals("Bloque A", g.getAula());
    }

    // ==================== PRUEBAS ADICIONALES COMPLETADAS ====================

    @Test
    public void removerEstudianteTest() {
        Group g = new Group(5);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.addEstudiante(s1);
        g.addEstudiante(s2);

        boolean removido = g.removerEstudiante(s1);

        assertTrue(removido);
        assertEquals(1, g.getInscritos());
        assertFalse(g.contieneEstudiante(s1));
        assertTrue(g.contieneEstudiante(s2));
    }

    @Test
    public void removerEstudianteNoExisteTest() {
        Group g = new Group(5);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.addEstudiante(s1);

        boolean removido = g.removerEstudiante(s2);

        assertFalse(removido);
        assertEquals(1, g.getInscritos());
    }

    @Test
    public void inscribirEstudianteViaStatePatternTest() {
        Group g = new Group(5);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");

        // Test usando el método que delega al State Pattern
        g.inscribirEstudiante(s1);

        assertTrue(g.contieneEstudiante(s1));
        assertEquals(1, g.getInscritos());
        assertEquals(4, g.getCuposDisponibles());
    }

    @Test
    public void setProfesorTest() {
        Group g = new Group(5);
        Professor profesor = new Professor("Dr. Smith", "smith@university.edu", "hash", "Matemáticas");

        g.setProfesor(profesor);

        assertEquals(profesor, g.getProfesor());
    }

    @Test
    public void setCursoTest() {
        Group g = new Group(5);
        Subject curso = new Subject(101, "Cálculo I", 4);
        
        g.setCurso(curso);

        assertEquals(curso, g.getCurso());
    }

    @Test
    public void setCapacidadConEstudiantesInscritosTest() {
        Group g = new Group(5);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
        g.addEstudiante(s1);

        // No debería permitir cambiar capacidad con estudiantes inscritos
        assertThrows(IllegalStateException.class, () -> {
            g.setCapacidad(10);
        });
    }

    @Test
    public void setCapacidadMenorQueInscritosTest() {
        Group g = new Group(5);
        Student s1 = new Student( "jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.addEstudiante(s1);
        g.addEstudiante(s2);

        // Remover estudiantes para permitir el cambio
        g.removerEstudiante(s1);
        g.removerEstudiante(s2);

        // Volver a agregar uno
        g.addEstudiante(s1);

        // Intentar poner capacidad menor que inscritos actuales
        assertThrows(IllegalArgumentException.class, () -> {
            g.setCapacidad(0);
        });
    }

    @Test
    public void addEstudianteDuplicadoTest() {
        Group g = new Group(5);
        Student s1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");

        g.addEstudiante(s1);

        assertThrows(IllegalArgumentException.class, () -> {
            g.addEstudiante(s1);
        });
    }

    @Test
    public void addEstudianteNullTest() {
        Group g = new Group(5);

        assertThrows(IllegalArgumentException.class, () -> {
            g.addEstudiante(null);
        });
    }

    @Test
    public void removerEstudianteNullTest() {
        Group g = new Group(5);

        assertThrows(IllegalArgumentException.class, () -> {
            g.removerEstudiante(null);
        });
    }

    @Test
    public void inscribirEstudianteNullTest() {
        Group g = new Group(5);

        assertThrows(IllegalArgumentException.class, () -> {
            g.inscribirEstudiante(null);
        });
    }

    @Test
    public void setEstadoNullTest() {
        Group g = new Group(5);

        assertThrows(IllegalArgumentException.class, () -> {
            g.setEstadoGrupo(null);
        });
    }

    @Test
    public void contieneEstudianteNullTest() {
        Group g = new Group(5);
        assertFalse(g.contieneEstudiante(null));
    }

    @Test
    public void getEstudiantesInmutableTest() {
        Group g = new Group(5);
        Student s1 = new Student( "jacobo", "jacobo@test.com", "hash123", "20231001");
        Student s2 = new Student("maria", "maria@test.com", "hash456", "20231002");

        g.addEstudiante(s1);
        List<Student> estudiantes = g.getEstudiantes();

        // La lista devuelta debe ser inmutable
        assertThrows(UnsupportedOperationException.class, () -> {
            estudiantes.add(s2);
        });
    }

    @Test
    public void cuposDisponiblesNuncaNegativoTest() {
        Group g = new Group(5);
        // Llenar el grupo completamente
        for(int i = 0; i < 5; i++) {
            Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
            g.addEstudiante(s);
        }

        // Los cupos disponibles nunca deben ser negativos
        assertEquals(0, g.getCuposDisponibles());
        assertTrue(g.getCuposDisponibles() >= 0);
    }

    @Test
    public void toStringTest() {
        Group g = new Group(5);
        Student s1 = new Student( "jacobo", "jacobo@test.com", "hash123", "20231001");

        g.setId(100000);
        g.setAula("A101");
        g.addEstudiante(s1);

        String resultado = g.toString();

        assertTrue(resultado.contains("100000"));
        assertTrue(resultado.contains("5")); // capacidad
        assertTrue(resultado.contains("1")); // inscritos
        assertTrue(resultado.contains("A101")); // aula
    }

    @Test
    public void equalsTest() {
        Group group1 = new Group(5);
        Group group2 = new Group(5);

        group1.setId(1);
        group2.setId(1);

        assertEquals(group1, group2);
    }

    @Test
    public void notEqualsTest() {
        Group group1 = new Group(5);
        Group group2 = new Group(5);

        group1.setId(1);
        group2.setId(2);

        assertNotEquals(group1, group2);
    }

    @Test
    public void capacidadInvalidaTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Group(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Group(-5);
        });
    }

    @Test
    public void setCapacidadInvalidaTest() {
        Group g = new Group(5);

        assertThrows(IllegalArgumentException.class, () -> {
            g.setCapacidad(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            g.setCapacidad(-1);
        });
    }

    @Test
    public void estadoInicialEsAbiertoTest() {
        Group g = new Group(5);
        // Verificar que el estado inicial es StatusOpen
        assertTrue(g.getEstadoGrupo() instanceof StatusOpen);
    }

    @Test
    public void listaEstudiantesInicializadaTest() {
        Group g = new Group(5);
        // La lista de estudiantes debe estar inicializada
        assertNotNull(g.getEstudiantes());
        assertTrue(g.getEstudiantes().isEmpty());
        assertEquals(0, g.getInscritos());
    }

}