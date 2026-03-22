package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.repository.ReviewerRepository;
import exambyte.domain.service.ReviewerService;
import exambyte.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReviewerServiceTest {

    private final ReviewerRepository repository = mock(ReviewerRepository.class);
    private final ReviewerService service = new ReviewerServiceImpl(repository);

    @Test
    void getReviewer_notFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getReviewer(id));
        verify(repository).findById(id);
    }

    @Test
    void saveReviewer_success() {
        service.saveReviewer("Reviewer");
        verify(repository).save(any(Reviewer.class));
    }
}
