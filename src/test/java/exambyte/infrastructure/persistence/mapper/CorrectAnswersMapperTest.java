package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.CorrectAnswersMapper;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.infrastructure.persistence.entities.CorrectAnswersEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorrectAnswersMapperTest {

    private CorrectAnswersMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CorrectAnswersMapperImpl();
    }

    @Test
    void toEntity() {
        // Arrange
        UUID questionId = UUID.randomUUID();
        String solution = "Solution 1\nSolution 2";
        String choices = "Solution 1\nSolution 2\nSolution 3";

        CorrectAnswers correctAnswers = new CorrectAnswers.CorrectAnswersBuilder()
            .questionId(questionId)
            .solution(solution)
            .choices(choices)
            .build();

        // Act
        CorrectAnswersEntity entity = mapper.toEntity(correctAnswers);

        // Assert
        assertThat(entity.getQuestionId()).isEqualTo(questionId);
        assertThat(entity.getSolution()).contains(solution);
        assertThat(entity.getChoices()).contains(choices);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID questionId = UUID.randomUUID();
        String solution = "Solution 1\nSolution 2";
        String choices = "Solution 1\nSolution 2\nSolution 3";
        CorrectAnswersEntity entity = new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
                .questionId(questionId)
                .choices(choices)
                .solution(solution)
                .build();

        // Act
        CorrectAnswers correctAnswers = mapper.toDomain(entity);

        // Assert
        assertThat(correctAnswers.getQuestionId()).isEqualTo(questionId);
        assertThat(correctAnswers.getSolution()).contains(solution);
        assertThat(correctAnswers.getChoices()).contains(choices);
    }
}
