package exambyte.domain.aggregate.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
public class FrageTest {

    // Create Frage instance with all valid parameters using builder pattern
    @Test
    @DisplayName("create_frage_with_valid_parameters")
    public void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        String frageText = "Test question";
        int maxPunkte = 10;
        UUID professorUUID = UUID.randomUUID();
        UUID examUUID = UUID.randomUUID();

        Frage frage = new Frage.FrageBuilder()
                .id(id)
                .fachId(fachId)
                .frageText(frageText)
                .maxPunkte(maxPunkte)
                .professorUUID(professorUUID)
                .examUUID(examUUID)
                .build();

        assertEquals(id, frage.getId());
        assertEquals(fachId, frage.getFachId());
        assertEquals(frageText, frage.getFrageText());
        assertEquals(maxPunkte, frage.getMaxPunkte());
        assertEquals(professorUUID, frage.getProfessorUUID());
        assertEquals(examUUID, frage.getExamUUID());
    }

    // Create Frage with null id
    @Test
    @DisplayName("create_frage_with_null_id")
    public void test_02() {
        UUID fachId = UUID.randomUUID();
        String frageText = "Test question";
        int maxPunkte = 10;
        UUID professorUUID = UUID.randomUUID();
        UUID examUUID = UUID.randomUUID();

        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(fachId)
                .frageText(frageText)
                .maxPunkte(maxPunkte)
                .professorUUID(professorUUID)
                .examUUID(examUUID)
                .build();

        assertNull(frage.getId());
        assertEquals(fachId, frage.getFachId());
        assertEquals(frageText, frage.getFrageText());
        assertEquals(maxPunkte, frage.getMaxPunkte());
        assertEquals(professorUUID, frage.getProfessorUUID());
        assertEquals(examUUID, frage.getExamUUID());
    }
}
