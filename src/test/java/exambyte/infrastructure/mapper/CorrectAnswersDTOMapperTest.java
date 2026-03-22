package exambyte.infrastructure.mapper;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("toDTO Test")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        String solution = "Lösung 1\nLösung 2";
        String choices = "Lösung 1\nLösung 2\nLösung 3";

        CorrectAnswers correctAnswers = new CorrectAnswers.CorrectAnswersBuilder()
                .id(id)
                .frageId(frageId)
                .solution(solution)
                .choices(choices)
                .build();

        // Act
        CorrectAnswersDTO dto = mapper.toDTO(correctAnswers);

        // Assert
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.questionId()).isEqualTo(frageId);
        assertThat(dto.solution()).contains(solution);
        assertThat(dto.choices()).contains(choices);
    }

    @Test
    @DisplayName("toDomain Test")
    void test_02() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        String solution = "Lösung 1\nLösung 2";
        String choices = "Lösung 1\nLösung 2\nLösung 3";

        CorrectAnswersDTO dto = new CorrectAnswersDTO(id, solution, choices, frageId);

        // Act
        CorrectAnswers result = mapper.toDomain(dto);

        // Assert
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFrageId()).isEqualTo(frageId);
        assertThat(result.getSolution()).contains(solution);
        assertThat(result.getChoices()).contains(choices);
    }
}
