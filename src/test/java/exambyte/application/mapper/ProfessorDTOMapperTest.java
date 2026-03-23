package exambyte.application.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.model.user.Professor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorDTOMapperTest {

    private final ProfessorDTOMapper mapper = new ProfessorDTOMapperImpl();

    @Test
    void toDTO() {
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
    void toProfessorDTOList() {
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
        List<Professor> profList = Arrays.asList(professor1, professor2);

        // Assert
        assertEquals(2, profList.size());
        assertThat(profList.getFirst().getName()).isEqualTo("Prof 1");
        assertThat(profList.getFirst().id()).isEqualTo(id1);
        assertThat(profList.getLast().getName()).isEqualTo("Prof 2");
        assertThat(profList.getLast().id()).isEqualTo(id2);
    }
}
