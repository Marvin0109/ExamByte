package exambyte.application.mapper;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.model.exam.CorrectAnswers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorrectAnswersDTOMapperTest {

    private CorrectAnswersDTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CorrectAnswersDTOMapperImpl();
    }

    @Test
    void toDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        String solution = "Solution 1\nSolution 2";
        String choices = "Solution 1\nSolution 2\nSolution 3";

        CorrectAnswers correctAnswers = new CorrectAnswers.CorrectAnswersBuilder()
                .id(id)
                .questionId(questionId)
                .solution(solution)
                .choices(choices)
                .build();

        // Act
        CorrectAnswersDTO dto = mapper.toDTO(correctAnswers);

        // Assert
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.questionId()).isEqualTo(questionId);
        assertThat(dto.solution()).contains(solution);
        assertThat(dto.choices()).contains(choices);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        String solution = "Solution 1\nSolution 2";
        String choices = "Solution 1\nSolution 2\nSolution 3";

        CorrectAnswersDTO dto = new CorrectAnswersDTO(id, solution, choices, questionId);

        // Act
        CorrectAnswers result = mapper.toDomain(dto);

        // Assert
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getQuestionId()).isEqualTo(questionId);
        assertThat(result.getSolution()).contains(solution);
        assertThat(result.getChoices()).contains(choices);
    }
}
