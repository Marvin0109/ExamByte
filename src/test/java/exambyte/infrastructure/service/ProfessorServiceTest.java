package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import exambyte.domain.service.ProfessorService;
import exambyte.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfessorServiceTest {

    private final ProfessorRepository repository = mock(ProfessorRepository.class);
    private final ProfessorService service = new ProfessorServiceImpl(repository);

    @Test
    void getProfessor_success() {
        // Arrange
        var profId = UUID.randomUUID();
        Professor prof = new Professor.ProfessorBuilder().id(profId).build();

        when(repository.findById(profId)).thenReturn(Optional.of(prof));

        // Act
        var result = service.getProfessor(profId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(profId);
        verify(repository).findById(profId);
    }

    @Test
    void getProfessor_notFound() {
        UUID profID = UUID.randomUUID();
        when(repository.findById(profID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getProfessor(profID));
        verify(repository).findById(profID);
    }

    @Test
    void getProfessorByName_success() {
        // Arrange
        String name = "Prof123";
        var prof = new Professor.ProfessorBuilder().name(name).build();

        when(repository.findByName(name)).thenReturn(Optional.of(prof));

        // Act
        var result = service.getProfessorByName(name);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(name);
        verify(repository).findByName(name);
    }
}
