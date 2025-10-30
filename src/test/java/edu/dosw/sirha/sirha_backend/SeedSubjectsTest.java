package edu.dosw.sirha.sirha_backend;

 
import org.junit.jupiter.api.Assertions;
 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.SubjectService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SeedSubjectsTest {

    @Autowired
    SubjectService subjectService;
    @Autowired
    edu.dosw.sirha.sirha_backend.service.GroupService groupService;

    // names used for the temporary subjects
    private final List<String> createdNames = new ArrayList<>();

    // Intentionally leave created data in the database for manual inspection by the developer.
    // No cleanup is performed.

    @Test
    void createThreeSubjectsWithThreeGroupsEach() throws SirhaException {
        // create 3 subjects, each with 3 groups
        for (int i = 1; i <= 3; i++) {
            String name = "TEST_SUBJECT_" + i;
            String id = "TS" + i;
            // delete if exists
            try {
                if (subjectService.existsByName(name)) {
                    subjectService.deleteByName(name);
                }
            } catch (Exception e) {
                // ignore
            }

            Subject s = new Subject(id, name, 3);
            AcademicPeriod period = new AcademicPeriod("2025-2", LocalDate.now(), LocalDate.now().plusMonths(4));

            // add 3 groups (keep references to avoid unused-variable issues)
            Group g1 = new Group(s, 30, period);
            g1.setId("g" + i + "-1");
            Group g2 = new Group(s, 25, period);
            g2.setId("g" + i + "-2");
            Group g3 = new Group(s, 20, period);
            g3.setId("g" + i + "-3");
            // save subject first
            Subject saved = subjectService.save(s);
            Assertions.assertNotNull(saved, "Saved subject should not be null");


            // verify groups persisted
            List<?> groups = subjectService.getGroupsOfSubject(name);
            Assertions.assertNotNull(groups, "Groups list should not be null");
            Assertions.assertEquals(3, groups.size(), "Subject should have 3 groups");

            // record created subject name (no cleanup will be done)
            createdNames.add(name);
        }
    }
}
