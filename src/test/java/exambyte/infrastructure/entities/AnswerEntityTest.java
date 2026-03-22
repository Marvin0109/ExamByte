package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.AnswerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AnswerEntityTest {

    @ParameterizedTest
    @DisplayName("Field missing -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createAnswerEntity_fail(AnswerEntity.AnswerEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<AnswerEntity.AnswerEntityBuilder> invalidBuilder() {
        UUID studentId = UUID.randomUUID();

        return Stream.of(
                new AnswerEntity.AnswerEntityBuilder()
                        .answer(null)
                        .questionId(UUID.randomUUID())
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer("")
                        .questionId(UUID.randomUUID())
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer(" ")
                        .questionId(UUID.randomUUID())
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer("Answer")
                        .questionId(null)
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer("Answer")
                        .questionId(UUID.randomUUID())
                        .studentId(null)
        );
    }

}
