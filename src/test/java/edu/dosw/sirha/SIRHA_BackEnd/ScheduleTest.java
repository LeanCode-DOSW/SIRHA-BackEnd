package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {
        @Test
        void constructorValido_creaObjeto() {
            Schedule s = new Schedule("Lunes", 8, 10);
            assertEquals("Lunes", s.getDia());
            assertEquals(8, s.getHoraInicio());
            assertEquals(10, s.getHoraFin());
        }

        @Test
        void constructorInvalido_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Schedule("Lunes", 10, 8));
            assertThrows(IllegalArgumentException.class, () -> new Schedule("Martes", 5, 5));
        }

        @Test
        void seSolapaCon_mismoDia_horariosSeCruzan() {
            Schedule s1 = new Schedule("Lunes", 8, 10);
            Schedule s2 = new Schedule("Lunes", 9, 11);
            assertTrue(s1.seSolapaCon(s2));
            assertTrue(s2.seSolapaCon(s1));
        }

        @Test
        void seSolapaCon_mismoDia_horariosNoSeCruzan() {
            Schedule s1 = new Schedule("Lunes", 8, 10);
            Schedule s2 = new Schedule("Lunes", 10, 12); // justo al final
            assertFalse(s1.seSolapaCon(s2));
        }

        @Test
        void seSolapaCon_diasDistintos_noSeCruzan() {
            Schedule s1 = new Schedule("Lunes", 8, 10);
            Schedule s2 = new Schedule("Martes", 9, 11);
            assertFalse(s1.seSolapaCon(s2));
        }

        @Test
        void equals_y_hashCode() {
            Schedule s1 = new Schedule("Lunes", 8, 10);
            Schedule s2 = new Schedule("lunes", 8, 10); // mismo pero con diferente case
            Schedule s3 = new Schedule("Lunes", 9, 11);

            assertEquals(s1, s2);
            assertEquals(s1.hashCode(), s2.hashCode());
            assertNotEquals(s1, s3);
        }

        @Test
        void toString_devuelveTextoEsperado() {
            Schedule s = new Schedule("Miercoles", 14, 16);
            String texto = s.toString();
            assertTrue(texto.contains("Miercoles"));
            assertTrue(texto.contains("14"));
            assertTrue(texto.contains("16"));
        }
}