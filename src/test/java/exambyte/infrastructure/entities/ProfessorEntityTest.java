package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfessorEntityTest {

    @Test
    @DisplayName("Professor-Fach-ID wird immer generiert")
    void createProfessorEntity_success() {
        ProfessorEntity professorEntity = new ProfessorEntity.ProfessorEntityBuilder()
                .name("Prof")
                .build();

        assertThat(professorEntity.getFachId()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createProfessorEntity_fail(ProfessorEntity.ProfessorEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<ProfessorEntity.ProfessorEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new ProfessorEntity.ProfessorEntityBuilder()
                        .name(" "),

                new ProfessorEntity.ProfessorEntityBuilder()
                        .name("")
        );
    }
}
