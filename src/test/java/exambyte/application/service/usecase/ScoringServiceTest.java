package exambyte.application.service.usecase;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ScoringServiceTest {

    private ScoringService scoringService;

    private static final UUID QUESTION_1_ID = UUID.randomUUID();
    private static final UUID QUESTION_2_ID = UUID.randomUUID();

    private static final UUID ANSWER_1_ID = UUID.randomUUID();
    private static final UUID ANSWER_2_ID = UUID.randomUUID();

    private static final UUID AUTOMATIC_REVIEWER = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private QuestionDTO question1;
    private QuestionDTO question2;

    private AnswerDTO answer1;
    private AnswerDTO answer2;

    private ReviewDTO review1;
    private ReviewDTO review2;

    private static final LocalDateTime SUBMIT_TIME =
            LocalDateTime.of(2026, 1, 1, 9, 0);

    @Mock
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneId.of("UTC")
        );

        scoringService = new ScoringServiceImpl(reviewService, fixedClock);

        question1 = new QuestionDTO(
                QUESTION_1_ID,
                "Question",
                5,
                UUID.randomUUID(),
                QuestionTypeDTO.FREE_RESPONSE);

        question2 = new QuestionDTO(
                QUESTION_2_ID,
                "Question",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.SC);

        answer1 = new AnswerDTO(
                ANSWER_1_ID,
                "Answer",
                QUESTION_1_ID,
                UUID.randomUUID(),
                SUBMIT_TIME);

        answer2 = new AnswerDTO(
                ANSWER_2_ID,
                "Answer",
                QUESTION_2_ID,
                UUID.randomUUID(),
                SUBMIT_TIME);

        review1 = new ReviewDTO(
                null,
                ANSWER_1_ID,
                UUID.randomUUID(),
                "Text",
                5
        );

        review2 = new ReviewDTO(
                null,
                ANSWER_2_ID,
                AUTOMATIC_REVIEWER,
                "Text",
                3
        );
    }

    @Test
    void shouldCountPoints_resultTimeReached() {
        when(reviewService.getReviewByAnswerId(ANSWER_1_ID))
                .thenReturn(review1);
        when(reviewService.getReviewByAnswerId(ANSWER_2_ID))
                .thenReturn(review2);

        LocalDateTime resultTime =
                LocalDateTime.of(2026, 1, 1, 9, 0);

        double result = scoringService.accumulatedPoints(
                List.of(answer1, answer2),
                Map.of(QUESTION_1_ID, question1, QUESTION_2_ID, question2),
                resultTime
        );

        assertEquals(8.0, result);
    }

    @Test
    void shouldCountPoints_resultTimeNotReachedYet() {
        when(reviewService.getReviewByAnswerId(ANSWER_1_ID))
                .thenReturn(review1);
        when(reviewService.getReviewByAnswerId(ANSWER_2_ID))
                .thenReturn(review2);

        LocalDateTime resultTime =
                LocalDateTime.of(2026, 1, 1, 11, 0);

        double result = scoringService.accumulatedPoints(
                List.of(answer1, answer2),
                Map.of(QUESTION_1_ID, question1, QUESTION_2_ID, question2),
                resultTime
        );

        assertEquals(3.0, result);
    }

}
