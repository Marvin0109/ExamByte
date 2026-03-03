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

class AntwortServiceTest {

    private final AntwortRepository antwortRepository = mock(AntwortRepository.class);
    private final AntwortService service = new AntwortServiceImpl(antwortRepository);

    @Test
    @DisplayName("gib null zurück wenn keine antwort gefunden wird nach studentID und questionID")
    void test_01() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();

        when(antwortRepository.findByStudentIdAndFrageId(studentId, frageId)).thenReturn(Optional.empty());

        // Act
        Antwort result = service.findByStudentAndFrage(studentId, frageId);

        // Assert
        assertNull(result);
        verify(antwortRepository).findByStudentIdAndFrageId(studentId, frageId);
    }
}