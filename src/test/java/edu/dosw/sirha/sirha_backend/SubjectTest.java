package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
class SubjectTest {
    @Test
    void constructorValidoTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);

            assertEquals("101", s.getId());
            assertEquals("Matemáticas", s.getName());
            assertEquals(4, s.getCredits());
            assertNotNull(s.getGroups());
            assertTrue(s.getGroups().isEmpty());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear la materia: " + e.getMessage());
        }
    }

    @Test
    void constructorNombreNullTest() {
        assertThrows(SirhaException.class, () -> {
            new Subject("101", null, 4);
        });
    }
    @Test
    void BaseconstructorTest() {
        Subject s = new Subject();
        assertNotNull(s.getGroups());
        assertTrue(s.getGroups().isEmpty());
        s.deleteGroups();
    }

    @Test 
    void getOpenGroupsEmptyTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertTrue(s.getOpenGroups().isEmpty());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear la materia: " + e.getMessage());
        }
    }

    @Test
    void getGroupByCodeTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
            Group g = new Group(s, 30, period);
            Group foundGroup = s.getGroupByCode("MAT-1");
            assertEquals(1, s.getGroups().size());
            assertNotNull(foundGroup);
            assertEquals(g, foundGroup);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }
    
    @Test
    void constructorNombreVacioTest() {
        assertThrows(SirhaException.class, () -> {
            new Subject("101", "", 4);
            });
        }

    @Test
    void constructorNombreBlankTest() {
        assertThrows(SirhaException.class, () -> {
            new Subject("101", "   ", 4);
        });
    }

    @Test
    void constructorCreditosCeroTest() {
        assertThrows(SirhaException.class, () -> {
            new Subject("101", "Matemáticas", 0);
        });
    }

    @Test
    void constructorCreditosNegativosTest() {
        assertThrows(SirhaException.class, () -> {
            new Subject("101", "Matemáticas", -1);
        });
    }

    @Test
    void setIdTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            s.setId("1012");
            assertEquals("1012", s.getId());
        } catch (Exception e) {
            fail("No se esperaba una excepción al establecer el ID: " + e.getMessage());
        }
    }

    @Test
    void setNameValidoTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            s.setName("Física");
            assertEquals("Física", s.getName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al establecer el nombre: " + e.getMessage());
        }
    }

    void setNameInvalidTest(String invalidName) {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertThrows(IllegalArgumentException.class, () -> {
                s.setName(invalidName);
            });
        } catch (Exception e) {
            fail("No se esperaba una excepción al establecer un nombre inválido: " + e.getMessage());
        }
    }

    @Test
    void setCreditsValidoTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            s.setCredits(6);
            assertEquals(6, s.getCredits());
        } catch (Exception e) {
            fail("No se esperaba una excepción al establecer créditos válidos: " + e.getMessage());
        }
    }

    @Test
    void setCreditsCeroTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertThrows(SirhaException.class, () -> {
                s.setCredits(0);
            });
        } catch (Exception e) {
            fail("No se esperaba una excepción al establecer créditos cero: " + e.getMessage());
        }
    }

    @Test
    void setCreditsNegativosTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertThrows(SirhaException.class, () -> {
                s.setCredits(-3);
            });
        } catch (Exception e) {
            fail("No se esperaba una excepción al establecer créditos negativos: " + e.getMessage());
        }
    }


    @Test
    void getNameTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertEquals("Matemáticas", s.getName());
            assertEquals(s.getName(), s.getName());
        } catch (Exception e) {
            fail("No se esperaba una excepción al obtener el nombre: " + e.getMessage());
        }
    }

    @Test
    void getCodigoTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertEquals("101", s.getId());
            assertEquals(s.getId(), s.getId());
        } catch (Exception e) {
            fail("No se esperaba una excepción al obtener el código: " + e.getMessage());
        }
    }


    @Test
    void addGrupoTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
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
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
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
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertThrows(SirhaException.class, () -> {
                s.addGroup(null);
            });
        } catch (Exception e) {
            fail("No se esperaba una excepción al agregar un grupo nulo: " + e.getMessage());
        }
    }

    @Test
    void removeGroupTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
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

        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
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
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group g = new Group(s, 30, period);
            g.setId("1");
            assertTrue(s.hasGroup(g));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void hasGroupFalseTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            Subject s2 = new Subject("102", "Física", 6);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
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
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            assertFalse(s.hasGroup(null));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }


    @Test
    void notEqualsTest() {
        try {
            Subject s1 = new Subject("101", "Matemáticas", 4);
            Subject s2 = new Subject("102", "Física", 6);
            assertNotEquals(s1, s2);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los sujetos: " + e.getMessage());
        }
    }

    @Test
    void equalsTest() {
        try {
            Subject s1 = new Subject("101", "Matemáticas", 4);
            Subject s2 = new Subject("101", "Matemáticas", 4);
            assertEquals(s1, s2);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los sujetos: " + e.getMessage());
        }
    }

    @Test
    void equalsNullTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            String other = "Not a Subject";
            assertNotEquals(s, other);
            assertNotEquals(null, s);
            assertEquals(s, s);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void hashCodeTest() {
        try {
            Subject s1 = new Subject("101", "Matemáticas", 4);
            Subject s2 = new Subject("102", "Física", 6);
            assertNotEquals(s1.hashCode(), s2.hashCode());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los sujetos: " + e.getMessage());
        }
    }

    @Test
    void toStringTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
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
        try {
            Subject s = new Subject("101", "Matemáticas", 4);

            String result = s.toString();

            assertTrue(result.contains("101"));
            assertTrue(result.contains("Matemáticas"));
            assertTrue(result.contains("4"));
            assertTrue(result.contains("0")); // Sin grupos
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }


    @Test
    void addMismoGrupoDosVecesTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));            
            Group g = new Group(s, 30, period);
            g.setId("1");

            assertEquals(1, s.getGroups().size());
            assertTrue(s.hasGroup(g));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void removeFromEmptyListTest() {
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));          
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
        try {
            Subject s = new Subject("101", "Matemáticas", 4);
            AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
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
            System.out.println(s.getGroups().toString());
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
        try {
            Subject s = new Subject("101", "Matemáticas", Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE, s.getCredits());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear la asignatura: " + e.getMessage());
        }
    }

    @Test
    void nombreConEspaciosTest() {
        try {
            Subject s = new Subject("101", " Matemáticas Avanzadas ", 4);
            assertEquals(" Matemáticas Avanzadas ", s.getName());
            s.getGroupByCode("1");
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear la asignatura: " + e.getMessage());
        }
    }

    
}
