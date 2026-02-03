package exambyte.application.service.usecase;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.service.ReviewService;
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

    private static final UUID FRAGE_1_ID = UUID.randomUUID();
    private static final UUID FRAGE_2_ID = UUID.randomUUID();

    private static final UUID ANTWORT_1_ID = UUID.randomUUID();
    private static final UUID ANTWORT_2_ID = UUID.randomUUID();

    private static final UUID AUTOMATIC_REVIEWER = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private FrageDTO frage1;
    private FrageDTO frage2;

    private AntwortDTO antwort1;
    private AntwortDTO antwort2;

    private Review review1;
    private Review review2;

    private static final LocalDateTime ANTWORT_TIME =
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

        frage1 = new FrageDTO(
                FRAGE_1_ID,
                "Frage",
                5,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.FREITEXT);

        frage2 = new FrageDTO(
                FRAGE_2_ID,
                "Frage",
                3,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.SC);

        antwort1 = new AntwortDTO(
                ANTWORT_1_ID,
                "Antwort",
                FRAGE_1_ID,
                UUID.randomUUID(),
                ANTWORT_TIME);

        antwort2 = new AntwortDTO(
                ANTWORT_2_ID,
                "Antwort",
                FRAGE_2_ID,
                UUID.randomUUID(),
                ANTWORT_TIME);

        review1 = new Review.ReviewBuilder()
                .korrektorFachId(UUID.randomUUID())
                .bewertung("Bewertung")
                .punkte(5)
                .antwortFachId(ANTWORT_1_ID)
                .build();

        review2 = new Review.ReviewBuilder()
                .korrektorFachId(AUTOMATIC_REVIEWER)
                .bewertung("Bewertung")
                .punkte(3)
                .antwortFachId(ANTWORT_2_ID)
                .build();
    }

    @Test
    void shouldCountPoints_resultTimeReached() {
        when(reviewService.getReviewByAntwortFachId(ANTWORT_1_ID))
                .thenReturn(review1);
        when(reviewService.getReviewByAntwortFachId(ANTWORT_2_ID))
                .thenReturn(review2);

        LocalDateTime resultTime =
                LocalDateTime.of(2026, 1, 1, 9, 0);

        double punkte = scoringService.berechneErreichtePunkte(
                List.of(antwort1, antwort2),
                Map.of(FRAGE_1_ID, frage1, FRAGE_2_ID, frage2),
                resultTime
        );

        assertEquals(8.0, punkte);
    }

    @Test
    void shouldCountPoints_resultTimeNotReachedYet() {
        when(reviewService.getReviewByAntwortFachId(ANTWORT_1_ID))
                .thenReturn(review1);
        when(reviewService.getReviewByAntwortFachId(ANTWORT_2_ID))
                .thenReturn(review2);

        LocalDateTime resultTime =
                LocalDateTime.of(2026, 1, 1, 11, 0);

        double punkte = scoringService.berechneErreichtePunkte(
                List.of(antwort1, antwort2),
                Map.of(FRAGE_1_ID, frage1, FRAGE_2_ID, frage2),
                resultTime
        );

        assertEquals(3.0, punkte);
    }

}
