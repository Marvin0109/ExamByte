package exambyte.domain.aggregate.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfessorTest {
    // Create Professor instance with all valid parameters using builder pattern
    @Test
    @DisplayName("create_professor_with_valid_parameters")
    public void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        String name = "John Doe";

        Professor professor = new Professor.ProfessorBuilder()
                .id(id)
                .fachId(fachId)
                .name(name)
                .build();

        assertEquals(id, professor.getId());
        assertEquals(fachId, professor.uuid());
        assertEquals(name, professor.getName());
    }

    // Create Professor with null fachId generates new random UUID
    @Test
    @DisplayName("create_professor_with_null_fachid_generates_uuid")
    public void test_02() {
        Long id = 1L;
        String name = "John Doe";

        Professor professor = new Professor.ProfessorBuilder()
                .id(id)
                .fachId(null)
                .name(name)
                .build();

        assertNotNull(professor.uuid());
        assertTrue(professor.uuid() instanceof UUID);
    }
}
