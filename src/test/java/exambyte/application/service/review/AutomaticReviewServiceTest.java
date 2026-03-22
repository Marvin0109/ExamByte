package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AutomaticReviewServiceTest {

    private ReviewService service;
    private AutomaticReviewService automaticReviewService;
    private final UUID studentUUID = UUID.randomUUID();
    private final LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        service = mock(ReviewService.class);
        automaticReviewService = new AutomaticReviewServiceImpl();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("mcTestCases")
    @DisplayName("MC automatische Bewertung")
    void autoReviewMC(
            String bewertung,
            double maxPunkte,
            String studentAnswer,
            String solution,
            double expectedPunkte
    ) {
        // Arrange
        QuestionDTO frage = new QuestionDTO(
                UUID.randomUUID(),
                "Fragetext",
                maxPunkte,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                studentAnswer,
                frage.id(),
                studentUUID,
                antwortTime
        );

        CorrectAnswersDTO correctAnswersDTO = new CorrectAnswersDTO(
                UUID.randomUUID(),
                solution,
                "A\nB\nC\nD\nE\nF\nG\nH",
                frage.id()
        );

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(frage),
                List.of(answer),
                List.of(correctAnswersDTO),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews.getFirst().points()).isEqualTo(expectedPunkte);
    }

    static Stream<Arguments> mcTestCases() {
        return Stream.of(
                Arguments.of("Alles richtig", 4.0, "A\nB\nC\nD", "A\nB\nC\nD", 4.0),
                Arguments.of("3 richtig, 0 falsch", 4.0, "A\nB\nC", "A\nB\nC\nD", 3.0),
                Arguments.of("3 richtig, 1 falsch", 4.0, "A\nB\nC\nE", "A\nB\nC\nD", 2.0),
                Arguments.of("2 richtig, 0 falsch", 4.0, "A\nB", "A\nB\nC\nD", 2.0),
                Arguments.of("2 richtig, 1 falsch", 4.0, "A\nB\nE", "A\nB\nC\nD", 1.0),
                Arguments.of("2 richtig, 2 falsch", 4.0, "A\nB\nE\nF", "A\nB\nC\nD", 0.0),
                Arguments.of("1 richtig, 0 falsch", 4.0, "A", "A\nB\nC\nD", 1.0),
                Arguments.of("1 richtig, 1 falsch", 4.0, "A\nE", "A\nB\nC\nD", 0.0),
                Arguments.of("0 richtig, 0 falsch", 4.0, "", "A\nB\nC\nD", 0.0),
                Arguments.of("Alles falsch", 4.0, "E\nF\nG\nH", "A\nB\nC\nD", 0.0),

                Arguments.of("Alles richtig", 3.5, "A\nB\nC\nD", "A\nB\nC\nD", 3.5),
                Arguments.of("3 richtig, 0 falsch", 3.5, "A\nB\nC\nD", "A\nB\nC", 2.5),
                Arguments.of("3 richtig, 1 falsch", 3.5, "A\nB\nC\nD", "A\nB\nC\nE", 2.0),
                Arguments.of("2 richtig, 1 falsch", 3.5, "A\nB\nC\nD", "A\nB\nE", 0.0),
                Arguments.of("1 richtig, 1 falsch", 3.5, "A\nB\nC\nD", "A\nE", 0.0),
                Arguments.of("Alles falsch", 3.5, "A\nB\nC\nD", "E\nF\nG\nH", 0.0),

                Arguments.of("3 richtig, 1 falsch", 2.0, "A\nB\nC\nE", "A\nB\nC\nD", 1.0),
                Arguments.of("2 richtig, 1 falsch", 2.0, "A\nB\nE", "A\nB\nC\nD", 0.5)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("scTestCases")
    @DisplayName("SC automatische Bewertung")
    void autoReviewSC(
            String bewertung,
            double maxPunkte,
            String studentAnswer,
            String solution,
            double expectedPunkte
    ) {
        // Arrange
        QuestionDTO frage = new QuestionDTO(
                UUID.randomUUID(),
                "Fragetext",
                maxPunkte,
                UUID.randomUUID(),
                QuestionTypeDTO.SC
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                studentAnswer,
                frage.id(),
                studentUUID,
                antwortTime
        );

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                solution,
                "A\nB\nC\nD",
                frage.id()
        );

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(frage),
                List.of(answer),
                List.of(correctAnswers),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews.getFirst().points()).isEqualTo(expectedPunkte);
    }

    static Stream<Arguments> scTestCases() {
        return Stream.of(
                Arguments.of("Richtig", 1, "A", "A", 1),
                Arguments.of("Richtig", 0.5, "A", "A", 0.5),
                Arguments.of("Falsch", 1, "B", "A", 0)
        );
    }

    @Test
    @DisplayName("SC: StudentAntwort nicht existierend")
    void autoReviewSC_studentAntwortNotFound() {
        // Arrange
        QuestionDTO frage = new QuestionDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 4\nAntwort 5",
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4\nAntwort 5",
                frage.id());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewSC(
                List.of(frage),
                List.of(),
                List.of(correctAnswers),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("SC: KorrekteAntwort nicht existierend")
    void autoReviewSC_correctAnswersNotFound() {
        // Arrange
        QuestionDTO frage = new QuestionDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 3\nAntwort 4",
                frage.id(),
                studentUUID,
                antwortTime);

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewSC(
                List.of(frage),
                List.of(answer),
                List.of(),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("MC: StudentAnswer nicht existierend")
    void autoReviewMC_studentAnswerNotFound() {
        // Arrange
        QuestionDTO frage = new QuestionDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 4\nAntwort 5",
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4\nAntwort 5",
                frage.id());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(frage),
                List.of(),
                List.of(correctAnswers),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("MC: KorrekteAntwort nicht existierend")
    void autoReviewMC_correctAnswersNotFound() {
        // Arrange
        QuestionDTO frage = new QuestionDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AnswerDTO antwort = new AnswerDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 3\nAntwort 4",
                frage.id(),
                studentUUID,
                antwortTime);

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(frage),
                List.of(antwort),
                List.of(),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).isEmpty();
    }
}
