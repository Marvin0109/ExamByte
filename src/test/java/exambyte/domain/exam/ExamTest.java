package exambyte.domain.exam;

import exambyte.domain.model.exam.Exam;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExamTest {

    @Test
    void exam_builder_test() {
        UUID id = UUID.randomUUID();
        String title = "Math Exam";
        UUID professorId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        LocalDateTime result = end.plusDays(1);

        Exam exam = new Exam.ExamBuilder()
                .id(id)
                .title(title)
                .professorId(professorId)
                .start(start)
                .end(end)
                .result(result)
                .build();

        assertEquals(id, exam.getId());
        assertEquals(title, exam.getTitle());
        assertEquals(professorId, exam.getProfessorId());
        assertEquals(start, exam.getStart());
        assertEquals(end, exam.getEnd());
        assertEquals(result, exam.getResult());
    }
}
