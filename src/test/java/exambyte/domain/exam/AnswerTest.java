package exambyte.domain.exam;

import exambyte.domain.model.exam.Answer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnswerTest {

    @Test
    void answer_builder_test_success() {
        UUID id = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Answer answer = new Answer.AnswerBuilder()
                .id(id)
                .answer("Answer")
                .questionId(questionId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        assertEquals(id, answer.getId());
        assertEquals("Answer", answer.getAnswer());
        assertEquals(questionId, answer.getQuestionId());
        assertEquals(studentId, answer.getStudentUUID());
        assertEquals(submitTime, answer.getSubmitTime());

    }

    @Test
    void answer_builder_test_idNull() {
        UUID questionId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Answer answer = new Answer.AnswerBuilder()
                .questionId(questionId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        assertNull(answer.getAnswer());
        assertEquals(questionId, answer.getQuestionId());
        assertEquals(studentId, answer.getStudentUUID());
        assertEquals(submitTime, answer.getSubmitTime());
    }

}
