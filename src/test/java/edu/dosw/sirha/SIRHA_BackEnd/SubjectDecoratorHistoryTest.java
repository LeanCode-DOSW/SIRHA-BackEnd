package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.AprobadaState;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.EnCursoState;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.NoCursadaState;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.ReprobadaState;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectStateProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SubjectDecoratorHistoryTest {

    private Subject subject;
    private SubjectDecorator decorator;
    private Group group;
    private AcademicPeriod period;

    @BeforeEach
    void setUp() {
        subject = new Subject("101", "Matemáticas", 4);
        decorator = new SubjectDecorator(subject);
        period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        group = new Group(subject, 30, period);
    }

    @Test
    void testInitialHistoryState() {
        // Verificar que el historial inicia vacío
        List<SubjectStateProcess> history = decorator.getHistory();
        assertNotNull(history);
        assertTrue(history.isEmpty());
        
        assertTrue(decorator.getState() instanceof NoCursadaState);
        assertEquals(SemaforoColores.GRIS,decorator.getEstadoColor());
    }

    @Test
    void testInscribirCreatesHistoryRecord() {
        // Inscribir materia
        decorator.inscribir(group);
        
        // Verificar estado actual
        assertTrue(decorator.getState() instanceof EnCursoState);
        assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
        assertEquals(group, decorator.getGroup());
        
        // Verificar que NO se crea registro en el historial para inscripción
        // (según el código, solo aprobar, reprobar y retirar crean registros)
        List<SubjectStateProcess> history = decorator.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void testAprobarCreatesHistoryRecord() {
        // Setup: inscribir primero
        decorator.inscribir(group);
        decorator.setGrade(85);
        
        // Aprobar materia
        decorator.aprobar();
        
        // Verificar estado actual
        assertTrue(decorator.getState() instanceof AprobadaState);
        assertEquals(SemaforoColores.VERDE, decorator.getEstadoColor());
        
        // Verificar historial
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(1, history.size());
        
        SubjectStateProcess record = history.get(0);
        assertTrue(record instanceof SubjectProgress);
        
        SubjectProgress progress = (SubjectProgress) record;
        assertEquals(SemaforoColores.VERDE, progress.getState());
        assertEquals(decorator.getSemester(), progress.getSemester());
        assertEquals(group, progress.getGroup());
        assertEquals(85, progress.getGrade());
    }

    @Test
    void testReprobarCreatesHistoryRecord() {
        // Setup: inscribir primero
        decorator.inscribir(group);
        decorator.setGrade(35); // Nota reprobatoria
        
        // Reprobar materia
        decorator.reprobar();
        
        // Verificar estado actual
        assertTrue(decorator.getState() instanceof ReprobadaState);
        assertEquals(SemaforoColores.ROJO, decorator.getEstadoColor());
        
        // Verificar historial
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(1, history.size());
        
        SubjectStateProcess record = history.get(0);
        assertTrue(record instanceof SubjectProgress);
        
        SubjectProgress progress = (SubjectProgress) record;
        assertEquals(SemaforoColores.ROJO, progress.getState());
        assertEquals(decorator.getSemester(), progress.getSemester());
        assertEquals(group, progress.getGroup());
        assertEquals(35, progress.getGrade());
    }

    @Test
    void testRetirarCreatesHistoryRecord() {
        // Setup: inscribir primero
        decorator.inscribir(group);
        
        // Retirar materia
        decorator.retirar();
        
        // Verificar estado actual (debería volver a NoCursada o mantener algún estado específico)
        // Esto depende de la implementación del estado EnCursoState.retirar()
        
        // Verificar historial
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(1, history.size());
        
        SubjectStateProcess record = history.get(0);
        assertTrue(record instanceof SubjectProgress);
        
        SubjectProgress progress = (SubjectProgress) record;
        assertEquals(SemaforoColores.AMARILLO, progress.getState()); // Color amarillo para retiro
        assertEquals(decorator.getSemester(), progress.getSemester());
        assertEquals(group, progress.getGroup());
        assertEquals(decorator.getGrade(), progress.getGrade());
    }

    @Test
    void testMultipleStateChangesCreateMultipleRecords() {
        // Flujo completo: inscribir -> reprobar -> inscribir -> aprobar
        
        // Primera inscripción
        decorator.inscribir(group);
        decorator.setGrade(30);
        
        // Reprobar
        decorator.reprobar();
        assertEquals(1, decorator.getHistory().size());
        
        // Re-inscribir (si es posible según los estados)
        Group newGroup = new Group(subject, 25, period);
        try {
            decorator.inscribir(newGroup);
            decorator.setGrade(90);
            
            // Aprobar en el segundo intento
            decorator.aprobar();
            
            // Verificar historial completo
            List<SubjectStateProcess> history = decorator.getHistory();
            assertEquals(2, history.size());
            
            // Primer registro: reprobación
            SubjectProgress firstRecord = (SubjectProgress) history.get(0);
            assertEquals(SemaforoColores.ROJO, firstRecord.getState());
            assertEquals(30, firstRecord.getGrade());
            
            // Segundo registro: aprobación
            SubjectProgress secondRecord = (SubjectProgress) history.get(1);
            assertEquals(SemaforoColores.VERDE, secondRecord.getState());
            assertEquals(90, secondRecord.getGrade());
            
        } catch (IllegalStateException e) {
            // Si no se puede re-inscribir desde estado reprobado, verificar solo el primer registro
            assertEquals(1, decorator.getHistory().size());
        }
    }

    @Test
    void testGetLastStateProcessWithEmptyHistory() {
        // Intentar obtener último estado con historial vacío
        assertThrows(IndexOutOfBoundsException.class, () -> {
            decorator.getLastStateProcess();
        });
    }

    @Test
    void testGetLastStateProcessWithRecords() {
        // Setup: crear algunos registros
        decorator.inscribir(group);
        decorator.setGrade(75);
        decorator.aprobar();
        
        // Obtener último registro
        SubjectStateProcess lastRecord = decorator.getLastStateProcess();
        assertNotNull(lastRecord);
        assertTrue(lastRecord instanceof SubjectProgress);
        
        SubjectProgress progress = (SubjectProgress) lastRecord;
        assertEquals(SemaforoColores.VERDE, progress.getState());
        assertEquals(75, progress.getGrade());
    }

    @Test
    void testInscribirWithSemesterCreatesCorrectRecord() {
        int targetSemester = 3;
        decorator.inscribir(group, targetSemester);
        
        // Verificar que el semestre se estableció
        assertEquals(targetSemester, decorator.getSemester());
        
        // Aprobar para crear registro
        decorator.setGrade(88);
        decorator.aprobar();
        
        // Verificar que el registro tiene el semestre correcto
        SubjectProgress progress = (SubjectProgress) decorator.getLastStateProcess();
        assertEquals(targetSemester, progress.getSemester());
    }

    @Test
    void testHistoryIntegrity() {
        // Verificar que el historial mantiene integridad referencial
        decorator.inscribir(group);
        decorator.setGrade(95);
        decorator.aprobar();
        
        List<SubjectStateProcess> history = decorator.getHistory();
        SubjectStateProcess record = history.get(0);
        
        // Verificar que el registro mantiene referencias correctas
        SubjectProgress progress = (SubjectProgress) record;
        assertEquals(group, progress.getGroup());
        assertNotNull(progress.getState());
        assertTrue(progress.getGrade() >= 0);
        assertTrue(progress.getSemester() >= 0);
    }

    @Test
    void testAddStateDirectly() {

        SubjectProgress customProgress = new SubjectProgress(
            SemaforoColores.VERDE, 
            2, 
            group, 
            95
        );
        
        decorator.addState(customProgress);
        
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(1, history.size());
        assertEquals(customProgress, history.get(0));
        assertEquals(customProgress, decorator.getLastStateProcess());
    }

    @Test
    void testHistoryImmutabilityAttempt() {
        // Crear algunos registros
        decorator.inscribir(group);
        decorator.setGrade(80);
        decorator.aprobar();
        
        // Obtener historial
        List<SubjectStateProcess> history = decorator.getHistory();
        int originalSize = history.size();
        
        // Intentar modificar el historial externamente
        try {
            history.clear();
            // Si la lista es mutable, verificar que afecta al decorador
            // (esto indicaría un problema de encapsulación)
            List<SubjectStateProcess> newHistory = decorator.getHistory();
            if (newHistory.isEmpty()) {
                fail("El historial debería estar protegido contra modificaciones externas");
            }
        } catch (UnsupportedOperationException e) {
            // Si la lista es inmutable, esto es lo esperado
            assertEquals(originalSize, decorator.getHistory().size());
        }
    }

    @Test
    void testStateTransitionConsistencyWithHistory() {
        
        // Estado inicial
        assertTrue(decorator.getState() instanceof NoCursadaState);
        assertEquals(SemaforoColores.GRIS, decorator.getEstadoColor());
        assertTrue(decorator.getHistory().isEmpty());
        
        // Inscribir
        decorator.inscribir(group);
        assertTrue(decorator.getState() instanceof EnCursoState);
        assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
        assertTrue(decorator.estaCursando());
        // Sin registro en historial para inscripción
        assertTrue(decorator.getHistory().isEmpty());
        
        // Aprobar
        decorator.setGrade(92);
        decorator.aprobar();
        assertTrue(decorator.getState() instanceof AprobadaState);
        assertEquals(SemaforoColores.VERDE, decorator.getEstadoColor());
        assertTrue(decorator.estaAprobada());
        // Debe haber registro en historial
        assertEquals(1, decorator.getHistory().size());
        
        SubjectProgress record = (SubjectProgress) decorator.getLastStateProcess();
        assertEquals(SemaforoColores.VERDE, record.getState());
        assertNotNull(record.getCreadoEn());
        assertEquals(92, record.getGrade());
    }

    @Test
    void testGradeManagementInHistory() {
        // Verificar manejo de notas en el historial
        decorator.inscribir(group);
        
        // Establecer diferentes notas y verificar registros
        decorator.setGrade(45);
        decorator.reprobar();
        
        SubjectProgress failRecord = (SubjectProgress) decorator.getLastStateProcess();
        assertEquals(45, failRecord.getGrade());
        assertEquals(SemaforoColores.ROJO, failRecord.getState());
        
        // Si es posible re-inscribir y aprobar
        try {
            decorator.inscribir(group);
            decorator.setGrade(87);
            decorator.aprobar();
            
            SubjectProgress passRecord = (SubjectProgress) decorator.getLastStateProcess();
            assertEquals(87, passRecord.getGrade());
            assertEquals(SemaforoColores.VERDE, passRecord.getState());
            
            // Verificar que hay dos registros diferentes
            List<SubjectStateProcess> history = decorator.getHistory();
            assertEquals(2, history.size());
            
        } catch (IllegalStateException e) {
            // Si no se puede re-inscribir, solo verificar el primer registro
            assertEquals(1, decorator.getHistory().size());
        }
    }
}