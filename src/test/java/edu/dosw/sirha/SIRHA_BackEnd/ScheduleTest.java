package edu.dosw.sirha.SIRHA_BackEnd;

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
                assertEquals(DiasSemana.LUNES, s.getDia());
                assertEquals(LocalTime.of(8, 0), s.getHoraInicio());
                assertEquals(LocalTime.of(10, 0), s.getHoraFin());
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
                assertTrue(s1.seSolapaCon(s2));
                assertTrue(s2.seSolapaCon(s1));
            } catch (Exception e) {
                fail("No se esperaba una excepción al crear horarios válidos: " + e.getMessage());
            }
        }

        @Test
        void seSolapaCon_mismoDia_horariosNoSeCruzan() {
            try {
                Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
                Schedule s2 = new Schedule(DiasSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(12, 0)); // justo al final
                assertFalse(s1.seSolapaCon(s2));
            } catch (Exception e) {
                fail("No se esperaba una excepción al crear horarios válidos: " + e.getMessage());
            }
        }

        @Test
        void seSolapaCon_diasDistintos_noSeCruzan() {
            try {
                Schedule s1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
                Schedule s2 = new Schedule(DiasSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(11, 0));
                assertFalse(s1.seSolapaCon(s2));
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
}