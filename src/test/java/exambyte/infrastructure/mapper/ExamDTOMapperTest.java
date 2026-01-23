package exambyte.infrastructure.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.mapper.ExamDTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import exambyte.domain.model.aggregate.exam.Exam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExamDTOMapperTest {

    private final ExamDTOMapper mapper = new ExamDTOMapperImpl();

    @Test
    @DisplayName("Test ExamDTOMapper 'toDTO'")
    void test_01() {
        //Arrange
        LocalDateTime now = LocalDateTime.now();
        UUID fachId = UUID.randomUUID();
        UUID profId = UUID.randomUUID();

        Exam exam = new Exam.ExamBuilder()
                .fachId(fachId)
                .title("Test Exam")
                .professorFachId(profId)
                .startTime(now)
                .endTime(now.plusHours(2))
                .resultTime(now.plusDays(1))
                .build();

        // Act
        ExamDTO dto = mapper.toDTO(exam);

        // Assert
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
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toExamDTOList Test")
    void test_03() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        UUID fachId = UUID.randomUUID();
        UUID fachId2 = UUID.randomUUID();
        UUID profId = UUID.randomUUID();

        Exam exam1 = new Exam.ExamBuilder()
                .fachId(fachId)
                .title("Test Exam 1")
                .professorFachId(profId)
                .startTime(now)
                .endTime(now.plusHours(2))
                .resultTime(now.plusDays(1))
                .build();

        Exam exam2 = new Exam.ExamBuilder()
                .fachId(fachId2)
                .title("Test Exam 2")
                .professorFachId(profId)
                .startTime(now)
                .endTime(now.plusHours(2))
                .resultTime(now.plusDays(1))
                .build();

        List<Exam> exams = Arrays.asList(exam1, exam2);

        // Act
        List<ExamDTO> examDTOList = mapper.toExamDTOList(exams);

        // Assert
        assertEquals(2, examDTOList.size());
        assertThat(examDTOList.getFirst().fachId()).isEqualTo(fachId);
        assertThat(examDTOList.getFirst().title()).isEqualTo("Test Exam 1");
        assertThat(examDTOList.getFirst().professorFachId()).isEqualTo(profId);
        assertThat(examDTOList.get(1).fachId()).isEqualTo(fachId2);
        assertThat(examDTOList.get(1).title()).isEqualTo("Test Exam 2");
        assertThat(examDTOList.get(1).professorFachId()).isEqualTo(profId);
    }

    @Test
    @DisplayName("toDomain Test")
    void test_04() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2020, 1, 1, 1, 2);
        LocalDateTime resultTime = LocalDateTime.of(2020, 1, 1, 1, 3);
        ExamDTO dto = new ExamDTO(
                fachId,
                "Exam 1",
                profFachId,
                start,
                end,
                resultTime);

        // Act
        Exam exam = mapper.toDomain(dto);

        // Assert
        assertEquals(fachId, exam.getFachId());
        assertEquals("Exam 1", exam.getTitle());
        assertEquals(profFachId, exam.getProfessorFachId());
        assertEquals(start, exam.getStartTime());
        assertEquals(end, exam.getEndTime());
        assertEquals(resultTime, exam.getResultTime());
    }
}
