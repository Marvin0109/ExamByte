package exambyte.domain.aggregate.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KorrektorTest {
    // Builder creates Korrektor instance with all fields populated
    @Test
    @DisplayName("test_builder_creates_korrektor_with_all_fields")
    public void test_01() {
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

    // Constructor generates random UUID when fachId is null
    @Test
    @DisplayName("test_constructor_generates_random_uuid_for_null_fachid")
    public void test_02() {
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
