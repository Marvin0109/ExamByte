package exambyte.domain.exam;

import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CorrectAnswersTest {

    @Test
    void correctAnswers_builder_test() {
        UUID id = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        String correctAnswers = "Solution 1\nSolution 2";
        String choices = "Solution 1\nSolution 2\nSolution 3";

        CorrectAnswers domain = new CorrectAnswers.CorrectAnswersBuilder()
                .id(id)
                .questionId(questionId)
                .solution(correctAnswers)
                .choices(choices)
                .build();

        assertEquals(id, domain.getId());
        assertEquals(questionId, domain.getQuestionId());
        assertThat(domain.getSolution()).contains(correctAnswers);
        assertThat(domain.getChoices()).contains(choices);
    }
}
