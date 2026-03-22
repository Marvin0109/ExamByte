package exambyte.domain.exam;

import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.aggregate.exam.Question;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {

    @Test
    void question_builder_test() {
        UUID id = UUID.randomUUID();
        String text = "Test question";
        double points = 10;
        QuestionType type = QuestionType.FREE_RESPONSE;
        UUID examId = UUID.randomUUID();

        Question question = new Question.FrageBuilder()
                .id(id)
                .text(text)
                .points(points)
                .type(type)
                .examId(examId)
                .build();

        assertEquals(id, question.getId());
        assertEquals(text, question.getText());
        assertEquals(points, question.getPoints());
        assertEquals(type, question.getType());
        assertEquals(examId, question.getExamId());
    }
}
