package exambyte.application.service.review;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.AntwortQueryService;
import exambyte.application.service.query.ReviewQueryService;
import exambyte.application.service.usecase.ReviewManagementService;
import exambyte.application.service.usecase.ReviewManagementServiceImpl;
import exambyte.domain.mapper.ReviewDTOMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.service.KorrektorService;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewManagementServiceTest {

    private ReviewManagementService reviewManagementService;
    private Korrektor korrektor;
    private AntwortDTO antwortDTO;
    private Review review;
    private ReviewDTO reviewDTO;

    private static final UUID STUDENT_UUID = UUID.randomUUID();

    @Mock
    private KorrektorService korrektorService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewDTOMapper reviewDTOMapper;

    @Mock
    private AntwortQueryService antwortQueryService;

    @Mock
    private ReviewQueryService reviewQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reviewManagementService = new ReviewManagementServiceImpl(
                reviewQueryService,
                antwortQueryService);

        korrektor = new Korrektor.KorrektorBuilder()
                .fachId(UUID.randomUUID())
                .name("Korrektor")
                .build();

        antwortDTO = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                UUID.randomUUID(),
                STUDENT_UUID,
                LocalDateTime.of(2000, 1, 1, 0, 0));

        review = new Review.ReviewBuilder()
                .fachId(UUID.randomUUID())
                .bewertung("Bewertung")
                .punkte(1)
                .antwortFachId(antwortDTO.fachId())
                .korrektorFachId(UUID.randomUUID())
                .build();

        reviewDTO = new ReviewDTO(
                review.getFachId(),
                review.getAntwortFachId(),
                review.getKorrektorFachId(),
                "Bewertung",
                1);
    }

    @Test
    void saveAutomaticReviewer_notFound() {
        when(korrektorService.getKorrektorByName("Automatischer Korrektor")).thenReturn(Optional.empty());

        reviewManagementService.saveAutomaticReviewer();

        verify(korrektorService).saveKorrektor("Automatischer Korrektor");
    }

    @Test
    void saveAutomaticReview_found() {
        when(korrektorService.getKorrektorByName("Automatischer Korrektor")).thenReturn(Optional.of(korrektor));

        reviewManagementService.saveAutomaticReviewer();

        verify(korrektorService, never()).saveKorrektor("Automatischer Korrektor");
    }

    @Test
    void getReviewCoverage_100Percent() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewService.getReviewByAntwortFachId(antwortDTO.fachId())).thenReturn(review);
        when(reviewDTOMapper.toDTO(review)).thenReturn(reviewDTO);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(100.0, result);
    }

    @Test
    void getReviewCoverage_50Percent() {
        AntwortDTO antwortDTO2 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                UUID.randomUUID(),
                STUDENT_UUID,
                LocalDateTime.of(2000, 1, 1, 0, 0));

        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO, antwortDTO2));
        when(reviewService.getReviewByAntwortFachId(antwortDTO.fachId())).thenReturn(null);
        when(reviewService.getReviewByAntwortFachId(antwortDTO2.fachId())).thenReturn(review);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(50.0, result);
    }

    @Test
    void getReviewCoverage_0Percent() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewService.getReviewByAntwortFachId(antwortDTO.fachId())).thenReturn(null);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(0.0, result);
        verify(reviewDTOMapper, never()).toDTO(any());
    }

    @Test
    void submitHasReview_true() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewService.getReviewByAntwortFachId(antwortDTO.fachId())).thenReturn(review);

        boolean result = reviewManagementService.submitHasReview(UUID.randomUUID(), STUDENT_UUID);

        assertTrue(result);
    }

    @Test
    void submitHasReview_false() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewService.getReviewByAntwortFachId(antwortDTO.fachId())).thenReturn(null);

        boolean result = reviewManagementService.submitHasReview(UUID.randomUUID(), STUDENT_UUID);

        assertFalse(result);
    }

    @Test
    void createReviewWithCorrectParams() {
        reviewQueryService.createReview(
                "Bewertung",
                1,
                antwortDTO.fachId(),
                review.getKorrektorFachId());

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        verify(reviewService).addReview(captor.capture());

        Review reviewCaptured = captor.getValue();
        assertEquals(reviewCaptured.getAntwortFachId(), review.getAntwortFachId());
        assertEquals(reviewCaptured.getKorrektorFachId(), review.getKorrektorFachId());
        assertEquals("Bewertung", reviewCaptured.getBewertung());
        assertEquals(1, reviewCaptured.getPunkte());
    }
}
