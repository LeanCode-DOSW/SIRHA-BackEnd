package edu.dosw.sirha.sirha_backend;

import edu.dosw.sirha.sirha_backend.domain.AcademicStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AcademicStatusTest {

    @Test
    void testEnumValues() {
        for (AcademicStatus status : AcademicStatus.values()) {
            assertNotNull(status.name());
        }
    }

    @Test
    void testValueOf() {
        AcademicStatus status = AcademicStatus.valueOf("GOOD"); // usa un valor real de tu enum
        assertEquals(AcademicStatus.GOOD, status);
    }
}
