package exambyte.infrastructure.mapper;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.domain.mapper.QuestionDTOMapper;
import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.model.common.QuestionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionDTOMapperTest {

    private final QuestionDTOMapper mapper = new QuestionDTOMapperImpl();

    @Test
    @DisplayName("Test QuestionDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID examId = UUID.randomUUID();

        Question question = new Question.FrageBuilder()
                .id(id)
                .text("Question 1")
                .points(10)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .examId(examId)
                .build();

        // Act
        QuestionDTO dto = mapper.toDTO(question);

        // Assert
        assertEquals(id, dto.id());
        assertEquals(examId, dto.examId());
        assertEquals(QuestionType.MC, QuestionType.valueOf(dto.type().name()));
        assertEquals(question.getText(), dto.text());
        assertEquals(question.getPoints(), dto.points());
    }

    @Test
    @DisplayName("test_null_frage_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toFrageDTOList Test")
    void test_03() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        UUID examId1 = UUID.randomUUID();
        UUID examId2 = UUID.randomUUID();

        Question question1 = new Question.FrageBuilder()
                .id(id1)
                .text("Question 1")
                .points(4)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .examId(examId1)
                .build();

        Question question2 = new Question.FrageBuilder()
                .id(id2)
                .text("Question 2")
                .points(9)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .examId(examId2)
                .build();

        List<Question> fragen  = Arrays.asList(question1, question2);

        // Act
        List<QuestionDTO> questionDTOList = mapper.toQuestionDTOList(fragen);

        // Assert
        assertEquals(fragen.size(), questionDTOList.size());
        assertThat(questionDTOList.getFirst().id()).isEqualTo(id1);
        assertThat(questionDTOList.getFirst().type()).isEqualTo(QuestionTypeDTO.valueOf(question1.getType().name()));
        assertThat(questionDTOList.getFirst().examId()).isEqualTo(examId1);
        assertThat(questionDTOList.getFirst().text()).isEqualTo(question1.getText());

        assertThat(questionDTOList.getLast().id()).isEqualTo(id2);
        assertThat(questionDTOList.getLast().type()).isEqualTo(QuestionTypeDTO.valueOf(question2.getType().name()));
        assertThat(questionDTOList.getLast().examId()).isEqualTo(examId2);
        assertThat(questionDTOList.getLast().text()).isEqualTo(question2.getText());
    }
}
