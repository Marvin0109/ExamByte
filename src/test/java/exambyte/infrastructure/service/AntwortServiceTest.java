package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import exambyte.domain.service.AntwortService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AntwortServiceTest {

    private final AntwortRepository antwortRepository = mock(AntwortRepository.class);
    private final AntwortService service = new AntwortServiceImpl(antwortRepository);

    @Test
    @DisplayName("Finde erfolgreich eine Antwort nach einer FrageId und gib das Antwort-Objekt zurück")
    void test_01() {
        // Arrange
        UUID frageFachId = UUID.randomUUID();
        Antwort expectedAntwort = new Antwort.AntwortBuilder()
                .frageFachId(frageFachId)
                .antwortText("Test answer")
                .build();

        when(antwortRepository.findByFrageFachId(frageFachId)).thenReturn(expectedAntwort);

        // Act
        Antwort result = service.findByFrageFachId(frageFachId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAntwort.getFrageFachId(), result.getFrageFachId());
        assertEquals(expectedAntwort.getAntwortText(), result.getAntwortText());
        verify(antwortRepository).findByFrageFachId(frageFachId);
    }

    @Test
    @DisplayName("Exception wird geworfen falls FrageFachID null ist")
    void test_02() {
        assertThrows(IllegalArgumentException.class, () -> service.findByFrageFachId(null));
    }

    @Test
    @DisplayName("Finde Antwort nach studentID und questionID und gib antwort zurück")
    void test_04() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        Antwort expectedAntwort = new Antwort.AntwortBuilder()
                .frageFachId(frageFachId)
                .antwortText("Test answer")
                .build();

        when(antwortRepository.findByStudentFachIdAndFrageFachId(studentId, frageFachId)).thenReturn(Optional.of(expectedAntwort));

        // Act
        Antwort result = service.findByStudentAndFrage(studentId, frageFachId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAntwort.getFrageFachId(), result.getFrageFachId());
        assertEquals(expectedAntwort.getAntwortText(), result.getAntwortText());
        verify(antwortRepository).findByStudentFachIdAndFrageFachId(studentId, frageFachId);
    }

    @Test
    @DisplayName("gib null zurück wenn keine antwort gefunden wird nach studentID und questionID")
    void test_05() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();

        when(antwortRepository.findByStudentFachIdAndFrageFachId(studentId, frageFachId)).thenReturn(Optional.empty());

        // Act
        Antwort result = service.findByStudentAndFrage(studentId, frageFachId);

        // Assert
        assertNull(result);;
        verify(antwortRepository).findByStudentFachIdAndFrageFachId(studentId, frageFachId);
    }
}