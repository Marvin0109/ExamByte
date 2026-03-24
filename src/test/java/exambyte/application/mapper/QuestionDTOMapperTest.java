package exambyte.application.mapper;

import exambyte.application.enums.QuestionTypeDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.enums.QuestionType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionDTOMapperTest {

    private final QuestionDTOMapper mapper = new QuestionDTOMapperImpl();

    @Test
    void toDTO() {
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
    void toQuestionDTOList() {
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

        List<Question> questions = Arrays.asList(question1, question2);

        // Act
        List<QuestionDTO> questionDTOList = mapper.toQuestionDTOList(questions);

        // Assert
        assertEquals(questions.size(), questionDTOList.size());
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
