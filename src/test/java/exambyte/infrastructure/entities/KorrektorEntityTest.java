package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.KorrektorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KorrektorEntityTest {

    @Test
    @DisplayName("Korrektor-Fach-ID wird immer generiert")
    void createKorrektorEntity_success() {
        KorrektorEntity korrektorEntity = new KorrektorEntity.KorrektorEntityBuilder()
                .name("John")
                .build();

        assertThat(korrektorEntity.getFachId()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createKorrektorEntity_fail(KorrektorEntity.KorrektorEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<KorrektorEntity.KorrektorEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new KorrektorEntity.KorrektorEntityBuilder()
                        .name(" "),
                new KorrektorEntity.KorrektorEntityBuilder()
                        .name("")
        );
    }
}
