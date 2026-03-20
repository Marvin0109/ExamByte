package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FrageEntityTest {

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createFrageEntity_fail(FrageEntity.FrageEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<FrageEntity.FrageEntityBuilder> invalidBuilder() {
        return Stream.of(
                new FrageEntity.FrageEntityBuilder()
                        .frageText("")
                        .examId(UUID.randomUUID())
                        .maxPunkte(1),

                new FrageEntity.FrageEntityBuilder()
                        .frageText(" ")
                        .examId(UUID.randomUUID())
                        .maxPunkte(1),

                new FrageEntity.FrageEntityBuilder()
                        .frageText("Frage")
                        .examId(null)
                        .maxPunkte(1),

                new FrageEntity.FrageEntityBuilder()
                        .frageText("Frage")
                        .examId(UUID.randomUUID())
                        .maxPunkte(0),

                new FrageEntity.FrageEntityBuilder()
                        .frageText("Frage")
                        .examId(UUID.randomUUID())
                        .maxPunkte(-1)
        );
    }
}
