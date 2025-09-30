package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SubjectTest {
        @Test
        public void constructorValidoTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertEquals(101, s.getId());
            assertEquals("Matemáticas", s.getName());
            assertEquals(4, s.getCreditos());
            assertNotNull(s.getGroups());
            assertTrue(s.getGroups().isEmpty());
        }

        @Test
        public void constructorNombreNullTest() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Subject(101, null, 4);
            });
        }

        @Test
        public void constructorNombreVacioTest() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Subject(101, "", 4);
            });
        }

        @Test
        public void constructorNombreBlankTest() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Subject(101, "   ", 4);
            });
        }

        @Test
        public void constructorCreditosCeroTest() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Subject(101, "Matemáticas", 0);
            });
        }

        @Test
        public void constructorCreditosNegativosTest() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Subject(101, "Matemáticas", -1);
            });
        }

        // ==================== GETTERS Y SETTERS TESTS ====================

        @Test
        public void setIdTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            s.setId(1012);
            assertEquals(1012, s.getId());
        }

        @Test
        public void setNameValidoTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            s.setName("Física");
            assertEquals("Física", s.getName());
        }

        @Test
        public void setNameNullTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertThrows(IllegalArgumentException.class, () -> {
                s.setName(null);
            });
        }

        @Test
        public void setNameVacioTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertThrows(IllegalArgumentException.class, () -> {
                s.setName("");
            });
        }

        @Test
        public void setNameBlankTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertThrows(IllegalArgumentException.class, () -> {
                s.setName("   ");
            });
        }

        @Test
        public void setCreditosValidoTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            s.setCreditos(6);
            assertEquals(6, s.getCreditos());
        }

        @Test
        public void setCreditosCeroTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertThrows(IllegalArgumentException.class, () -> {
                s.setCreditos(0);
            });
        }

        @Test
        public void setCreditosNegativosTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertThrows(IllegalArgumentException.class, () -> {
                s.setCreditos(-3);
            });
        }

        // ==================== MÉTODOS ADICIONALES (ALIAS) ====================

        @Test
        public void getNombreTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            assertEquals("Matemáticas", s.getNombre());
            assertEquals(s.getName(), s.getNombre());
        }

        @Test
        public void getCodigoTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            assertEquals(101, s.getId());
            assertEquals(s.getId(), s.getId());
        }


        @Test
        public void addGrupoTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g = new Group(30);
            g.setId(1);

            s.addGrupo(g);

            assertTrue(s.isHasGroup(g));
            assertEquals(1, s.getGroups().size());
            assertTrue(s.getGroups().contains(g));
        }

        @Test
        public void addMultiplesGruposTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g1 = new Group(30);
            Group g2 = new Group(25);
            Group g3 = new Group(20);

            g1.setId(1);
            g2.setId(2);
            g3.setId(3);

            s.addGrupo(g1);
            s.addGrupo(g2);
            s.addGrupo(g3);

            assertEquals(3, s.getGroups().size());
            assertTrue(s.isHasGroup(g1));
            assertTrue(s.isHasGroup(g2));
            assertTrue(s.isHasGroup(g3));
        }

        @Test
        public void addGrupoNullTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertThrows(IllegalArgumentException.class, () -> {
                s.addGrupo(null);
            });
        }

        @Test
        public void removeGrupoTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g1 = new Group(30);
            Group g2 = new Group(25);

            g1.setId(1);
            g2.setId(2);

            s.addGrupo(g1);
            s.addGrupo(g2);

            boolean removed = s.removeGrupo(g2);

            assertTrue(removed);
            assertFalse(s.isHasGroup(g2));
            assertTrue(s.isHasGroup(g1));
            assertEquals(1, s.getGroups().size());
        }

        @Test
        public void removeGrupoNoExistenteTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g1 = new Group(30);
            Group g2 = new Group(25);

            g1.setId(1);
            g2.setId(2);

            s.addGrupo(g1);

            boolean removed = s.removeGrupo(g2);

            assertFalse(removed);
            assertTrue(s.isHasGroup(g1));
            assertEquals(1, s.getGroups().size());
        }

        @Test
        public void removeGrupoNullTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            boolean removed = s.removeGrupo(null);

            assertFalse(removed);
        }

        @Test
        public void isHasGroupTrueTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g = new Group(30);
            g.setId(1);

            s.addGrupo(g);

            assertTrue(s.isHasGroup(g));
        }

        @Test
        public void isHasGroupFalseTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g1 = new Group(30);
            Group g2 = new Group(25);

            g1.setId(1);
            g2.setId(2);

            s.addGrupo(g1);

            assertFalse(s.isHasGroup(g2));
        }

        @Test
        public void isHasGroupNullTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertFalse(s.isHasGroup(null));
        }

        // ==================== EQUALS, HASHCODE Y TOSTRING ====================

        @Test
        public void equalsTest() {
            Subject s1 = new Subject(101, "Matemáticas", 4);
            Subject s2 = new Subject(101, "Física", 6); // Mismo ID, diferentes otros campos

            assertEquals(s1, s2); // Solo compara por ID
        }

        @Test
        public void notEqualsTest() {
            Subject s1 = new Subject(101, "Matemáticas", 4);
            Subject s2 = new Subject(102, "Matemáticas", 4); // Diferentes IDs

            assertNotEquals(s1, s2);
        }

        @Test
        public void equalsNullTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertNotEquals(s, null);
        }

        @Test
        public void equalsOtherClassTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            String other = "Not a Subject";

            assertNotEquals(s, other);
        }

        @Test
        public void equalsSameInstanceTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            assertEquals(s, s);
        }

        @Test
        public void hashCodeTest() {
            Subject s1 = new Subject(101, "Matemáticas", 4);
            Subject s2 = new Subject(102, "Física", 6);

            assertNotEquals(s1.hashCode(), s2.hashCode());
        }

        @Test
        public void toStringTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g1 = new Group(30);
            Group g2 = new Group(25);

            g1.setId(1);
            g2.setId(2);

            s.addGrupo(g1);
            s.addGrupo(g2);

            String result = s.toString();

            assertTrue(result.contains("101"));
            assertTrue(result.contains("Matemáticas"));
            assertTrue(result.contains("4"));
            assertTrue(result.contains("2")); // Número de grupos
        }

        @Test
        public void toStringWithoutGroupsTest() {
            Subject s = new Subject(101, "Matemáticas", 4);

            String result = s.toString();

            assertTrue(result.contains("101"));
            assertTrue(result.contains("Matemáticas"));
            assertTrue(result.contains("4"));
            assertTrue(result.contains("0")); // Sin grupos
        }

        // ==================== CASOS EDGE Y VALIDACIÓN ====================

        @Test
        public void addMismoGrupoDosVecesTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g = new Group(30);
            g.setId(1);

            s.addGrupo(g);
            s.addGrupo(g); // Añadir el mismo grupo otra vez

            assertEquals(2, s.getGroups().size()); // ArrayList permite duplicados
            assertTrue(s.isHasGroup(g));
        }

        @Test
        public void removeFromEmptyListTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g = new Group(30);
            g.setId(1);

            boolean removed = s.removeGrupo(g);

            assertFalse(removed);
            assertEquals(0, s.getGroups().size());
        }

        @Test
        public void addRemoveMultipleOperationsTest() {
            Subject s = new Subject(101, "Matemáticas", 4);
            Group g1 = new Group(30);
            Group g2 = new Group(25);
            Group g3 = new Group(20);

            g1.setId(1);
            g2.setId(2);
            g3.setId(3);

            // Añadir todos
            s.addGrupo(g1);
            s.addGrupo(g2);
            s.addGrupo(g3);
            assertEquals(3, s.getGroups().size());

            // Remover uno del medio
            s.removeGrupo(g2);
            assertEquals(2, s.getGroups().size());
            assertFalse(s.isHasGroup(g2));

            // Añadir uno nuevo
            Group g4 = new Group(15);
            g4.setId(4);
            s.addGrupo(g4);
            assertEquals(3, s.getGroups().size());
            assertTrue(s.isHasGroup(g4));

            // Remover todos
            s.removeGrupo(g1);
            s.removeGrupo(g3);
            s.removeGrupo(g4);
            assertEquals(0, s.getGroups().size());
        }

        @Test
        public void creditosMaxValueTest() {
            Subject s = new Subject(101, "Matemáticas", Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE, s.getCreditos());
        }

        @Test
        public void nombreConEspaciosTest() {
            Subject s = new Subject(101, " Matemáticas Avanzadas ", 4);
            assertEquals(" Matemáticas Avanzadas ", s.getName());
        }

}
