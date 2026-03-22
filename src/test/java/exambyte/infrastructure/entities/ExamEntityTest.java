package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.ExamEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ExamEntityTest {

    private static final LocalDateTime START =
            LocalDateTime.of(2000, 1, 1, 0, 0, 0);

    @ParameterizedTest
    @DisplayName("Field missing -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createExamEntity_fail(ExamEntity.ExamEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<ExamEntity.ExamEntityBuilder> invalidBuilder() {
        return Stream.of(
                new ExamEntity.ExamEntityBuilder()
                        .title("")
                        .professorId(UUID.randomUUID())
                        .start(START)
                        .end(START.plusHours(1))
                        .result(START.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(null)
                        .start(START)
                        .end(START.plusHours(1))
                        .result(START.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(UUID.randomUUID())
                        .start(null)
                        .end(START.plusHours(1))
                        .result(START.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(UUID.randomUUID())
                        .start(START)
                        .end(null)
                        .result(START.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(UUID.randomUUID())
                        .start(START)
                        .end(START.plusHours(1))
                        .result(null)
        );
    }
}
