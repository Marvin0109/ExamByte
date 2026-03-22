package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.QuestionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionEntityTest {

    @ParameterizedTest
    @DisplayName("Field missing -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createFrageEntity_fail(QuestionEntity.QuestionEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<QuestionEntity.QuestionEntityBuilder> invalidBuilder() {
        return Stream.of(
                new QuestionEntity.QuestionEntityBuilder()
                        .text("")
                        .examId(UUID.randomUUID())
                        .points(1),

                new QuestionEntity.QuestionEntityBuilder()
                        .text(" ")
                        .examId(UUID.randomUUID())
                        .points(1),

                new QuestionEntity.QuestionEntityBuilder()
                        .text("Question")
                        .examId(null)
                        .points(1),

                new QuestionEntity.QuestionEntityBuilder()
                        .text("Question")
                        .examId(UUID.randomUUID())
                        .points(0),

                new QuestionEntity.QuestionEntityBuilder()
                        .text("Question")
                        .examId(UUID.randomUUID())
                        .points(-1)
        );
    }
}
