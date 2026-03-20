package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.service.CorrectAnswersService;
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
    private AnswerDTO answerMC;

    private FrageDTO frageDTOSC;
    private AnswerDTO answerSC;

    private ReviewDTO reviewDTOMC;
    private ReviewDTO reviewDTOSC;

    @Mock
    private AutomaticReviewService automaticReviewService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private CorrectAnswersService correctAnswersService;

    @Mock
    private CorrectAnswersDTOMapper correctAnswersDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewGenerationService = new ReviewGenerationServiceImpl(
                automaticReviewService,
                reviewService,
                correctAnswersService,
                correctAnswersDTOMapper);

        frageDTOMC = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                2,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        answerMC = new AnswerDTO(
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

        answerSC = new AnswerDTO(
                UUID.randomUUID(),
                "Antwort",
                frageDTOSC.id(),
                STUDENT_ID,
                TIME);

        reviewDTOMC = new ReviewDTO(
                UUID.randomUUID(),
                answerMC.id(),
                UUID.randomUUID(),
                "Bewertung",
                1);

        reviewDTOSC = new ReviewDTO(
                UUID.randomUUID(),
                answerSC.id(),
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
                List.of(answerMC));

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
                List.of(answerMC, answerSC));

        assertThat(result).hasSize(2);
    }
}
