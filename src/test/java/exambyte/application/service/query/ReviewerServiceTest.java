package exambyte.application.service.query;

import exambyte.application.exception.NotFoundException;
import exambyte.application.mapper.ReviewerDTOMapper;
import exambyte.domain.model.user.Reviewer;
import exambyte.domain.repository.ReviewerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReviewerServiceTest {

    private ReviewerService queryService;

    @Mock
    private ReviewerRepository repository;

    @Mock
    private ReviewerDTOMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryService = new ReviewerServiceImpl(repository, mapper);
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerFound() {
        Reviewer reviewer = new Reviewer.ReviewerBuilder().name("Auto reviewer").build();
        when(repository.findByName("Auto reviewer")).thenReturn(Optional.of(reviewer));
        queryService.saveAutomaticReviewer();
        verify(repository, never()).save(any());
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerNotFound() {
        when(repository.findByName("Auto reviewer")).thenReturn(Optional.empty());
        queryService.saveAutomaticReviewer();
        verify(repository).save(any(Reviewer.class));
    }

    @Test
    void getReviewer_notFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> queryService.getReviewerById(id));
        verify(repository).findById(id);
    }
}
