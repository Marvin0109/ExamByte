package exambyte.domain.aggregate.exam;

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
        UUID fachId = UUID.randomUUID();
        String title = "Math Exam";
        UUID professorFachId = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        LocalDateTime resultTime = endTime.plusDays(1);

        Exam exam = new Exam.ExamBuilder()
                .fachId(fachId)
                .title(title)
                .professorFachId(professorFachId)
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        assertEquals(fachId, exam.getFachId());
        assertEquals(title, exam.getTitle());
        assertEquals(professorFachId, exam.getProfessorFachId());
        assertEquals(startTime, exam.getStartTime());
        assertEquals(endTime, exam.getEndTime());
        assertEquals(resultTime, exam.getResultTime());
    }
}
