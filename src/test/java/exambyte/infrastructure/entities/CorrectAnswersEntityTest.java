package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.CorrectAnswersEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CorrectAnswersEntityTest {

    @ParameterizedTest
    @DisplayName("Field missing -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createCorrectAnswersEntity_fail(CorrectAnswersEntity.CorrectAnswersEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<CorrectAnswersEntity.CorrectAnswersEntityBuilder> invalidBuilder() {
        return Stream.of(
                new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
                        .questionId(null)
                        .solution("A\nB\nC")
                        .choices("A\nB\nC\nD"),

                new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
                        .questionId(UUID.randomUUID())
                        .solution("")
                        .choices("A\nB\nC\nD"),

                new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
                        .questionId(UUID.randomUUID())
                        .solution(" ")
                        .choices("A\nB\nC\nD"),

                new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
                        .questionId(UUID.randomUUID())
                        .solution("A\nB\nC")
                        .choices(""),

                new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
                        .questionId(UUID.randomUUID())
                        .solution("A\nB\nC")
                        .choices(" ")
        );
    }
}
