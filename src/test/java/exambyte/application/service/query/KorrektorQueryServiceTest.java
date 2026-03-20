package exambyte.application.service.query;

import exambyte.domain.mapper.ReviewerDTOMapper;
import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.service.ReviewerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ReviewerQueryServiceTest {

    private ReviewerQueryService reviewerQueryService;

    @Mock
    private ReviewerService reviewerService;

    @Mock
    private ReviewerDTOMapper reviewerDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewerQueryService = new ReviewerQueryServiceImpl(reviewerService, reviewerDTOMapper);
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerFound() {
        Reviewer reviewer = new Reviewer.ReviewerBuilder().name("Auto reviewer").build();
        when(reviewerService.getReviewerByName("Auto reviewer")).thenReturn(Optional.of(reviewer));
        reviewerQueryService.saveAutomaticReviewer();
        verify(reviewerService, never()).saveReviewer("Auto reviewer");
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerNotFound() {
        when(reviewerService.getReviewerByName("Auto reviewer")).thenReturn(Optional.empty());
        reviewerQueryService.saveAutomaticReviewer();
        verify(reviewerService).saveReviewer("Auto reviewer");
    }
}
