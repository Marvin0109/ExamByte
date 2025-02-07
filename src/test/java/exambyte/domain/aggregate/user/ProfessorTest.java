package exambyte.domain.aggregate.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfessorTest {

    @Test
    @DisplayName("ProfessorBuilder Test")
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

    @Test
    @DisplayName("ProfessorBuilder Test mit null Feldern")
    public void test_02() {
        Long id = 1L;
        String name = "John Doe";

        Professor professor = new Professor.ProfessorBuilder()
                .id(id)
                .fachId(null)
                .name(name)
                .build();

        assertNotNull(professor.uuid());
        Assertions.assertNotNull(professor.uuid());
    }
}
