package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.entitymapper.KorrektorMapper;
import exambyte.infrastructure.persistence.entities.KorrektorEntity;
import exambyte.infrastructure.persistence.mapper.KorrektorMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class KorrektorMapperTest {

    @Test
    @DisplayName("KorrektorMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        KorrektorMapper korrektorMapper = new KorrektorMapperImpl();

        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(null)
                .name("Korrektor1")
                .build();

        // Act
        KorrektorEntity korrektorEntity = korrektorMapper.toEntity(korrektor);
        UUID entityFachId = korrektorEntity.getFachId();
        String entityName = korrektorEntity.getName();

        // Assert
        assertThat(korrektorEntity).isNotNull();
        assertThat(entityFachId).isEqualTo(korrektor.uuid());
        assertThat(entityName).isEqualTo("Korrektor1");
    }

    @Test
    @DisplayName("KorrektorMapper test 'toDomain'")
    public void test_02() {
        // Arrange
        KorrektorMapper korrektorMapper = new KorrektorMapperImpl();

        KorrektorEntity korrektorEntity = new KorrektorEntity.KorrektorEntityBuilder()
                .id(null)
                .fachId(null)
                .name("Korrektor2")
                .build();

        // Act
        Korrektor korrektor = korrektorMapper.toDomain(korrektorEntity);
        UUID korrektorFachId = korrektor.uuid();
        String korrektorName = korrektor.getName();

        // Assert
        assertThat(korrektor).isNotNull();
        assertThat(korrektorFachId).isEqualTo(korrektorEntity.getFachId());
        assertThat(korrektorName).isEqualTo("Korrektor2");
    }
}
