package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.CorrectAnswersMapper;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.infrastructure.persistence.entities.CorrectAnswersEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorrectAnswersMapperTest {

    private CorrectAnswersMapper correctAnswersMapper;

    @BeforeEach
    void setUp() {
        correctAnswersMapper = new CorrectAnswersMapperImpl();
    }

    @Test
    void toEntity() {
        // Arrange
        UUID frageId = UUID.randomUUID();
        String solution = "Lösung 1\nLösung 2";
        String choices = "Lösung 1\nLösung 2\nLösung 3";

        CorrectAnswers correctAnswers = new CorrectAnswers.CorrectAnswersBuilder()
            .frageId(frageId)
            .solution(solution)
            .choices(choices)
            .build();

        // Act
        CorrectAnswersEntity entity = correctAnswersMapper.toEntity(correctAnswers);

        // Assert
        assertThat(entity.getFrageId()).isEqualTo(frageId);
        assertThat(entity.getSolution()).contains(solution);
        assertThat(entity.getChoices()).contains(choices);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID frageId = UUID.randomUUID();
        String solution = "Lösung 1\nLösung 2";
        String choices = "Lösung 1\nLösung 2\nLösung 3";
        CorrectAnswersEntity entity = new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
                .frageId(frageId)
                .choices(choices)
                .solution(solution)
                .build();

        // Act
        CorrectAnswers correctAnswers = correctAnswersMapper.toDomain(entity);

        // Assert
        assertThat(correctAnswers.getFrageId()).isEqualTo(frageId);
        assertThat(correctAnswers.getSolution()).contains(solution);
        assertThat(correctAnswers.getChoices()).contains(choices);
    }
}
