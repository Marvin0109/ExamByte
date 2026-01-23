package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.entitymapper.KorrektorMapper;
import exambyte.infrastructure.persistence.entities.KorrektorEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KorrektorMapperTest {

    private final KorrektorMapper korrektorMapper = new KorrektorMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .name("Korrektor1")
                .build();

        // Act
        KorrektorEntity korrektorEntity = korrektorMapper.toEntity(korrektor);

        // Assert
        assertThat(korrektorEntity.getName()).isEqualTo("Korrektor1");
    }

    @Test
    void toDomain() {
        // Arrange
        KorrektorEntity korrektorEntity = new KorrektorEntity.KorrektorEntityBuilder()
                .name("Korrektor2")
                .build();

        // Act
        Korrektor korrektor = korrektorMapper.toDomain(korrektorEntity);

        // Assert
        assertThat(korrektor.getName()).isEqualTo("Korrektor2");
    }
}
