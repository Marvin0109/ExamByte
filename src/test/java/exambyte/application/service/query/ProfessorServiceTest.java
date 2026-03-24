package exambyte.application.service.query;

import exambyte.application.dto.ProfessorDTO;
import exambyte.application.exception.NotFoundException;
import exambyte.application.mapper.ProfessorDTOMapper;
import exambyte.domain.model.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfessorServiceTest {

    private final ProfessorDTOMapper mapper = mock(ProfessorDTOMapper.class);
    private final ProfessorRepository repository = mock(ProfessorRepository.class);
    private final ProfessorService service = new ProfessorServiceImpl(repository, mapper);

    @Test
    void getProfessor_success() {
        // Arrange
        var profId = UUID.randomUUID();
        ProfessorDTO professorDTO = new ProfessorDTO(profId, "");
        Professor prof = new Professor.ProfessorBuilder().id(profId).build();

        when(repository.findById(profId)).thenReturn(Optional.of(prof));
        when(mapper.toDTO(prof)).thenReturn(professorDTO);

        // Act
        var result = service.getProfessorById(profId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(profId);
        verify(repository).findById(profId);
    }

    @Test
    void getProfessor_notFound() {
        UUID profID = UUID.randomUUID();
        when(repository.findById(profID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getProfessorById(profID));
        verify(repository).findById(profID);
    }

    @Test
    void getProfessorByName_success() {
        // Arrange
        String name = "Prof123";
        var prof = new Professor.ProfessorBuilder().name(name).build();
        var dto = new ProfessorDTO(null, name);

        when(repository.findByName(name)).thenReturn(Optional.of(prof));
        when(mapper.toDTO(prof)).thenReturn(dto);

        // Act
        var result = service.getProfessorByName(name);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(name);
        verify(repository).findByName(name);
    }
}
