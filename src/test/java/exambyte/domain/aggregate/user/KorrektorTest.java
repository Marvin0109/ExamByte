package exambyte.domain.aggregate.user;

import exambyte.domain.model.aggregate.user.Korrektor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KorrektorTest {

    @Test
    @DisplayName("KorrektorBuilder Test")
    void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        String name = "Test Korrektor";

        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(id)
                .fachId(fachId)
                .name(name)
                .build();

        assertEquals(id, korrektor.getId());
        assertEquals(fachId, korrektor.uuid());
        assertEquals(name, korrektor.getName());
    }

    @Test
    @DisplayName("KorrektorBuilder Test mit null Feldern")
    void test_02() {
        Long id = 1L;
        String name = "Test Korrektor";

        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(id)
                .fachId(null)
                .name(name)
                .build();

        assertNotNull(korrektor.uuid());
        assertEquals(UUID.class, korrektor.uuid().getClass());
    }
}
