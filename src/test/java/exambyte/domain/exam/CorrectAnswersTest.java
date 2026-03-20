package exambyte.domain.exam;

import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CorrectAnswersTest {

    @Test
    @DisplayName("CorrectAnswersBuilder Test")
    void test_01() {
        UUID id = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        String correctAnswers = "Lösung 1\nLösung 2";
        String choices = "Lösung 1\nLösung 2\nLösung 3";

        CorrectAnswers domain = new CorrectAnswers.CorrectAnswersBuilder()
                .id(id)
                .frageId(frageId)
                .solution(correctAnswers)
                .choices(choices)
                .build();

        assertEquals(id, domain.getId());
        assertEquals(frageId, domain.getFrageId());
        assertThat(domain.getSolution()).contains(correctAnswers);
        assertThat(domain.getChoices()).contains(choices);
    }
}
