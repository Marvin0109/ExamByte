package exambyte.application.service.query;

import exambyte.application.mapper.ReviewerDTOMapper;
import exambyte.domain.model.user.Reviewer;
import exambyte.domain.service.ReviewerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ReviewerQueryServiceTest {

    private ReviewerQueryService queryService;

    @Mock
    private ReviewerService service;

    @Mock
    private ReviewerDTOMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryService = new ReviewerQueryServiceImpl(service, mapper);
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerFound() {
        Reviewer reviewer = new Reviewer.ReviewerBuilder().name("Auto reviewer").build();
        when(service.getReviewerByName("Auto reviewer")).thenReturn(Optional.of(reviewer));
        queryService.saveAutomaticReviewer();
        verify(service, never()).saveReviewer("Auto reviewer");
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerNotFound() {
        when(service.getReviewerByName("Auto reviewer")).thenReturn(Optional.empty());
        queryService.saveAutomaticReviewer();
        verify(service).saveReviewer("Auto reviewer");
    }
}
