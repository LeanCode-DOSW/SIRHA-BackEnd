package edu.dosw.sirha.SIRHA_BackEnd;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestState;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CambioGrupoTest {

    private Student student;
    private Subject previousSubject;
    private Subject newSubject;
    private Group previousGroup;
    private Group newGroup;

    @BeforeEach
    void setUp() {
        student = new Student("00001", "Juan" , "juan@gmail.com", "12345", "10001" + String.format("%02d", 2));
        previousSubject = new Subject("DOSW301", "Desarrollo de software", 4);
        newSubject = new Subject("ODSC201", "Operaciones de software", 3);
        previousGroup = new Group(5);
        newGroup = new Group(7);
    }
    @Test
    void testEstadoInicialEsPendiente() {
        BaseRequest request = new CambioGrupo(
            previousSubject,
            previousGroup,
            newSubject,
            newGroup,
            "Cambio de materia por falta de creditos",
            student.getSolicitudes().size(),
            student
        );

        assertEquals(RequestState.PENDIENTE, request.getEstado());
    }

        @Test
        void testValidarCambioGrupo() {
            Subject previousSubject1 = new Subject("DOSW301", "Desarrollo de software", 4);
            Subject newSubject1 = new Subject("DOSW301", "Operaciones de software", 3);
            Semaforo semaforo = new Semaforo();
            semaforo.setColor(previousSubject1.getName(), SemaforoColores.VERDE);
            student.setSemaforo(semaforo);
            BaseRequest request = new CambioGrupo(
                previousSubject1,
                previousGroup,
                newSubject1,
                newGroup,
                "Cambio de materia por falta de creditos",
                student.getSolicitudes().size(),
                student
            );

            request.validar(newSubject1, newGroup);
    
            // Asumiendo que el semáforo del estudiante permite el cambio
            assertTrue(request.validar(newSubject, newGroup));
        }
    // PRUEBAS DE LA INTERFAZ
}
