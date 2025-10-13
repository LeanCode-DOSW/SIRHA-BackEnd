package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
class SubjectTest {
    @Test
    void constructorValidoTest() {
        Subject s = new Subject("101", "Matemáticas", 4);

        assertEquals("101", s.getId());
        assertEquals("Matemáticas", s.getName());
        assertEquals(4, s.getCredits());
        assertNotNull(s.getGroups());
        assertTrue(s.getGroups().isEmpty());
    }

    @Test
    void constructorNombreNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Subject("101", null, 4);
        });
    }

    @Test
    void constructorNombreVacioTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Subject("101", "", 4);
            });
        }

    @Test
    void constructorNombreBlankTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Subject("101", "   ", 4);
        });
    }

    @Test
    void constructorCreditosCeroTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Subject("101", "Matemáticas", 0);
        });
    }

    @Test
    void constructorCreditosNegativosTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Subject("101", "Matemáticas", -1);
        });
    }

    @Test
    void setIdTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        s.setId("1012");
        assertEquals("1012", s.getId());
    }

    @Test
    void setNameValidoTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        s.setName("Física");
        assertEquals("Física", s.getName());
    }

    void setNameInvalidTest(String invalidName) {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertThrows(IllegalArgumentException.class, () -> {
            s.setName(invalidName);
        });
    }

    @Test
    void setCreditsValidoTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        s.setCredits(6);
        assertEquals(6, s.getCredits());
    }

    @Test
    void setCreditsCeroTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertThrows(IllegalArgumentException.class, () -> {
            s.setCredits(0);
        });
    }

    @Test
    void setCreditsNegativosTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertThrows(IllegalArgumentException.class, () -> {
            s.setCredits(-3);
        });
    }


    @Test
    void getNameTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertEquals("Matemáticas", s.getName());
        assertEquals(s.getName(), s.getName());
    }

    @Test
    void getCodigoTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertEquals("101", s.getId());
        assertEquals(s.getId(), s.getId());
    }


    @Test
    void addGrupoTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g = new Group(s, 30, period);
            g.setId("1");
            assertTrue(s.hasGroup(g));
            assertEquals(1, s.getGroups().size());
            assertTrue(s.getGroups().contains(g));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
        
    }

    @Test
    void addMultiplesGruposTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g1 = new Group(s, 30, period);
            Group g2 = new Group(s, 25, period);
            Group g3 = new Group(s, 20, period);

            g1.setId("1");
            g2.setId("2");
            g3.setId("3");

            assertEquals(3, s.getGroups().size());
            assertTrue(s.hasGroup(g1));
            assertTrue(s.hasGroup(g2));
            assertTrue(s.hasGroup(g3));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }

        
    }

    @Test
    void addGrupoNullTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertThrows(IllegalArgumentException.class, () -> {
            s.addGroup(null);
        });
    }

    @Test
    void removeGroupTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g1 = new Group(s, 30, period);
            Group g2 = new Group(s, 25, period);

            g1.setId("1");
            g2.setId("2");

            boolean removed = s.removeGroup(g2);

            assertTrue(removed);
            assertFalse(s.hasGroup(g2));
            assertTrue(s.hasGroup(g1));
            assertEquals(1, s.getGroups().size());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }

    @Test
    void removeGrupoNoExistenteTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g1 = new Group(s, 30, period);
            Group g2 = new Group(s, 25, period);

            g1.setId("1");
            g2.setId("2");

            assertTrue(s.removeGroup(g2));
            assertTrue(s.hasGroup(g1));
            assertEquals(1, s.getGroups().size());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }

    @Test
    void hasGroupTrueTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g = new Group(s, 30, period);
            g.setId("1");
            assertTrue(s.hasGroup(g));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void hasGroupFalseTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        Subject s2 = new Subject("102", "Física", 6);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g1 = new Group(s, 30, period);
            Group g2 = new Group(s2, 25, period);
            g1.setId("1");
            g2.setId("2");
            assertFalse(s.hasGroup(g2));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }

    @Test
    void hasGroupNullTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertFalse(s.hasGroup(null));
    }


    @Test
    void equalsTest() {
        Subject s1 = new Subject("101", "Matemáticas", 4);
        Subject s2 = new Subject("101", "Física", 6);

        assertEquals(s1, s2);
    }

    @Test
    void notEqualsTest() {
        Subject s1 = new Subject("101", "Matemáticas", 4);
        Subject s2 = new Subject("102", "Matemáticas", 4); // Diferentes IDs
        assertNotEquals(s1, s2);
    }

    @Test
    void equalsNullTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertNotEquals(null, s);
    }

    @Test
    void equalsOtherClassTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        String other = "Not a Subject";
        assertNotEquals(s, other);
    }

    @Test
    void equalsSameInstanceTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        assertEquals(s, s);
    }

    @Test
    void hashCodeTest() {
        Subject s1 = new Subject("101", "Matemáticas", 4);
        Subject s2 = new Subject("102", "Física", 6);
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void toStringTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g1 = new Group(s, 30, period);
            Group g2 = new Group(s, 25, period);

            g1.setId("1");
            g2.setId("2");

            String result = s.toString();

            assertTrue(result.contains("101"));
            assertTrue(result.contains("Matemáticas"));
            assertTrue(result.contains("4"));
            assertTrue(result.contains("2")); // Número de grupos
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }

    @Test
    void toStringWithoutGroupsTest() {
        Subject s = new Subject("101", "Matemáticas", 4);

        String result = s.toString();

        assertTrue(result.contains("101"));
        assertTrue(result.contains("Matemáticas"));
        assertTrue(result.contains("4"));
        assertTrue(result.contains("0")); // Sin grupos
    }


    @Test
    void addMismoGrupoDosVecesTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g = new Group(s, 30, period);
            g.setId("1");

            s.addGroup(g); // Añadir el mismo grupo otra vez

            assertEquals(2, s.getGroups().size());
            assertTrue(s.hasGroup(g));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void removeFromEmptyListTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g = new Group(s, 30, period);
            g.setId("1");
            boolean removed = s.removeGroup(g);
            assertTrue(removed);
            assertEquals(0, s.getGroups().size());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void addRemoveMultipleOperationsTest() {
        Subject s = new Subject("101", "Matemáticas", 4);
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            Group g1 = new Group(s, 30, period);
            Group g2 = new Group(s, 25, period);
            Group g3 = new Group(s, 20, period);

            g1.setId("1");
            g2.setId("2");
            g3.setId("3");

            assertEquals(3, s.getGroups().size());

            s.removeGroup(g2);
            assertEquals(2, s.getGroups().size());
            assertFalse(s.hasGroup(g2));

            Group g4 = new Group(s, 15, period);
            g4.setId("4");
            assertEquals(3, s.getGroups().size());
            assertTrue(s.hasGroup(g4));

            s.removeGroup(g1);
            s.removeGroup(g3);
            s.removeGroup(g4);
            assertEquals(0, s.getGroups().size());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }

    @Test
        void creditosMaxValueTest() {
        Subject s = new Subject("101", "Matemáticas", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, s.getCredits());
    }

    @Test
    void nombreConEspaciosTest() {
        Subject s = new Subject("101", " Matemáticas Avanzadas ", 4);
        assertEquals(" Matemáticas Avanzadas ", s.getName());
        s.getGroupByCode("1");
    }

}
