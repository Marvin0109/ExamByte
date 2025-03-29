package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import exambyte.domain.service.ProfessorService;
import exambyte.infrastructure.NichtVorhandenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProfessorServiceTest {

    private final ProfessorRepository profRepo = mock(ProfessorRepository.class);
    private final ProfessorService service = new ProfessorServiceImpl(profRepo);

    @Test
    @DisplayName("Ein Professor kann geladen werden mit profFachID")
    void test_01() {
        // Arrange
        var profFachId = UUID.randomUUID();
        Professor prof = new Professor.ProfessorBuilder().fachId(profFachId).build();

        when(profRepo.findByFachId(profFachId)).thenReturn(Optional.of(prof));

        // Act
        var result = service.getProfessor(profFachId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.uuid()).isEqualTo(profFachId);
        verify(profRepo).findByFachId(profFachId);
    }

    @Test
    @DisplayName("Ein Professor wurde nicht gefunden")
    void test_02() {
        when(profRepo.findByFachId(any())).thenReturn(Optional.empty());
        assertThrows(NichtVorhandenException.class, () -> service.getProfessor(any()));
        verify(profRepo).findByFachId(any());
    }

    @Test
    @DisplayName("Ein Professor kann gespeichert werden")
    void test_03() {
        // Arrange
        String name = "Prof123";

        // Act
        service.saveProfessor(name);

        // Assert
        verify(profRepo).save(any(Professor.class));
    }

    @Test
    @DisplayName("Ein Professor kann nach seinem Namen gefunden werden")
    void test_04() {
        // Arrange
        String name = "Prof123";
        var prof = new Professor.ProfessorBuilder().name(name).build();

        when(profRepo.findByName(name)).thenReturn(Optional.of(prof));

        // Act
        var result = service.getProfessorByName(name);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get().getName()).isEqualTo(name);
        verify(profRepo).findByName(name);
    }

    @Test
    @DisplayName("Eine profFachID kann nach dem Professor Namen geladen werden")
    void test_05() {
        // Arrange
        String name = "Prof123";
        var profFachID = UUID.randomUUID();

        when(profRepo.findFachIdByName(name)).thenReturn(Optional.of(profFachID));

        // Act
        var result = service.getProfessorFachId(name);

        assertThat(result).isNotNull();
        assertThat(result.get().toString()).isEqualTo(profFachID.toString());
        verify(profRepo).findFachIdByName(name);
    }
}
