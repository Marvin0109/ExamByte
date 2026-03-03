package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AntwortEntityTest {

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createAntwortEntity_fail(AntwortEntity.AntwortEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<AntwortEntity.AntwortEntityBuilder> ungueltigeBuilder() {
        UUID studentId = UUID.randomUUID();

        return Stream.of(
                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText(null)
                        .frageId(UUID.randomUUID())
                        .studentId(studentId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText("")
                        .frageId(UUID.randomUUID())
                        .studentId(studentId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText(" ")
                        .frageId(UUID.randomUUID())
                        .studentId(studentId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText("Antwort")
                        .frageId(null)
                        .studentId(studentId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText("Antwort")
                        .frageId(UUID.randomUUID())
                        .studentId(null)
        );
    }

}
