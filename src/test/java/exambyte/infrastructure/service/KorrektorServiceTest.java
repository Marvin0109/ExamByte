package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.repository.ReviewerRepository;
import exambyte.domain.service.ReviewerService;
import exambyte.infrastructure.exceptions.NichtVorhandenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReviewerServiceTest {

    private final ReviewerRepository reviewerRepository = mock(ReviewerRepository.class);
    private final ReviewerService service = new ReviewerServiceImpl(reviewerRepository);

    @Test
    @DisplayName("Ein Reviewer kann nicht gefunden werden")
    void test_01() {
        UUID id = UUID.randomUUID();
        when(reviewerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NichtVorhandenException.class, () -> service.getReviewer(id));
        verify(reviewerRepository).findById(id);
    }

    @Test
    @DisplayName("Der Automatische Korrektur wird erfolgreich gespeichert")
    void test_02() {
        service.saveReviewer("Automatischer Reviewer");
        verify(reviewerRepository).save(any(Reviewer.class));
    }
}
