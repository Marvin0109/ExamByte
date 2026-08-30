package exambyte.application.mapper;

import exambyte.application.dto.AnswerDTO;
import exambyte.domain.model.exam.Answer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnswerDTOMapperTest {

    private final AnswerDTOMapper mapper = new AnswerDTOMapper();

    @Test
    void toDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Answer answer = new Answer.AnswerBuilder()
                .id(id)
                .answer("Answer")
                .questionId(questionId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        // Act
        AnswerDTO dto = mapper.toDTO(answer);

        // Assert
        assertEquals(id, dto.id());
        assertEquals(studentId, dto.studentId());
        assertEquals(questionId, dto.questionId());
        assertEquals("Answer", dto.answer());
        assertEquals(submitTime, dto.submitTime());
    }

    @Test
    void toDomain() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        AnswerDTO dto = new AnswerDTO(
                id,
                "Answer",
                questionId,
                studentId,
                submitTime);

        // Act
        Answer answer = mapper.toDomain(dto);

        // Assert
        assertEquals(id, answer.getId());
        assertEquals(studentId, answer.getStudentUUID());
        assertEquals(questionId, answer.getQuestionId());
        assertEquals(submitTime, answer.getSubmitTime());
        assertEquals("Answer", answer.getAnswer());
    }
}
