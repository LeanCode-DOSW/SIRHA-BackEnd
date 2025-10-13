package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

@SpringBootTest
class ScheduleTest {
        @Test
        void constructorValido_creaObjeto() {
            Schedule s = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            assertEquals(DiasSemana.LUNES, s.getDia());
            assertEquals(LocalTime.of(8, 0), s.getHoraInicio());
            assertEquals(LocalTime.of(10, 0), s.getHoraFin());
        }

        @Test
        void constructorInvalido_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, this::crearHorarioInvalido1);
            assertThrows(IllegalArgumentException.class, this::crearHorarioInvalido2);
        }

        private void crearHorarioInvalido1() {
            new Schedule(DiasSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(8, 0));
        }

        private void crearHorarioInvalido2() {
            new Schedule(DiasSemana.MARTES, LocalTime.of(5, 0), LocalTime.of(5, 0));
        }

        @Test
        void seSolapaCon_mismoDia_horariosSeCruzan() {
            Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule s2 = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
            assertTrue(s1.seSolapaCon(s2));
            assertTrue(s2.seSolapaCon(s1));
        }

        @Test
        void seSolapaCon_mismoDia_horariosNoSeCruzan() {
            Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule s2 = new Schedule(DiasSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(12, 0)); // justo al final
            assertFalse(s1.seSolapaCon(s2));
        }

        @Test
        void seSolapaCon_diasDistintos_noSeCruzan() {
            Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            Schedule s2 = new Schedule(DiasSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(11, 0));
            assertFalse(s1.seSolapaCon(s2));
        }

        @Test
        void toString_devuelveTextoEsperado() {
            Schedule s = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0));
            String texto = s.toString();
            System.out.println(texto);
            assertTrue(texto.contains("MIERCOLES"));
            assertTrue(texto.contains("14"));
            assertTrue(texto.contains("16"));
        }
}