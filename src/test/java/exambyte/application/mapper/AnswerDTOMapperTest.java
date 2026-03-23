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

    private final AnswerDTOMapper mapper = new AnswerDTOMapperImpl();

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
    void toAnswerDTOList() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        UUID id2 = UUID.randomUUID();
        UUID studentId2 = UUID.randomUUID();
        UUID questionId2 = UUID.randomUUID();
        LocalDateTime submitTime2 = LocalDateTime.of(2020, 1, 1, 1, 0);

        Answer answer = new Answer.AnswerBuilder()
                .id(id)
                .answer("Answer")
                .questionId(questionId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        Answer answer2 = new Answer.AnswerBuilder()
                .id(id2)
                .answer("Answer2")
                .questionId(questionId2)
                .studentId(studentId2)
                .submitTime(submitTime2)
                .build();

        List<Answer> answerList = Arrays.asList(answer, answer2);

        // Act
        List<AnswerDTO> dtoList = mapper.toAnswerDTOList(answerList);

        // Assert
        assertEquals(2, dtoList.size());
        assertEquals(id, dtoList.getFirst().id());
        assertEquals(studentId, dtoList.getFirst().studentId());
        assertEquals(questionId, dtoList.getFirst().questionId());
        assertEquals(submitTime, dtoList.getFirst().submitTime());

        assertEquals(id2, dtoList.getLast().id());
        assertEquals(studentId2, dtoList.getLast().studentId());
        assertEquals(questionId2, dtoList.getLast().questionId());
        assertEquals(submitTime2, dtoList.getLast().submitTime());
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
