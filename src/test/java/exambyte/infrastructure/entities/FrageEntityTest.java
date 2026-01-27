package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FrageEntityTest {

    @Test
    @DisplayName("Die Frage-Fach-ID wird immer generiert")
    void createFrageEntity_success() {
        FrageEntity frageEntity = new FrageEntity.FrageEntityBuilder()
                .frageText("Frage")
                .professorFachId(UUID.randomUUID())
                .examFachId(UUID.randomUUID())
                .maxPunkte(1)
                .build();

        assertThat(frageEntity.getFachId()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createFrageEntity_fail(FrageEntity.FrageEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<FrageEntity.FrageEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new FrageEntity.FrageEntityBuilder()
                        .frageText("")
                        .professorFachId(UUID.randomUUID())
                        .examFachId(UUID.randomUUID())
                        .maxPunkte(1),

                new FrageEntity.FrageEntityBuilder()
                        .frageText(" ")
                        .professorFachId(UUID.randomUUID())
                        .examFachId(UUID.randomUUID())
                        .maxPunkte(1),

                new FrageEntity.FrageEntityBuilder()
                        .frageText("Frage")
                        .professorFachId(null)
                        .examFachId(UUID.randomUUID())
                        .maxPunkte(1),

                new FrageEntity.FrageEntityBuilder()
                        .frageText("Frage")
                        .professorFachId(UUID.randomUUID())
                        .examFachId(null)
                        .maxPunkte(1),

                new FrageEntity.FrageEntityBuilder()
                        .frageText("Frage")
                        .professorFachId(UUID.randomUUID())
                        .examFachId(UUID.randomUUID())
                        .maxPunkte(0),

                new FrageEntity.FrageEntityBuilder()
                        .frageText("Frage")
                        .professorFachId(UUID.randomUUID())
                        .examFachId(UUID.randomUUID())
                        .maxPunkte(-1)
        );
    }
}
