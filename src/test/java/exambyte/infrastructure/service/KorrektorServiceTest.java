package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import exambyte.domain.service.KorrektorService;
import exambyte.infrastructure.NichtVorhandenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class KorrektorServiceTest {

    private final KorrektorRepository korrektorRepository = mock(KorrektorRepository.class);
    private final KorrektorService service = new KorrektorServiceImpl(korrektorRepository);

    @Test
    @DisplayName("Ein Korrektor kann mit der FachID geladen werden")
    void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        Korrektor korrektor = new Korrektor.KorrektorBuilder().fachId(fachId).build();

        when(korrektorRepository.findByFachId(fachId)).thenReturn(Optional.of(korrektor));

        // Act
        Korrektor result = service.getKorrektor(fachId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.uuid()).isEqualTo(korrektor.uuid());
        verify(korrektorRepository).findByFachId(fachId);
    }

    @Test
    @DisplayName("Ein Korrektor kann nicht gefunden werden")
    void test_02() {
        UUID fachId = UUID.randomUUID();
        when(korrektorRepository.findByFachId(fachId)).thenReturn(Optional.empty());
        assertThrows(NichtVorhandenException.class, () -> service.getKorrektor(fachId));
        verify(korrektorRepository).findByFachId(fachId);
    }

    @Test
    @DisplayName("Ein Korrektor kann erfolgreich gespeichert werden")
    void test_03() {
        // Arrange
        String name = "new_reviewer";

        // Act
        service.saveKorrektor(name);

        // Assert
        verify(korrektorRepository).save(any(Korrektor.class));
    }

    @Test
    @DisplayName("Ein Korrektor kann nach seinem Namen geladen werden")
    void test_04() {
        // Arrange
        String name = "new_reviewer";
        var Korrektor = new Korrektor.KorrektorBuilder().name(name).build();

        when(korrektorRepository.findByName(name)).thenReturn(Optional.of(Korrektor));

        // Act
        var result = service.getKorrektorByName(name);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get().getName()).isEqualTo(name);
    }
}
