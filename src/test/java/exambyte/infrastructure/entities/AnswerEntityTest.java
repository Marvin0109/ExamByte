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
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createAnswerEntity_fail(AnswerEntity.AnswerEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<AnswerEntity.AnswerEntityBuilder> invalidBuilder() {
        UUID studentId = UUID.randomUUID();

        return Stream.of(
                new AnswerEntity.AnswerEntityBuilder()
                        .answer(null)
                        .frageId(UUID.randomUUID())
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer("")
                        .frageId(UUID.randomUUID())
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer(" ")
                        .frageId(UUID.randomUUID())
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer("Answer")
                        .frageId(null)
                        .studentId(studentId),

                new AnswerEntity.AnswerEntityBuilder()
                        .answer("Answer")
                        .frageId(UUID.randomUUID())
                        .studentId(null)
        );
    }

}
