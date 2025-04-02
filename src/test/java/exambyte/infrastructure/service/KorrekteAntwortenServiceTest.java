package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.repository.KorrekteAntwortenRepository;
import exambyte.domain.service.KorrekteAntwortenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class KorrekteAntwortenServiceTest {

    private final KorrekteAntwortenRepository mockRepo = mock(KorrekteAntwortenRepository.class);
    private final KorrekteAntwortenService service = new KorrekteAntwortenServiceImpl(mockRepo);

    @Test
    @DisplayName("Das hinzufügen von Lösungen ist erfolgreich")
    void test_01() {
        // Arrange
        KorrekteAntworten domain = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .build();

        // Act
        service.addKorrekteAntwort(domain);

        // Assert
        verify(mockRepo).save(domain);
    }
}
