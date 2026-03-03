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

    private static final LocalDateTime START_TIME =
            LocalDateTime.of(2000, 1, 1, 0, 0, 0);

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createExamEntity_fail(ExamEntity.ExamEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<ExamEntity.ExamEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new ExamEntity.ExamEntityBuilder()
                        .title("")
                        .professorId(UUID.randomUUID())
                        .startZeitpunkt(START_TIME)
                        .endZeitpunkt(START_TIME.plusHours(1))
                        .resultZeitpunkt(START_TIME.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(null)
                        .startZeitpunkt(START_TIME)
                        .endZeitpunkt(START_TIME.plusHours(1))
                        .resultZeitpunkt(START_TIME.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(UUID.randomUUID())
                        .startZeitpunkt(null)
                        .endZeitpunkt(START_TIME.plusHours(1))
                        .resultZeitpunkt(START_TIME.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(UUID.randomUUID())
                        .startZeitpunkt(START_TIME)
                        .endZeitpunkt(null)
                        .resultZeitpunkt(START_TIME.plusHours(2)),

                new ExamEntity.ExamEntityBuilder()
                        .title("Exam")
                        .professorId(UUID.randomUUID())
                        .startZeitpunkt(START_TIME)
                        .endZeitpunkt(START_TIME.plusHours(1))
                        .resultZeitpunkt(null)
        );
    }
}
