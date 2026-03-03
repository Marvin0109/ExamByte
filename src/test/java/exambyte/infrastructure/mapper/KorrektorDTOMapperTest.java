package exambyte.infrastructure.mapper;

import exambyte.application.dto.KorrektorDTO;
import exambyte.domain.mapper.KorrektorDTOMapper;
import exambyte.domain.model.aggregate.user.Korrektor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KorrektorDTOMapperTest {

    private final KorrektorDTOMapper mapper = new KorrektorDTOMapperImpl();

    @Test
    @DisplayName("Test KorrektorDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();

        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(id)
                .name("Korrektorname")
                .build();

        // Act
        KorrektorDTO dto = mapper.toDTO(korrektor);

        // Assert
        assertEquals(id, dto.id());
        assertEquals("Korrektorname", dto.name());
    }

    @Test
    @DisplayName("test_null_korrektor_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toKorrektorDTOList Test")
    void test_03() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Korrektor korrektor1 = new Korrektor.KorrektorBuilder()
                .id(id1)
                .name("Korrektor 1")
                .build();

        Korrektor korrektor2 = new Korrektor.KorrektorBuilder()
                .id(id2)
                .name("Korrektor 2")
                .build();

        List<Korrektor> korrektoren = Arrays.asList(korrektor1, korrektor2);

        // Act
        List<KorrektorDTO> korrektorDTOList = mapper.toKorrektorDTOList(korrektoren);

        // Assert
        assertEquals(2, korrektorDTOList.size());
        assertThat(korrektorDTOList.getFirst().name()).isEqualTo("Korrektor 1");
        assertThat(korrektorDTOList.getFirst().id()).isEqualTo(id1);
        assertThat(korrektorDTOList.getLast().name()).isEqualTo("Korrektor 2");
        assertThat(korrektorDTOList.getLast().id()).isEqualTo(id2);
    }
}
