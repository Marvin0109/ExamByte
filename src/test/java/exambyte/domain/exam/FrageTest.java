package exambyte.domain.exam;

import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.aggregate.exam.Frage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrageTest {

    @Test
    @DisplayName("FrageBuilder Test")
    void test_01() {
        UUID id = UUID.randomUUID();
        String frageText = "Test question";
        int maxPunkte = 10;
        QuestionType type = QuestionType.FREITEXT;
        UUID examId = UUID.randomUUID();

        Frage frage = new Frage.FrageBuilder()
                .id(id)
                .frageText(frageText)
                .maxPunkte(maxPunkte)
                .type(type)
                .examId(examId)
                .build();

        assertEquals(id, frage.getId());
        assertEquals(frageText, frage.getFrageText());
        assertEquals(maxPunkte, frage.getMaxPunkte());
        assertEquals(type, frage.getType());
        assertEquals(examId, frage.getExamId());
    }
}
