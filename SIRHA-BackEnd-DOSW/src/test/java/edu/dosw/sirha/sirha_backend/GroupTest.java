package edu.dosw.sirha.sirha_backend;

import edu.dosw.sirha.sirha_backend.domain.Group;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    @Test
    void testConstructorAndGetters() {
        Group group = new Group(1L, "Math", "Dr. Smith", 30, "Lun 8-10", 25);

        assertEquals(1L, group.getId());
        assertEquals("Math", group.getSubject());
        assertEquals("Dr. Smith", group.getProfessor());
        assertEquals(30, group.getCapacity());
        assertEquals("Lun 8-10", group.getSchedule());
        assertEquals(25, group.getEnrolled());
    }

    @Test
    void testSetters() {
        Group group = new Group();
        group.setId(2L);
        group.setSubject("Physics");
        group.setProfessor("Dr. Brown");
        group.setCapacity(40);
        group.setSchedule("Mar 10-12");
        group.setEnrolled(10);

        assertEquals(2L, group.getId());
        assertEquals("Physics", group.getSubject());
        assertEquals("Dr. Brown", group.getProfessor());
        assertEquals(40, group.getCapacity());
        assertEquals("Mar 10-12", group.getSchedule());
        assertEquals(10, group.getEnrolled());
    }
}

