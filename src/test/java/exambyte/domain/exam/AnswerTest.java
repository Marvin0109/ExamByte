package exambyte.domain.exam;

import exambyte.domain.model.aggregate.exam.Answer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnswerTest {

    @Test
    @DisplayName("AnswerBuilder Test")
    void test_01() {
        UUID id = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Answer answer = new Answer.AnswerBuilder()
                .id(id)
                .answer("Answer")
                .frageId(frageId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        assertEquals(id, answer.getId());
        assertEquals("Answer", answer.getAnswer());
        assertEquals(frageId, answer.getFrageId());
        assertEquals(studentId, answer.getStudentUUID());
        assertEquals(submitTime, answer.getSubmitTime());

    }

    @Test
    @DisplayName("AnswerBuilder Test mit null Werte")
    void test_02() {
        UUID frageId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Answer answer = new Answer.AnswerBuilder()
                .frageId(frageId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        assertNull(answer.getAnswer());
        assertEquals(frageId, answer.getFrageId());
        assertEquals(studentId, answer.getStudentUUID());
        assertEquals(submitTime, answer.getSubmitTime());
    }

}
