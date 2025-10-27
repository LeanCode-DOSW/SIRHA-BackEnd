package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;

class AcademicPeriodTest {

    @Test
    void isActive_withinPeriod_returnsTrue_and_isActivoTrue() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("2025-1", today.minusDays(1), today.plusDays(1));

        assertTrue(ap.isActivo(), "isActivo debe ser true cuando hoy está entre startDate y endDate");
        assertTrue(ap.isActive(), "isActive debe ser true cuando isActivo y hoy entre start/end");
        assertFalse(ap.isPeriodInscripcionesAbiertas());
    }

    @Test
    void isActive_beforeStartAnd_afterEnd_returnFalse() {
        LocalDate today = LocalDate.now();

        AcademicPeriod beforeStart = new AcademicPeriod("future", today.plusDays(1), today.plusDays(10));
        assertFalse(beforeStart.isActivo());
        assertFalse(beforeStart.isActive());

        AcademicPeriod afterEnd = new AcademicPeriod("past", today.minusDays(10), today.minusDays(1));
        assertFalse(afterEnd.isActivo());
        assertFalse(afterEnd.isActive());
    }

    @Test
    void periodInscripciones_nullDates_or_outsideWindow_returnFalse() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("2025-1", today.minusDays(5), today.plusDays(5));

        assertFalse(ap.isPeriodInscripcionesAbiertas());

        ap.setStartDatesInscripciones(today.plusDays(1), today.plusDays(2));
        assertFalse(ap.isPeriodInscripcionesAbiertas());
    }

    @Test
    void periodInscripciones_withinWindow_and_boundaries_returnTrue() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("2025-1", today.minusDays(5), today.plusDays(5));

        ap.setStartDatesInscripciones(today.minusDays(1), today.plusDays(1));
        assertTrue(ap.isPeriodInscripcionesAbiertas());

        ap.setStartDatesInscripciones(today, today);
        assertTrue(ap.isPeriodInscripcionesAbiertas());
    }

    @Test
    void id_setter_and_getter_and_toString_containsValues() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("P", today.minusDays(1), today.plusDays(1));
        ap.setId("abc-123");

        assertEquals("abc-123", ap.getId());
        String s = ap.toString();
        assertNotNull(s);
        assertTrue(s.contains("abc-123"));
        assertTrue(s.contains("P"));
    }

    @Test
    void equals_and_hashCode_consistency() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 6, 30);

        AcademicPeriod a1 = new AcademicPeriod("2025-1", start, end);
        AcademicPeriod a2 = new AcademicPeriod("2025-1", start, end);
        AcademicPeriod a3 = new AcademicPeriod("2025-2", start, end);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotEquals(a1, a3);
    }

    @Test
    void setStartDatesInscripciones_assignsValues_accessorsWork() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("X", today.minusDays(1), today.plusDays(1));

        LocalDate s = today.minusDays(2);
        LocalDate e = today.plusDays(2);
        ap.setStartDatesInscripciones(s, e);

        assertEquals(s, ap.getStartDateInscripciones());
        assertEquals(e, ap.getEndDateInscripciones());
    }


    @Test
    void periodInscripciones_false_if_period_not_active_even_if_inscripcion_dates_include_today() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("FUT", today.plusDays(5), today.plusDays(10));
        ap.setStartDatesInscripciones(today.minusDays(1), today.plusDays(1));
        assertFalse(ap.isActivo());
        assertFalse(ap.isPeriodInscripcionesAbiertas(), "Si el periodo no está activo, inscripciones no deben considerarse abiertas");
    }

    @Test
    void equals_returnsFalse_for_null_and_other_types_and_true_for_same_instance() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("E", today.minusDays(1), today.plusDays(1));
        assertNotEquals(null, ap);
        assertNotEquals("some" , ap);
        assertEquals(ap, ap);
    }

    @Test
    void single_day_period_isActive_true_and_toString_contains_activo_flag() {
        LocalDate today = LocalDate.now();
        AcademicPeriod ap = new AcademicPeriod("ONE", today, today);
        assertTrue(ap.isActivo());
        assertTrue(ap.isActive());
        String s = ap.toString();
        assertTrue(s.contains("activo="));
    }
}