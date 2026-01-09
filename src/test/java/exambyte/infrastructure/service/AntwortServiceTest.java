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
    @DisplayName("Exception wird geworfen falls FrageFachID null ist")
    void test_01() {
        assertThrows(IllegalArgumentException.class, () -> service.findByFrageFachId(null));
    }

    @Test
    @DisplayName("gib null zurück wenn keine antwort gefunden wird nach studentID und questionID")
    void test_02() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();

        when(antwortRepository.findByStudentFachIdAndFrageFachId(studentId, frageFachId)).thenReturn(Optional.empty());

        // Act
        Antwort result = service.findByStudentAndFrage(studentId, frageFachId);

        // Assert
        assertNull(result);
        verify(antwortRepository).findByStudentFachIdAndFrageFachId(studentId, frageFachId);
    }
}