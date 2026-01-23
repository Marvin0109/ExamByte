package exambyte.application;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.AutomaticReviewService;
import exambyte.application.service.AutomaticReviewServiceImpl;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Test
    @DisplayName("SC alles richtig")
    void automatischeReviewSCSuccess() {
        // Arrange
        FrageDTO frage1 = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                5,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.SC);

        FrageDTO frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 2",
                10,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.SC);

        List<FrageDTO> scFragen = List.of(frage1, frage2);

        AntwortDTO antwort1 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 1",
                frage1.fachId(),
                studentUUID,
                antwortTime);

        AntwortDTO antwort2 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 2",
                frage2.fachId(),
                studentUUID,
                antwortTime);

        List<AntwortDTO> antwortDTOList = List.of(antwort1, antwort2);

        KorrekteAntwortenDTO korrekteAntworten1 = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 1",
                "Antwort 1\nAntwort A",
                frage1.fachId());

        KorrekteAntwortenDTO korrekteAntworten2 = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 2",
                "Antwort 2\nAntwort A",
                frage2.fachId());

        List<KorrekteAntwortenDTO> korrekteAntwortenList = List.of(korrekteAntworten1, korrekteAntworten2);

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewSC(
                scFragen,
                antwortDTOList,
                korrekteAntwortenList,
                studentUUID,
                service);

        // Assert
        assertThat(reviews).hasSize(2);
        assertThat(reviews)
                .extracting(ReviewDTO::punkte)
                .containsExactlyInAnyOrder(frage1.maxPunkte(), frage2.maxPunkte());
    }

    @Test
    @DisplayName("MC alles richtig")
    void automatischeReviewMCSuccess() {
        // Arrange
        FrageDTO frage1 = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                5,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        FrageDTO frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 2",
                10,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AntwortDTO antwort1 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 1\nAntwort 2",
                frage1.fachId(),
                studentUUID,
                antwortTime);

        AntwortDTO antwort2 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 3",
                frage2.fachId(),
                studentUUID,
                antwortTime);

        KorrekteAntwortenDTO korrekteAntworten1 = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 1\nAntwort 2",
                "Antwort 1\nAntwort A\nAntwort 2",
                frage1.fachId());

        KorrekteAntwortenDTO korrekteAntworten2 = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 3",
                "Antwort 2\nAntwort A\nAntwort 3",
                frage2.fachId());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                List.of(frage1, frage2),
                List.of(antwort1, antwort2),
                List.of(korrekteAntworten1, korrekteAntworten2),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(2);
        assertThat(reviews)
                .extracting(ReviewDTO::punkte)
                .containsExactlyInAnyOrder(frage1.maxPunkte(), frage2.maxPunkte());
    }

    @Test
    @DisplayName("MC: Alles falsch")
    void automatischeReviewMCFailure() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                5,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 1\nAntwort 2",
                frage.fachId(),
                studentUUID,
                antwortTime);

        KorrekteAntwortenDTO korrekteAntworten = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort3\nAntwort 4",
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4",
                frage.fachId());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                List.of(frage),
                List.of(antwort),
                List.of(korrekteAntworten),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews)
                .extracting(ReviewDTO::punkte)
                .containsExactly(0);
    }

    @Test
    @DisplayName("MC: 1 richtig, 1 falsch -> 0 Punkte")
    void automatischeReviewMCMixed() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                2,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 1\nAntwort 2",
                frage.fachId(),
                studentUUID,
                antwortTime);

        KorrekteAntwortenDTO korrekteAntworten = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 4",
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4",
                frage.fachId());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                List.of(frage),
                List.of(antwort),
                List.of(korrekteAntworten),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews)
                .extracting(ReviewDTO::punkte)
                .containsExactly(0);
    }

    @Test
    @DisplayName("MC: 2 richtig, 1 falsch -> 1 Punkt")
    void automatischeReviewMCMixed2() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Fragetext 1",
                3,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 3\nAntwort 4",
                frage.fachId(),
                studentUUID,
                antwortTime);

        KorrekteAntwortenDTO korrekteAntworten = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 2\nAntwort 4\nAntwort 5",
                "Antwort 1\nAntwort 2\nAntwort 3\nAntwort 4\nAntwort 5",
                frage.fachId());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                List.of(frage),
                List.of(antwort),
                List.of(korrekteAntworten),
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews)
                .extracting(ReviewDTO::punkte)
                .containsExactly(1);
    }
}
