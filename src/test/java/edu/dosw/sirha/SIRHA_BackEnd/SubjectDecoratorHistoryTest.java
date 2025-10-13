package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.AprobadaState;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.EnCursoState;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.NoCursadaState;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.ReprobadaState;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectStateProcess;

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
        try{
            group = new Group(subject, 30, period);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void testInitialHistoryState() {
        List<SubjectStateProcess> history = decorator.getHistory();
        assertNotNull(history);
        assertTrue(history.isEmpty());
        
        assertTrue(decorator.getState() instanceof NoCursadaState);
        assertEquals(SemaforoColores.GRIS,decorator.getEstadoColor());
    }

    @Test
    void testInscribirCreatesHistoryRecord() {
        decorator.inscribir(group);
        
        assertTrue(decorator.getState() instanceof EnCursoState);
        assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
        assertEquals(group, decorator.getGroup());
        
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void testAprobarCreatesHistoryRecord() {
        // Setup: inscribir primero
        decorator.inscribir(group);
        decorator.setGrade(85);
        
        decorator.aprobar();
        
        assertTrue(decorator.getState() instanceof AprobadaState);
        assertEquals(SemaforoColores.VERDE, decorator.getEstadoColor());
        
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(2, history.size());
        
        SubjectStateProcess stateRecord = history.get(1);
        assertTrue(stateRecord instanceof SubjectProgress);
        
        SubjectProgress progress = (SubjectProgress) stateRecord;
        assertEquals(SemaforoColores.VERDE, progress.getState());
        assertEquals(decorator.getSemester(), progress.getSemester());
        assertEquals(group, progress.getGroup());
        assertEquals(85, progress.getGrade());
    }

    @Test
    void testReprobarCreatesHistoryRecord() {
        decorator.inscribir(group);
        decorator.setGrade(35);
        
        decorator.reprobar();
        
        assertTrue(decorator.getState() instanceof ReprobadaState);
        assertEquals(SemaforoColores.ROJO, decorator.getEstadoColor());
        
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(2, history.size());
        
        SubjectStateProcess stateRecord = history.get(1);
        assertTrue(stateRecord instanceof SubjectProgress);
        
        SubjectProgress progress = (SubjectProgress) stateRecord;
        assertEquals(SemaforoColores.ROJO, progress.getState());
        assertEquals(decorator.getSemester(), progress.getSemester());
        assertEquals(group, progress.getGroup());
        assertEquals(35, progress.getGrade());
    }

    @Test
    void testRetirarCreatesHistoryRecord() {
        decorator.inscribir(group);
        
        decorator.retirar();
        
        List<SubjectStateProcess> history = decorator.getHistory();
        assertEquals(2, history.size());
        
        SubjectStateProcess stateRecord = history.get(1);
        assertTrue(stateRecord instanceof SubjectProgress);
        
        SubjectProgress progress = (SubjectProgress) stateRecord;
        assertEquals(SemaforoColores.AMARILLO, progress.getState());
        assertEquals(decorator.getSemester(), progress.getSemester());
        assertEquals(group, progress.getGroup());
        assertEquals(decorator.getGrade(), progress.getGrade());
    }

    @Test
    void testMultipleStateChangesCreateMultipleRecords() {
        decorator.inscribir(group);
        decorator.setGrade(30);        
        decorator.reprobar();
        assertEquals(2, decorator.getHistory().size());
        
        try {
            Group newGroup = new Group(subject, 25, period);
            decorator.inscribir(newGroup);
            decorator.setGrade(90);
            
            decorator.aprobar();
            
            List<SubjectStateProcess> history = decorator.getHistory();
            assertEquals(4, history.size());
            
            SubjectProgress firstRecord = (SubjectProgress) history.get(1);
            assertEquals(SemaforoColores.ROJO, firstRecord.getState());
            assertEquals(30, firstRecord.getGrade());
            
            SubjectProgress secondRecord = (SubjectProgress) history.get(3);
            assertEquals(SemaforoColores.VERDE, secondRecord.getState());
            assertEquals(90, secondRecord.getGrade()); 
        }
        catch (Exception e) {
            fail("No se esperaba una excepción al crear el nuevo grupo: " + e.getMessage());
        }
    }

    @Test
    void testGetLastStateProcessWithEmptyHistory() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            decorator.getLastStateProcess();
        });
    }

    @Test
    void testGetLastStateProcessWithRecords() {
        decorator.inscribir(group);
        decorator.setGrade(75);
        decorator.aprobar();
        
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
        
        assertEquals(targetSemester, decorator.getSemester());
        
        decorator.setGrade(88);
        decorator.aprobar();
        
        SubjectProgress progress = (SubjectProgress) decorator.getLastStateProcess();
        assertEquals(targetSemester, progress.getSemester());
    }

    @Test
    void testHistoryIntegrity() {
        decorator.inscribir(group);
        decorator.setGrade(95);
        decorator.aprobar();
        
        List<SubjectStateProcess> history = decorator.getHistory();
        SubjectStateProcess progressRecord = history.get(0);
        
        SubjectProgress progress = (SubjectProgress) progressRecord;
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
        decorator.inscribir(group);
        decorator.setGrade(80);
        decorator.aprobar();
        
        List<SubjectStateProcess> history = decorator.getHistory();
        int originalSize = history.size();
        
        try {
            history.clear();
            List<SubjectStateProcess> newHistory = decorator.getHistory();
            if (newHistory.isEmpty()) {
                fail("El historial debería estar protegido contra modificaciones externas");
            }
        } catch (UnsupportedOperationException e) {
            assertEquals(originalSize, decorator.getHistory().size());
        }
    }

    @Test
    void testStateTransitionConsistencyWithHistory() {
        
        assertTrue(decorator.getState() instanceof NoCursadaState);
        assertEquals(SemaforoColores.GRIS, decorator.getEstadoColor());
        assertTrue(decorator.getHistory().isEmpty());
        
        decorator.inscribir(group);
        assertTrue(decorator.getState() instanceof EnCursoState);
        assertEquals(SemaforoColores.AMARILLO, decorator.getEstadoColor());
        assertTrue(decorator.estaCursando());
        assertEquals(1, decorator.getHistory().size());
        
        decorator.setGrade(92);
        decorator.aprobar();
        assertTrue(decorator.getState() instanceof AprobadaState);
        assertEquals(SemaforoColores.VERDE, decorator.getEstadoColor());
        assertTrue(decorator.estaAprobada());
        assertEquals(2, decorator.getHistory().size());
        
        SubjectProgress progressRecord = (SubjectProgress) decorator.getLastStateProcess();
        assertEquals(SemaforoColores.VERDE, progressRecord.getState());
        assertNotNull(progressRecord.getCreatedAt());
        assertEquals(92, progressRecord.getGrade());
    }

    @Test
    void testGradeManagementInHistory() {
        decorator.inscribir(group);
        
        decorator.setGrade(45);
        decorator.reprobar();
        
        SubjectProgress failRecord = (SubjectProgress) decorator.getLastStateProcess();
        assertEquals(45, failRecord.getGrade());
        assertEquals(SemaforoColores.ROJO, failRecord.getState());
        
        try {
            decorator.inscribir(group);
            decorator.setGrade(87);
            decorator.aprobar();
            
            SubjectProgress passRecord = (SubjectProgress) decorator.getLastStateProcess();
            assertEquals(87, passRecord.getGrade());
            assertEquals(SemaforoColores.VERDE, passRecord.getState());
            
            List<SubjectStateProcess> history = decorator.getHistory();
            assertEquals(4, history.size());
            
        } catch (IllegalStateException e) {
            assertEquals(1, decorator.getHistory().size());
        }
    }
}