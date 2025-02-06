package exambyte.domain.aggregate.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
public class ExamTest {

    // Create exam with all required fields using builder pattern
    @Test
    @DisplayName("create_exam_with_all_fields")
    public void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        String title = "Math Exam";
        UUID professorFachId = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        LocalDateTime resultTime = endTime.plusDays(1);

        Exam exam = new Exam.ExamBuilder()
                .id(id)
                .fachId(fachId)
                .title(title)
                .professorFachId(professorFachId)
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        assertEquals(id, exam.getId());
        assertEquals(fachId, exam.getFachId());
        assertEquals(title, exam.getTitle());
        assertEquals(professorFachId, exam.getProfessorFachId());
        assertEquals(startTime, exam.getStartTime());
        assertEquals(endTime, exam.getEndTime());
        assertEquals(resultTime, exam.getResultTime());
    }
    // Create exam with null values for optional fields
    @Test
    @DisplayName("create_exam_with_optional_fields_null")
    public void test_02() {
        String title = "Math Exam";
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);

        Exam exam = new Exam.ExamBuilder()
                .title(title)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        assertNull(exam.getId());
        assertNotNull(exam.getFachId());
        assertEquals(title, exam.getTitle());
        assertNull(exam.getProfessorFachId());
        assertEquals(startTime, exam.getStartTime());
        assertEquals(endTime, exam.getEndTime());
        assertNull(exam.getResultTime());
    }
}
