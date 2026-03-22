package exambyte.application.service.usecase;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.AnswerQueryService;
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
    private AnswerDTO answer;
    private ReviewDTO reviewDTO;

    private static final UUID STUDENT_UUID = UUID.randomUUID();

    @Mock
    private AnswerQueryService answerQueryService;

    @Mock
    private ReviewQueryService reviewQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reviewManagementService = new ReviewManagementServiceImpl(
                reviewQueryService,
                answerQueryService);

        answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                UUID.randomUUID(),
                STUDENT_UUID,
                LocalDateTime.of(2000, 1, 1, 0, 0));

        reviewDTO = new ReviewDTO(
                UUID.randomUUID(),
                answer.id(),
                UUID.randomUUID(),
                "Text",
                1);
    }

    @Test
    void getReviewCoverage_100Percent() {
        when(answerQueryService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer));
        when(reviewQueryService.getReviewByAnswerId(answer.id())).thenReturn(reviewDTO);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(100.0, result);
    }

    @Test
    void getReviewCoverage_50Percent() {
        AnswerDTO answerDTO2 = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                UUID.randomUUID(),
                STUDENT_UUID,
                LocalDateTime.of(2000, 1, 1, 0, 0));

        when(answerQueryService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer, answerDTO2));
        when(reviewQueryService.getReviewByAnswerId(answer.id())).thenReturn(null);
        when(reviewQueryService.getReviewByAnswerId(answerDTO2.id())).thenReturn(reviewDTO);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(50.0, result);
    }

    @Test
    void getReviewCoverage_0Percent() {
        when(answerQueryService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer));
        when(reviewQueryService.getReviewByAnswerId(answer.id())).thenReturn(null);

        double result = reviewManagementService.getReviewCoverage(STUDENT_UUID);

        assertEquals(0.0, result);
    }

    @Test
    void submitHasReview_true() {
        when(answerQueryService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer));
        when(reviewQueryService.getReviewByAnswerId(answer.id())).thenReturn(reviewDTO);

        boolean result = reviewManagementService.submitHasReview(UUID.randomUUID(), STUDENT_UUID);

        assertTrue(result);
    }

    @Test
    void submitHasReview_false() {
        when(answerQueryService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer));
        when(reviewQueryService.getReviewByAnswerId(answer.id())).thenReturn(null);

        boolean result = reviewManagementService.submitHasReview(UUID.randomUUID(), STUDENT_UUID);

        assertFalse(result);
    }
}
