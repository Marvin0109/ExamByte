package exambyte.infrastructure.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.mapper.ProfessorDTOMapper;
import exambyte.domain.model.aggregate.user.Professor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorDTOMapperTest {

    private final ProfessorDTOMapper mapper = new ProfessorDTOMapperImpl();

    @Test
    @DisplayName("Test ProfessorDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();

        Professor professor = new Professor.ProfessorBuilder()
                .id(id)
                .name("Prof name")
                .build();

        // Act
        ProfessorDTO professorDTO = mapper.toDTO(professor);

        // Assert
        assertEquals(professorDTO.id(), id);
        assertEquals("Prof name", professorDTO.name());
    }

    @Test
    @DisplayName("test_null_professor_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toProfessorDTOList Test")
    void test_03() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Professor professor1 = new Professor.ProfessorBuilder()
                .id(id1)
                .name("Prof 1")
                .build();

        Professor professor2 = new Professor.ProfessorBuilder()
                .id(id2)
                .name("Prof 2")
                .build();

        // Act
        List<Professor> professoren = Arrays.asList(professor1, professor2);

        // Assert
        assertEquals(2, professoren.size());
        assertThat(professoren.getFirst().getName()).isEqualTo("Prof 1");
        assertThat(professoren.getFirst().id()).isEqualTo(id1);
        assertThat(professoren.getLast().getName()).isEqualTo("Prof 2");
        assertThat(professoren.getLast().id()).isEqualTo(id2);
    }
}
