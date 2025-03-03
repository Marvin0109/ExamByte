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

public class ProfessorDTOMapperTest {

    private final ProfessorDTOMapper mapper = new ProfessorDTOMapperImpl();

    @Test
    @DisplayName("Test ProfessorDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();

        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(fachId)
                .name("Prof name")
                .build();

        // Act
        ProfessorDTO professorDTO = mapper.toDTO(professor);

        // Assert
        assertNull(professorDTO.id());
        assertEquals(professorDTO.fachId(), fachId);
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
        UUID fachId1 = UUID.randomUUID();
        UUID fachId2 = UUID.randomUUID();

        Professor professor1 = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(fachId1)
                .name("Prof 1")
                .build();

        Professor professor2 = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(fachId2)
                .name("Prof 2")
                .build();

        // Act
        List<Professor> professoren = Arrays.asList(professor1, professor2);

        // Assert
        assertEquals(2, professoren.size());
        assertThat(professoren.getFirst().getName()).isEqualTo("Prof 1");
        assertThat(professoren.getFirst().uuid()).isEqualTo(fachId1);
        assertThat(professoren.getLast().getName()).isEqualTo("Prof 2");
        assertThat(professoren.getLast().uuid()).isEqualTo(fachId2);
    }
}
