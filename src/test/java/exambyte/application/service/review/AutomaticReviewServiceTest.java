package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
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
    void automatischeReviewMC(
            String bewertung,
            int maxPunkte,
            String studentAntwort,
            String korrekteAntwort,
            int expectedPunkte
    ) {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext",
                maxPunkte,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                studentAntwort,
                frage.id(),
                studentUUID,
                antwortTime
        );

        KorrekteAntwortenDTO korrekteAntwortenDTO = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                korrekteAntwort,
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4\nAntwort 5",
                frage.id()
        );

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                List.of(frage),
                List.of(antwort),
                List.of(korrekteAntwortenDTO),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews.getFirst().punkte()).isEqualTo(expectedPunkte);
    }

    static Stream<Arguments> mcTestCases() {
        return Stream.of(
                Arguments.of(
                        "Richtig",
                        5,
                        "Antwort 1\nAntwort 2",
                        "Antwort 1\nAntwort 2",
                        5
                ),

                Arguments.of(
                        "Falsch",
                        5,
                        "Antwort 1\nAntwort 2",
                        "Antwort 3\nAntwort 4",
                        0
                ),

                Arguments.of(
                        "1 richtig, 1 falsch -> 0 Punkte",
                        2,
                        "Antwort 1\nAntwort 2",
                        "Antwort 2\nAntwort 4",
                        0
                ),

                Arguments.of(
                        "2 richtig, 1 falsch -> 1 Punkt",
                        3,
                        "Antwort 2\nAntwort 3\nAntwort 4",
                        "Antwort 2\nAntwort 4\nAntwort 5",
                        1
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("scTestCases")
    @DisplayName("SC automatische Bewertung")
    void automatischeReviewSC(
            String bewertung,
            int maxPunkte,
            String studentAntwort,
            String korrekteAntwort,
            int expectedPunkte
    ) {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext",
                maxPunkte,
                UUID.randomUUID(),
                QuestionTypeDTO.SC
        );

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                studentAntwort,
                frage.id(),
                studentUUID,
                antwortTime
        );

        KorrekteAntwortenDTO korrekteAntwortenDTO = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                korrekteAntwort,
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4\nAntwort 5",
                frage.id()
        );

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                List.of(frage),
                List.of(antwort),
                List.of(korrekteAntwortenDTO),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews.getFirst().punkte()).isEqualTo(expectedPunkte);
    }

    static Stream<Arguments> scTestCases() {
        return Stream.of(
                Arguments.of(
                        "Alles richtig",
                        5,
                        "Antwort 1",
                        "Antwort 1",
                        5
                ),

                Arguments.of(
                        "Alles falsch",
                        5,
                        "Antwort 2",
                        "Antwort 3",
                        0
                )
        );
    }

    @Test
    @DisplayName("SC: StudentAntwort nicht existierend")
    void automatischeReviewSC_studentAntwortNotFound() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        KorrekteAntwortenDTO korrekteAntworten = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 4\nAntwort 5",
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4\nAntwort 5",
                frage.id());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewSC(
                List.of(frage),
                List.of(),
                List.of(korrekteAntworten),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("SC: KorrekteAntwort nicht existierend")
    void automatischeReviewSC_korrekteAntwortNotFound() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 3\nAntwort 4",
                frage.id(),
                studentUUID,
                antwortTime);

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewSC(
                List.of(frage),
                List.of(antwort),
                List.of(),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("MC: StudentAntwort nicht existierend")
    void automatischeReviewMC_studentAntwortNotFound() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        KorrekteAntwortenDTO korrekteAntworten = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 4\nAntwort 5",
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4\nAntwort 5",
                frage.id());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                List.of(frage),
                List.of(),
                List.of(korrekteAntworten),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    @DisplayName("MC: KorrekteAntwort nicht existierend")
    void automatischeReviewMC_korrekteAntwortNotFound() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 3\nAntwort 4",
                frage.id(),
                studentUUID,
                antwortTime);

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
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
