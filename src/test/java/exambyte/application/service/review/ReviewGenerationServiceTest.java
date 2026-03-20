package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.service.KorrekteAntwortenService;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ReviewGenerationServiceTest {

    private ReviewGenerationService reviewGenerationService;

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final LocalDateTime TIME = LocalDateTime.of(2000, 1, 1, 0, 0);

    private FrageDTO frageDTOMC;
    private AntwortDTO antwortDTOMC;

    private FrageDTO frageDTOSC;
    private AntwortDTO antwortDTOSC;

    private ReviewDTO reviewDTOMC;
    private ReviewDTO reviewDTOSC;

    @Mock
    private AutomaticReviewService automaticReviewService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private KorrekteAntwortenService korrekteAntwortenService;

    @Mock
    private KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewGenerationService = new ReviewGenerationServiceImpl(
                automaticReviewService,
                reviewService,
                korrekteAntwortenService,
                korrekteAntwortenDTOMapper);

        frageDTOMC = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                2,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        antwortDTOMC = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frageDTOMC.id(),
                STUDENT_ID,
                TIME);

        frageDTOSC = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                1,
                UUID.randomUUID(),
                QuestionTypeDTO.SC);

        antwortDTOSC = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frageDTOSC.id(),
                STUDENT_ID,
                TIME);

        reviewDTOMC = new ReviewDTO(
                UUID.randomUUID(),
                antwortDTOMC.id(),
                UUID.randomUUID(),
                "Bewertung",
                1);

        reviewDTOSC = new ReviewDTO(
                UUID.randomUUID(),
                antwortDTOSC.id(),
                UUID.randomUUID(),
                "Bewertung",
                1);
    }

    @Test
    void generateReview_MCOnly() {
        when(automaticReviewService.autoReviewMC(any(), any(), any(), eq(STUDENT_ID), any()))
                .thenReturn(List.of(reviewDTOMC));

        List<ReviewDTO> result = reviewGenerationService.generateReviews(
                STUDENT_ID,
                List.of(frageDTOMC),
                List.of(antwortDTOMC));

        assertThat(result).hasSize(1);
    }

    @Test
    void generateReview_MCWithSC() {
        when(automaticReviewService.autoReviewMC(any(), any(), any(), eq(STUDENT_ID), any()))
                .thenReturn(List.of(reviewDTOMC));
        when(automaticReviewService.autoReviewSC(any(), any(), any(), eq(STUDENT_ID), any()))
                .thenReturn(List.of(reviewDTOSC));

        List<ReviewDTO> result = reviewGenerationService.generateReviews(
                STUDENT_ID,
                List.of(frageDTOMC, frageDTOSC),
                List.of(antwortDTOMC, antwortDTOSC));

        assertThat(result).hasSize(2);
    }
}
