package exambyte.domain.exam;

import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.aggregate.exam.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {

    @Test
    @DisplayName("FrageBuilder Test")
    void test_01() {
        UUID id = UUID.randomUUID();
        String frageText = "Test question";
        double maxPunkte = 10;
        QuestionType type = QuestionType.FREE_RESPONSE;
        UUID examId = UUID.randomUUID();

        Question question = new Question.FrageBuilder()
                .id(id)
                .text(frageText)
                .points(maxPunkte)
                .type(type)
                .examId(examId)
                .build();

        assertEquals(id, question.getId());
        assertEquals(frageText, question.getText());
        assertEquals(maxPunkte, question.getPoints());
        assertEquals(type, question.getType());
        assertEquals(examId, question.getExamId());
    }
}
