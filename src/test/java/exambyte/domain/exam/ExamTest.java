package exambyte.domain.exam;

import exambyte.domain.model.aggregate.exam.Exam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExamTest {

    @Test
    @DisplayName("Exam Builder Test")
    void test_01() {
        UUID id = UUID.randomUUID();
        String title = "Math Exam";
        UUID professorId = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        LocalDateTime resultTime = endTime.plusDays(1);

        Exam exam = new Exam.ExamBuilder()
                .id(id)
                .title(title)
                .professorId(professorId)
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        assertEquals(id, exam.getId());
        assertEquals(title, exam.getTitle());
        assertEquals(professorId, exam.getProfessorId());
        assertEquals(startTime, exam.getStartTime());
        assertEquals(endTime, exam.getEndTime());
        assertEquals(resultTime, exam.getResultTime());
    }
}
