package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfessorEntityTest {

    @ParameterizedTest
    @DisplayName("Field missing -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createProfessorEntity_fail(ProfessorEntity.ProfessorEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<ProfessorEntity.ProfessorEntityBuilder> invalidBuilder() {
        return Stream.of(
                new ProfessorEntity.ProfessorEntityBuilder()
                        .name(" "),

                new ProfessorEntity.ProfessorEntityBuilder()
                        .name("")
        );
    }
}
