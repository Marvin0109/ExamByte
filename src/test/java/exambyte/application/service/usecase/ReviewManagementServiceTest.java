package exambyte.application.service.usecase;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.AntwortQueryService;
import exambyte.application.service.query.ReviewQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewManagementServiceTest {

    private ReviewManagementService reviewManagementService;
    private AntwortDTO antwortDTO;
    private ReviewDTO reviewDTO;

    private static final UUID STUDENT_UUID = UUID.randomUUID();

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

        antwortDTO = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                UUID.randomUUID(),
                STUDENT_UUID,
                LocalDateTime.of(2000, 1, 1, 0, 0));

        reviewDTO = new ReviewDTO(
                UUID.randomUUID(),
                antwortDTO.id(),
                UUID.randomUUID(),
                "Bewertung",
                1);
    }

    @Test
    void getReviewCoverage_100Percent() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewQueryService.getReviewByAntwortId(antwortDTO.id())).thenReturn(reviewDTO);

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
        when(reviewQueryService.getReviewByAntwortId(antwortDTO.id())).thenReturn(null);
        when(reviewQueryService.getReviewByAntwortId(antwortDTO2.id())).thenReturn(reviewDTO);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(50.0, result);
    }

    @Test
    void getReviewCoverage_0Percent() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewQueryService.getReviewByAntwortId(antwortDTO.id())).thenReturn(null);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(0.0, result);
    }

    @Test
    void submitHasReview_true() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewQueryService.getReviewByAntwortId(antwortDTO.id())).thenReturn(reviewDTO);

        boolean result = reviewManagementService.submitHasReview(UUID.randomUUID(), STUDENT_UUID);

        assertTrue(result);
    }

    @Test
    void submitHasReview_false() {
        when(antwortQueryService.getFreitextAntwortenForExam(any())).thenReturn(List.of(antwortDTO));
        when(reviewQueryService.getReviewByAntwortId(antwortDTO.id())).thenReturn(null);

        boolean result = reviewManagementService.submitHasReview(UUID.randomUUID(), STUDENT_UUID);

        assertFalse(result);
    }
}
