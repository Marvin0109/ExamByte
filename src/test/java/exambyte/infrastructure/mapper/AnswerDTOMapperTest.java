package exambyte.infrastructure.mapper;

import exambyte.application.dto.AnswerDTO;
import exambyte.domain.mapper.AnswerDTOMapper;
import exambyte.domain.model.aggregate.exam.Answer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnswerDTOMapperTest {

    private final AnswerDTOMapper mapper = new AnswerDTOMapperImpl();

    @Test
    @DisplayName("Test AnswerDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Answer answer = new Answer.AnswerBuilder()
                .id(id)
                .answer("Answer")
                .frageId(frageId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        // Act
        AnswerDTO dto = mapper.toDTO(answer);

        // Assert
        assertEquals(id, dto.id());
        assertEquals(studentId, dto.studentId());
        assertEquals(frageId, dto.frageId());
        assertEquals("Answer", dto.answer());
        assertEquals(submitTime, dto.submitTime());
    }

    @Test
    @DisplayName("test_null_answer_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toAnswerDTOList Test")
    void test_03() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        UUID id2 = UUID.randomUUID();
        UUID studentId2 = UUID.randomUUID();
        UUID frageId2 = UUID.randomUUID();
        LocalDateTime submitTime2 = LocalDateTime.of(2020, 1, 1, 1, 0);

        Answer answer = new Answer.AnswerBuilder()
                .id(id)
                .answer("Answer")
                .frageId(frageId)
                .studentId(studentId)
                .submitTime(submitTime)
                .build();

        Answer answer2 = new Answer.AnswerBuilder()
                .id(id2)
                .answer("Answer2")
                .frageId(frageId2)
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
        assertEquals(frageId, dtoList.getFirst().frageId());
        assertEquals(submitTime, dtoList.getFirst().submitTime());

        assertEquals(id2, dtoList.getLast().id());
        assertEquals(studentId2, dtoList.getLast().studentId());
        assertEquals(frageId2, dtoList.getLast().frageId());
        assertEquals(submitTime2, dtoList.getLast().submitTime());
    }

    @Test
    @DisplayName("toDomain Test")
    void test_04() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        AnswerDTO dto = new AnswerDTO(
                id,
                "Answer",
                frageId,
                studentId,
                submitTime);

        // Act
        Answer answer = mapper.toDomain(dto);

        // Assert
        assertEquals(id, answer.getId());
        assertEquals(studentId, answer.getStudentUUID());
        assertEquals(frageId, answer.getFrageId());
        assertEquals(submitTime, answer.getSubmitTime());
        assertEquals("Answer", answer.getAnswer());
    }
}
