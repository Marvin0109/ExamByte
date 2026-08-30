package exambyte.application.mapper;

import exambyte.application.dto.ExamDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import exambyte.domain.model.exam.Exam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExamDTOMapperTest {

    private final ExamDTOMapper mapper = new ExamDTOMapper();

    @Test
    void toDTO() {
        //Arrange
        LocalDateTime now = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        UUID id = UUID.randomUUID();
        UUID profId = UUID.randomUUID();

        Exam exam = new Exam.ExamBuilder()
                .id(id)
                .title("Test Exam")
                .professorId(profId)
                .start(now)
                .end(now.plusHours(2))
                .result(now.plusDays(1))
                .build();

        // Act
        ExamDTO dto = mapper.toDTO(exam);

        // Assert
        assertEquals(id, dto.id());
        assertEquals("Test Exam", dto.title());
        assertEquals(profId, dto.professorId());
        assertEquals(now, dto.start());
        assertEquals(now.plusHours(2), dto.end());
        assertEquals(now.plusDays(1), dto.result());
    }

    @Test
    void toExamDTOList() {
        // Arrange
        LocalDateTime now = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID profId = UUID.randomUUID();

        Exam exam1 = new Exam.ExamBuilder()
                .id(id)
                .title("Test Exam 1")
                .professorId(profId)
                .start(now)
                .end(now.plusHours(2))
                .result(now.plusDays(1))
                .build();

        Exam exam2 = new Exam.ExamBuilder()
                .id(id2)
                .title("Test Exam 2")
                .professorId(profId)
                .start(now)
                .end(now.plusHours(2))
                .result(now.plusDays(1))
                .build();

        List<Exam> exams = Arrays.asList(exam1, exam2);

        // Act
        List<ExamDTO> examDTOList = mapper.toExamDTOList(exams);

        // Assert
        assertEquals(2, examDTOList.size());
        assertThat(examDTOList.getFirst().id()).isEqualTo(id);
        assertThat(examDTOList.getFirst().title()).isEqualTo("Test Exam 1");
        assertThat(examDTOList.getFirst().professorId()).isEqualTo(profId);
        assertThat(examDTOList.get(1).id()).isEqualTo(id2);
        assertThat(examDTOList.get(1).title()).isEqualTo("Test Exam 2");
        assertThat(examDTOList.get(1).professorId()).isEqualTo(profId);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID profId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2020, 1, 1, 1, 2);
        LocalDateTime resultTime = LocalDateTime.of(2020, 1, 1, 1, 3);
        ExamDTO dto = new ExamDTO(
                id,
                "Exam 1",
                profId,
                start,
                end,
                resultTime);

        // Act
        Exam exam = mapper.toDomain(dto);

        // Assert
        assertEquals(id, exam.getId());
        assertEquals("Exam 1", exam.getTitle());
        assertEquals(profId, exam.getProfessorId());
        assertEquals(start, exam.getStart());
        assertEquals(end, exam.getEnd());
        assertEquals(resultTime, exam.getResult());
    }
}
