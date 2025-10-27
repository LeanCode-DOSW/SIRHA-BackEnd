package edu.dosw.sirha.sirha_backend;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.*;
import edu.dosw.sirha.sirha_backend.domain.model.Semaforo;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.dto.AcademicIndicatorsDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import io.jsonwebtoken.lang.Collections;

class SemaforoIndicatorsTest {

    private SubjectDecorator findDec(Semaforo sem, String name) {
        return sem.getSubjects().stream()
            .filter(d -> name.equals(d.getName()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Decorator not found: " + name));
    }

    private StudyPlan planWithSubjects(Subject... subjects) {
        StudyPlan plan = new StudyPlan( Careers.INGENIERIA_DE_SISTEMAS);
        for (Subject s : subjects) {
            plan.getSubjects().put(s.getName(), s);
        }
        return plan;
    }

    @Test
    void indicators_allPassed_shouldBeExcelente_and_VERDE() throws SirhaException {
        Subject s1 = new Subject("s1", "Mat1", 3);
        Subject s2 = new Subject("s2", "Mat2", 4);
        Subject s3 = new Subject("s3", "Mat3", 2);
        Subject s4 = new Subject("s4", "Mat4", 1);

        StudyPlan plan = planWithSubjects(s1, s2, s3, s4);
        Semaforo sem = new Semaforo(plan);

        // aprobar todas las materias
        for (SubjectDecorator d : sem.getSubjects()) {
            // crear grupo y asignar (necesario para inscribir)
            Group g = new Group(new Subject(d.getId(), d.getName(), d.getCredits()), 30,
                new AcademicPeriod("2025-1", LocalDate.now().minusDays(10), LocalDate.now().plusDays(10)));
            d.inscribir(g);
            d.aprobar();
        }

        AcademicIndicatorsDTO dto = sem.getAcademicIndicators();
        assertEquals(100.0, dto.getOverallProgressPercentage(), 1e-6);
        assertEquals(SemaforoColores.VERDE, dto.getGlobalProgressIndicator());
        assertEquals("Excelente", dto.getAcademicStatus());
        assertFalse(dto.isAcademicRisk());
        assertEquals(100.0, dto.getAcademicSuccessRate(), 1e-6);
        // completed credits should be 100% since all credits approved
        assertEquals(100.0, dto.getCreditsCompletionPercentage(), 1e-6);

        Map<SemaforoColores, Double> summary = dto.getTrafficLightSummary();
        assertEquals(100.0, summary.get(SemaforoColores.VERDE));
    }

    @Test
    void indicators_threePassed_oneFailed_shouldBeBueno_and_VERDE() throws SirhaException {
        Subject s1 = new Subject("s1", "Mat1", 3);
        Subject s2 = new Subject("s2", "Mat2", 4);
        Subject s3 = new Subject("s3", "Mat3", 2);
        Subject s4 = new Subject("s4", "Mat4", 1);

        StudyPlan plan = planWithSubjects(s1, s2, s3, s4);
        Semaforo sem = new Semaforo(plan);

        // aprobar 3 y reprobar 1
        SubjectDecorator d1 = findDec(sem, "Mat1");
        SubjectDecorator d2 = findDec(sem, "Mat2");
        SubjectDecorator d3 = findDec(sem, "Mat3");
        SubjectDecorator d4 = findDec(sem, "Mat4");

        Group g = new Group(s1, 30, new AcademicPeriod("2025-1", LocalDate.now(), LocalDate.now().plusMonths(4)));
        d1.inscribir(g); d1.aprobar();

        Group g2 = new Group(s2, 30, g.getCurrentPeriod());
        d2.inscribir(g2); d2.aprobar();

        Group g3 = new Group(s3, 30, g.getCurrentPeriod());
        d3.inscribir(g3); d3.aprobar();

        Group g4 = new Group(s4, 30, g.getCurrentPeriod());
        d4.inscribir(g4); d4.reprobar();

        AcademicIndicatorsDTO dto = sem.getAcademicIndicators();
        assertEquals(75.0, dto.getOverallProgressPercentage(), 1e-6);
        assertEquals(SemaforoColores.VERDE, dto.getGlobalProgressIndicator()); // >=75 -> VERDE
        assertEquals("Bueno", dto.getAcademicStatus()); // 75 >=70 -> Bueno
        assertFalse(dto.isAcademicRisk());
        assertEquals(75.0, dto.getAcademicSuccessRate(), 1e-6); // 3/(3+1)=75
    }

    @Test
    void indicators_twoPassed_twoFailed_shouldBeRegular_and_AMARILLO() throws SirhaException {
        Subject s1 = new Subject("s1", "MatA", 3);
        Subject s2 = new Subject("s2", "MatB", 4);
        Subject s3 = new Subject("s3", "MatC", 2);
        Subject s4 = new Subject("s4", "MatD", 1);

        StudyPlan plan = planWithSubjects(s1, s2, s3, s4);
        Semaforo sem = new Semaforo(plan);

        SubjectDecorator da = findDec(sem, "MatA");
        SubjectDecorator db = findDec(sem, "MatB");
        SubjectDecorator dc = findDec(sem, "MatC");
        SubjectDecorator dd = findDec(sem, "MatD");

        Group g = new Group(s1, 30, new AcademicPeriod("2025-1", LocalDate.now(), LocalDate.now().plusMonths(4)));
        da.inscribir(g); da.aprobar();
        db.inscribir(g); db.aprobar();

        dc.inscribir(g); dc.reprobar();
        dd.inscribir(g); dd.reprobar();

        AcademicIndicatorsDTO dto = sem.getAcademicIndicators();
        assertEquals(50.0, dto.getOverallProgressPercentage(), 1e-6);
        assertEquals(SemaforoColores.AMARILLO, dto.getGlobalProgressIndicator()); // >=50 -> AMARILLO
        assertEquals("Regular", dto.getAcademicStatus()); // >=50 -> Regular
        assertFalse(dto.isAcademicRisk());
        assertEquals(50.0, dto.getAcademicSuccessRate(), 1e-6); // 2/(2+2)=50
    }

    @Test
    void indicators_lowProgress_shouldBeGRIS_and_NecesitaMejorar() throws SirhaException {
        Subject s1 = new Subject("s1", "X1", 3);
        Subject s2 = new Subject("s2", "X2", 4);
        Subject s3 = new Subject("s3", "X3", 2);
        Subject s4 = new Subject("s4", "X4", 1);

        StudyPlan plan = planWithSubjects(s1, s2, s3, s4);
        Semaforo sem = new Semaforo(plan);

        AcademicIndicatorsDTO dto = sem.getAcademicIndicators();
        assertEquals(0.0, dto.getOverallProgressPercentage());
        assertEquals(SemaforoColores.GRIS, dto.getGlobalProgressIndicator());
        assertEquals("Necesita Mejorar", dto.getAcademicStatus());
        assertFalse(dto.isAcademicRisk());
        Map<SemaforoColores, Double> summary = dto.getTrafficLightSummary();
        if (!summary.isEmpty()) {
            assertEquals(100.0, summary.get(SemaforoColores.GRIS));
            assertEquals(0.0 , summary.getOrDefault(SemaforoColores.VERDE, 0.0));
        }
    }

    private StudyPlan planWithSubjectsAndCredits(int totalCredits, Subject... subjects) {
        return new StudyPlan(Careers.INGENIERIA_DE_SISTEMAS) {
            @Override public Map<String, Subject> getSubjects() {
                Map<String, Subject> m = new LinkedHashMap<>();
                for (Subject s : subjects) m.put(s.getName(), s);
                return m;
            }
            @Override public int calculateTotalCredits() {
                return totalCredits;
            }
        };
    }

    @Test
    void emptyStudyPlan_and_zeroTotalCredits_behaviour() throws SirhaException {
        StudyPlan emptyPlan = new StudyPlan(Careers.INGENIERIA_DE_SISTEMAS) {
            @Override public Map<String, Subject> getSubjects() { return Collections.emptyMap(); }
            @Override public int calculateTotalCredits() { return 0; }
        };

        Semaforo sem = new Semaforo(emptyPlan);

        assertEquals(0, sem.getTotalSubjectsCount());
        assertTrue(sem.getPercentageByColor().isEmpty());
        assertEquals(0.0, sem.getOverallProgressPercentage());
        assertEquals(0.0, sem.getAcademicSuccessRate());
        assertEquals(0.0, sem.getCompletedCreditsPercentage());
        var pensum = sem.getAcademicPensum();
        assertTrue(pensum.isEmpty());
        var schedules = sem.getAllSchedules();
        assertTrue(schedules.isEmpty());
        AcademicIndicatorsDTO dto = sem.getAcademicIndicators();
        assertNotNull(dto);
    }

    @Test
    void completedCredits_percentage_when_planCredits_zero_returnsZero() throws SirhaException {
        Subject s = new Subject("id1","MateriaLong",3);
        // plan reports 0 totalCredits
        StudyPlan plan = planWithSubjectsAndCredits(0, s);
        Semaforo sem = new Semaforo(plan);

        SubjectDecorator d = findDec(sem, s.getName());
        Group g = new Group(s, 30, new AcademicPeriod("2025-1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(10)));
        d.inscribir(g);
        d.aprobar();

        // since plan total credits == 0, completed credits percentage must be 0.0 (branch)
        assertEquals(0.0, sem.getCompletedCreditsPercentage());
    }

    @Test
    void enroll_subject_success_and_group_not_found_exception() throws Exception {
        Subject s = new Subject("id2","MateriaEnroll",4);
        StudyPlan plan = planWithSubjectsAndCredits(100, s);
        Semaforo sem = new Semaforo(plan);

        SubjectDecorator dec = findDec(sem, s.getName());
        Group group = new Group(s, 20, new AcademicPeriod("2025-1", LocalDate.now(), LocalDate.now().plusMonths(4)));

        // enroll with Subject instance that reports it DOES belong to the group
        // usar la misma instancia 's' que se usó para crear el Group, así subject.hasGroup(group) devuelve true
        Subject subjThatHasGroup = s;
 
         sem.enrollSubjectInGroup(subjThatHasGroup, group);
         assertEquals(group, dec.getGroup());
         assertEquals(SemaforoColores.AMARILLO, dec.getEstadoColor());

        // enroll with Subject that DOES NOT have the group => throws GROUP_NOT_FOUND
        Subject subjNoGroup = new Subject(s.getId(), s.getName(), s.getCredits()) {
            @Override public boolean hasGroup(Group g) { return false; }
        };
        assertThrows(SirhaException.class, () -> sem.enrollSubjectInGroup(subjNoGroup, group));
    }

    @Test
    void verifyChangeGroup_all_exception_paths_and_success() throws Exception {
        Subject s = new Subject("id3","MateriaChange",3);
        StudyPlan plan = planWithSubjectsAndCredits(100, s);
        Semaforo sem = new Semaforo(plan);

        SubjectDecorator dec = findDec(sem, s.getName());
        Group g1 = new Group(s, 10, new AcademicPeriod("2025-1", LocalDate.now(), LocalDate.now().plusMonths(3)));
        dec.inscribir(g1);

        assertTrue(sem.isSubjectCursando(s.getName()));

        Subject subjHasSame = new Subject(s.getId(), s.getName(), s.getCredits());
        assertThrows(SirhaException.class, () -> sem.verifyChangeGroup(subjHasSame, g1));

        // GROUP_NOT_FOUND: decorator group different but subject.hasGroup false
        Group other = new Group(s, 15, g1.getCurrentPeriod());
        Subject subjNoGroup = new Subject(s.getId(), s.getName(), s.getCredits());
        // make decorator's group something else so equals fails
        dec.setGroup(g1);
        assertThrows(SirhaException.class, () -> sem.verifyChangeGroup(subjNoGroup, other));

        // SUBJECT_NOT_FOUND: subject not in semaforo map
        Subject notInPlan = new Subject("no","NoPlan",1);
        assertThrows(SirhaException.class, () -> sem.verifyChangeGroup(notInPlan, other));

        // success case: subject.hasGroup true and different group
        Subject subjHasOther = new Subject(s.getId(), s.getName(), s.getCredits()) {
            @Override public boolean hasGroup(Group g) { return java.util.Objects.equals(g, other); }
        };
        // set decorator current group different from 'other'
        dec.setGroup(g1);
        assertTrue(sem.verifyChangeGroup(subjHasOther, other));
    }

    @Test
    void pensum_and_schedules_and_color_counts_and_semesters() throws SirhaException {
        Subject a = new Subject("A","MatAAA",3);
        Subject b = new Subject("B","MatBBB",4);
        Subject c = new Subject("C","MatCCC",2);

        StudyPlan plan = planWithSubjectsAndCredits(20, a, b, c);
        Semaforo sem = new Semaforo(plan);

        SubjectDecorator da = findDec(sem, "MatAAA");
        SubjectDecorator db = findDec(sem, "MatBBB");

        AcademicPeriod p = new AcademicPeriod("2025-1", LocalDate.now().minusDays(5), LocalDate.now().plusDays(20));
        Group ga = new Group(a, 30, p); ga.setId("g-a");
        Group gb = new Group(b, 25, p); gb.setId("g-b");

        Schedule sch = new Schedule(null, LocalTime.of(9,0), LocalTime.of(10,0)); // start < end
        ga.addSchedule(sch);

        // set states and semesters (set semester BEFORE approving)
        da.inscribir(ga);
        da.setSemester(1);
        da.aprobar();
        db.inscribir(gb); /* in progress */

        var pensum = sem.getAcademicPensum();
        assertTrue(pensum.containsKey(SemaforoColores.VERDE));
        assertTrue(pensum.containsKey(SemaforoColores.AMARILLO) || pensum.containsKey(SemaforoColores.GRIS));

        var schedules = sem.getAllSchedules();
        assertTrue(schedules.containsKey(p));
        assertEquals(ga.getSchedules(), schedules.get(p));

        assertEquals(1, sem.getSubjectsBySemester(1).size());

        assertEquals(1, sem.getSubjectsByColorCount(SemaforoColores.VERDE));
        assertEquals(3, sem.getCreditsByColor(SemaforoColores.VERDE));
    }
}