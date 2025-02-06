package exambyte.infrastructure.mapper;

import exambyte.application.dto.ExamDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.time.LocalDateTime;
import exambyte.domain.aggregate.exam.Exam;

import static org.junit.jupiter.api.Assertions.*;

public class ExamDTOMapperImplTest {
    // Single Exam object is correctly mapped to ExamDTO with all fields preserved
    @Test
    @DisplayName("test_exam_maps_to_dto_with_all_fields")
    public void test_01() {
        ExamDTOMapperImpl mapper = new ExamDTOMapperImpl();

        LocalDateTime now = LocalDateTime.now();
        UUID fachId = UUID.randomUUID();
        UUID profId = UUID.randomUUID();

        Exam exam = new Exam.ExamBuilder()
                .id(1L)
                .fachId(fachId)
                .title("Test Exam")
                .professorFachId(profId)
                .startTime(now)
                .endTime(now.plusHours(2))
                .resultTime(now.plusDays(1))
                .build();

        ExamDTO dto = mapper.toDTO(exam);

        assertEquals(1L, dto.id());
        assertEquals(fachId, dto.fachId());
        assertEquals("Test Exam", dto.title());
        assertEquals(profId, dto.professorFachId());
        assertEquals(now, dto.startTime());
        assertEquals(now.plusHours(2), dto.endTime());
        assertEquals(now.plusDays(1), dto.resultTime());
    }

    // Handling null Exam input in toDTO method
    @Test
    @DisplayName("test_null_exam_throws_exception")
    public void test_02() {
        ExamDTOMapperImpl mapper = new ExamDTOMapperImpl();

        assertThrows(NullPointerException.class, () -> {
            mapper.toDTO(null);
        });
    }
}
