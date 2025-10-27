package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

@SpringBootTest
class ScheduleTest {

    @Test
    void constructorValido_creaObjeto() {
        try { 
            Schedule s = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            assertEquals(DiasSemana.LUNES, s.getDay());
            assertEquals(LocalTime.of(8, 0), s.getStartHour());
            assertEquals(LocalTime.of(10, 0), s.getEndHour());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear un horario válido: " + e.getMessage());
        }    
    }

    @Test
    void constructorInvalido_lanzaExcepcion() {
        assertThrows(SirhaException.class, () -> new Schedule(DiasSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(8, 0)));
        assertThrows(SirhaException.class, () -> new Schedule(DiasSemana.MARTES, LocalTime.of(5, 0), LocalTime.of(5, 0)));
    }


    @Test
    void seSolapaCon_mismoDia_horariosSeCruzan() {
        try {
            Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule s2 = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
            assertTrue(s1.overlapsWith(s2));
            assertTrue(s2.overlapsWith(s1));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear horarios válidos: " + e.getMessage());
        }
    }

    @Test
    void seSolapaCon_mismoDia_horariosNoSeCruzan() {
        try {
            Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule s2 = new Schedule(DiasSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(12, 0)); // justo al final
            assertFalse(s1.overlapsWith(s2));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear horarios válidos: " + e.getMessage());
        }
    }

    @Test
    void seSolapaCon_diasDistintos_noSeCruzan() {
        try {
            Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule s2 = new Schedule(DiasSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(11, 0));
            assertFalse(s1.overlapsWith(s2));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear horarios válidos: " + e.getMessage());
        }
    }

    @Test
    void toString_devuelveTextoEsperado() {
        try { 
            Schedule s = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0));
            String texto = s.toString();
            System.out.println(texto);
            assertTrue(texto.contains("MIERCOLES"));
            assertTrue(texto.contains("14"));
            assertTrue(texto.contains("16"));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear un horario válido: " + e.getMessage());
        }
    }

    @Test
    void equals_and_hashCode_behaviour() {
        try {
            Schedule a = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule b = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule diffStart = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(10, 0));
            Schedule diffEnd = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(11, 0));
            Schedule diffDay = new Schedule(DiasSemana.MARTES, LocalTime.of(8, 0), LocalTime.of(10, 0));

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());

            assertTrue(a.equals(b) && b.equals(a));

            assertNotEquals(a, diffStart);
            assertNotEquals(a, diffEnd);
            assertNotEquals(a, diffDay);

            assertNotEquals(null, a);
            assertNotEquals("not-a-schedule", a);

        } catch (SirhaException e) {
            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void equals_sameInstance_true() {
        try {
            Schedule s = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(12, 0), LocalTime.of(14, 0));
            assertEquals(s, s);
        } catch (SirhaException e) {
            fail(e);
        }
    }

    @Test
    void nonOverlap_whenThisStartsAfterOtherEnds_and_symmetry() throws SirhaException {
        Schedule a = new Schedule(DiasSemana.LUNES, LocalTime.of(10,0), LocalTime.of(12,0));
        Schedule b = new Schedule(DiasSemana.LUNES, LocalTime.of(8,0), LocalTime.of(10,0));
        assertFalse(a.overlapsWith(b));
        assertFalse(b.overlapsWith(a));
    }

    @Test
    void overlap_whenOneInsideOther() throws SirhaException {
        Schedule outer = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(8,0), LocalTime.of(12,0));
        Schedule inner = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(9,0), LocalTime.of(10,0));
        assertTrue(outer.overlapsWith(inner));
        assertTrue(inner.overlapsWith(outer));
    }

    @Test
    void equals_returnsFalse_forNullAndOtherTypes_and_hashCodeDiffersForDifferentSchedules() throws SirhaException {
        Schedule s = new Schedule(DiasSemana.VIERNES, LocalTime.of(7,0), LocalTime.of(9,0));
        assertNotEquals(null, s);
        assertNotEquals("not-a-schedule", s);

        Schedule s2 = new Schedule(DiasSemana.VIERNES, LocalTime.of(7,0), LocalTime.of(10,0));
        assertNotEquals(s, s2);
        assertNotEquals(s.hashCode(), s2.hashCode());
    }
}