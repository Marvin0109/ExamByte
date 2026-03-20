package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.mapper.ReviewDTOMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReviewQueryServiceTest {

    private ReviewQueryService reviewQueryService;

    private Review review;
    private static final UUID ANSWER_ID = UUID.randomUUID();
    private static final UUID REVIEWER_ID = UUID.randomUUID();

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewDTOMapper reviewDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewQueryService = new ReviewQueryServiceImpl(reviewService, reviewDTOMapper);

        review = new Review.ReviewBuilder()
                .answerId(ANSWER_ID)
                .reviewerId(REVIEWER_ID)
                .bewertung("Bewertung")
                .punkte(1)
                .build();
    }

    @Test
    void createReview_withCorrectParams_noExistingReview() {
        // Arrange
        when(reviewDTOMapper.toDomain(any())).thenReturn(review);
        when(reviewService.getReviewByAnswerId(ANSWER_ID)).thenReturn(null);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        // Act
        reviewQueryService.createReview("Bewertung", 1, ANSWER_ID, REVIEWER_ID);

        // Assert
        verify(reviewService).addReview(captor.capture());
        Review result = captor.getValue();

        assertThat(result.getAnswerId()).isEqualTo(ANSWER_ID);
        assertThat(result.getReviewerId()).isEqualTo(REVIEWER_ID);
        assertThat(result.getId()).isNull();
    }

    @Test
    void createReview_withCorrectParams_existingReview() {
        // Arrange
        when(reviewDTOMapper.toDomain(any())).thenReturn(review);
        when(reviewService.getReviewByAnswerId(ANSWER_ID)).thenReturn(review);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        // Act
        reviewQueryService.createReview("Bewertung", 1, ANSWER_ID, REVIEWER_ID);

        // Assert
        verify(reviewService).addReview(captor.capture());
        Review result = captor.getValue();

        assertThat(result.getAnswerId()).isEqualTo(ANSWER_ID);
        assertThat(result.getReviewerId()).isEqualTo(REVIEWER_ID);
        assertThat(result.getId()).isEqualTo(review.getId());
    }

    @Test
    void getReviewByAntwortId_success() {
        ReviewDTO reviewDTO = new ReviewDTO(
                review.getId(),
                ANSWER_ID,
                REVIEWER_ID,
                review.getBewertung(),
                review.getPunkte());

        when(reviewService.getReviewByAnswerId(any())).thenReturn(review);
        when(reviewDTOMapper.toDTO(review)).thenReturn(reviewDTO);

        ReviewDTO result = reviewQueryService.getReviewByAnswerId(ANSWER_ID);

        assertNotNull(result);
    }

    @Test
    void getReviewByAntwortId_notFound() {
        when(reviewService.getReviewByAnswerId(any())).thenReturn(null);
        ReviewDTO result = reviewQueryService.getReviewByAnswerId(ANSWER_ID);
        assertNull(result);
    }
}
