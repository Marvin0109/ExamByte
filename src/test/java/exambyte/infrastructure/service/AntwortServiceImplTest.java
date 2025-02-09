package exambyte.infrastructure.service;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class AntwortServiceImplTest {

    @Autowired
    private AntwortServiceImpl antwortService;

    @MockBean
    private AntwortRepository antwortRepository;

    @Test
    @DisplayName("Finde erfolgreich eine Antwort nach einer FrageId und gib das Antwort-Objekt zurück")
    public void test_01() {
        // Arrange
        UUID frageFachId = UUID.randomUUID();
        Antwort expectedAntwort = new Antwort.AntwortBuilder()
                .frageFachId(frageFachId)
                .antwortText("Test answer")
                .build();

        when(antwortRepository.findByFrageFachId(frageFachId)).thenReturn(expectedAntwort);

        // Act
        Antwort result = antwortService.findByFrageFachId(frageFachId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAntwort.getFrageFachId(), result.getFrageFachId());
        assertEquals(expectedAntwort.getAntwortText(), result.getAntwortText());
        verify(antwortRepository).findByFrageFachId(frageFachId);
    }

    @Test
    @DisplayName("Gib null UUID zurück wenn man nach question ID sucht")
    public void test_02() {
        // Arrange
        UUID nullFrageFachId = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            antwortService.findByFrageFachId(nullFrageFachId);
        });

        verify(antwortRepository, never()).findByFrageFachId(any());
    }
    @Test
    @DisplayName("Finde Antwort nach studentID und questionID und gib antwort zurück")
    public void test_03() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        Antwort expectedAntwort = new Antwort.AntwortBuilder()
                .frageFachId(frageFachId)
                .antwortText("Test answer")
                .build();

        when(antwortRepository.findByStudentFachIdAndFrageFachId(studentId, frageFachId)).thenReturn(Optional.of(expectedAntwort));

        // Act
        Antwort result = antwortService.findByStudentAndFrage(studentId, frageFachId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAntwort.getFrageFachId(), result.getFrageFachId());
        assertEquals(expectedAntwort.getAntwortText(), result.getAntwortText());
        verify(antwortRepository).findByStudentFachIdAndFrageFachId(studentId, frageFachId);
    }
    @Test
    @DisplayName("gib null zurück wenn keine antwort gefunden wird nach studentID und questionID")
    public void test_04() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();

        when(antwortRepository.findByStudentFachIdAndFrageFachId(studentId, frageFachId)).thenReturn(Optional.empty());

        // Act
        Antwort result = antwortService.findByStudentAndFrage(studentId, frageFachId);

        // Assert
        assertNull(result);
        verify(antwortRepository).findByStudentFachIdAndFrageFachId(studentId, frageFachId);

    }
}