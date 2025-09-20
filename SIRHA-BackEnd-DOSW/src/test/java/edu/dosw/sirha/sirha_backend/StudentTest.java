package edu.dosw.sirha.sirha_backend;


import edu.dosw.sirha.sirha_backend.domain.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class StudentTest {

    @Test
    void testConstructorAndGetters() {
        Student s = new Student(1L, "Laura", "Engineering", 5, Arrays.asList("Math", "Physics"));

        assertEquals(1L, s.getId());
        assertEquals("Laura", s.getName());
        assertEquals("Engineering", s.getCareer());
        assertEquals(5, s.getSemester());
        assertEquals(2, s.getSubjects().size());
    }

    @Test
    void testSetters() {
        Student s = new Student();
        s.setId(2L);
        s.setName("Carlos");
        s.setCareer("Medicine");
        s.setSemester(3);
        s.setSubjects(Arrays.asList("Biology"));

        assertEquals(2L, s.getId());
        assertEquals("Carlos", s.getName());
        assertEquals("Medicine", s.getCareer());
        assertEquals(3, s.getSemester());
        assertEquals("Biology", s.getSubjects().get(0));
    }

    @Test
    void testEqualsAndHashCode() {
        Student s1 = new Student(1L, "Ana", "Law", 4, Arrays.asList("Civil Law"));
        Student s2 = new Student(1L, "Ana María", "Law", 6, Arrays.asList("Penal Law"));
        Student s3 = new Student(2L, "Luis", "Economics", 2, Arrays.asList("Microeconomics"));

        assertEquals(s1, s2);  // mismos ids
        assertNotEquals(s1, s3); // diferente id
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}
