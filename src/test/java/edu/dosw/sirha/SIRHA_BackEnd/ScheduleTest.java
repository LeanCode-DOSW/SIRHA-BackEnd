package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.DiasSemana;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {
        @Test
        void constructorValido_creaObjeto() {
            Schedule s = new Schedule(DiasSemana.LUNES, 8, 10);
            assertEquals(DiasSemana.LUNES, s.getDia());
            assertEquals(8, s.getHoraInicio());
            assertEquals(10, s.getHoraFin());
        }

        @Test
        void constructorInvalido_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Schedule(DiasSemana.LUNES, 10, 8));
            assertThrows(IllegalArgumentException.class, () -> new Schedule(DiasSemana.MARTES, 5, 5));
        }

        @Test
        void seSolapaCon_mismoDia_horariosSeCruzan() {
            Schedule s1 = new Schedule(DiasSemana.LUNES, 8, 10);
            Schedule s2 = new Schedule(DiasSemana.LUNES, 9, 11);
            assertTrue(s1.seSolapaCon(s2));
            assertTrue(s2.seSolapaCon(s1));
        }

        @Test
        void seSolapaCon_mismoDia_horariosNoSeCruzan() {
            Schedule s1 = new Schedule(DiasSemana.LUNES, 8, 10);
            Schedule s2 = new Schedule(DiasSemana.LUNES, 10, 12); // justo al final
            assertFalse(s1.seSolapaCon(s2));
        }

        @Test
        void seSolapaCon_diasDistintos_noSeCruzan() {
            Schedule s1 = new Schedule(DiasSemana.LUNES, 8, 10);
            Schedule s2 = new Schedule(DiasSemana.MARTES, 9, 11);
            assertFalse(s1.seSolapaCon(s2));
        }

        @Test
        void toString_devuelveTextoEsperado() {
            Schedule s = new Schedule(DiasSemana.MIERCOLES, 14, 16);
            String texto = s.toString();
            System.out.println(texto);
            assertTrue(texto.contains("MIERCOLES"));
            assertTrue(texto.contains("14"));
            assertTrue(texto.contains("16"));
        }
}