package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import exambyte.domain.service.KorrektorService;
import exambyte.infrastructure.NichtVorhandenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class KorrektorServiceTest {

    private final KorrektorRepository korrektorRepository = mock(KorrektorRepository.class);
    private final KorrektorService service = new KorrektorServiceImpl(korrektorRepository);

    @Test
    @DisplayName("Ein Korrektor kann nicht gefunden werden")
    void test_01() {
        UUID fachId = UUID.randomUUID();
        when(korrektorRepository.findByFachId(fachId)).thenReturn(Optional.empty());

        assertThrows(NichtVorhandenException.class, () -> service.getKorrektor(fachId));
        verify(korrektorRepository).findByFachId(fachId);
    }

    @Test
    @DisplayName("Der Automatische Korrektur wird erfolgreich gespeichert")
    void test_02() {
        service.saveKorrektor("Automatischer Korrektor");
        verify(korrektorRepository).save(any(Korrektor.class));
    }
}
