package exambyte.application;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.AutomaticReviewService;
import exambyte.application.service.AutomaticReviewServiceImpl;
import exambyte.domain.service.ReviewService;
import exambyte.infrastructure.service.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AutomaticReviewServiceTest {

    private ReviewService service;

    @BeforeEach
    void setUp() {
        service = mock(ReviewServiceImpl.class);
    }

    @Test
    @DisplayName("Automatische Bewertung von SC Fragen ist erfolgreich")
    void test_01() {
        // Arrange
        UUID studentUUID = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime lastChanges = LocalDateTime.of(2020, 1, 1, 0, 0);

        AutomaticReviewService automaticReviewService = new AutomaticReviewServiceImpl();
        FrageDTO frage1 = new FrageDTO(
                null,
                UUID.randomUUID(),
                "Fragetext 1",
                5,
                QuestionTypeDTO.SC,
                UUID.randomUUID(),
                UUID.randomUUID());

        FrageDTO frage2 = new FrageDTO(
                null,
                UUID.randomUUID(),
                "Fragetext 2",
                10,
                QuestionTypeDTO.SC,
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        List<FrageDTO> scFragen = List.of(frage1, frage2);

        AntwortDTO antwort1 = new AntwortDTO.AntwortDTOBuilder()
                .fachId(UUID.randomUUID())
                .frageFachId(frage1.getFachId())
                .antwortText("Antwort 1")
                .studentFachId(studentUUID)
                .antwortZeitpunkt(antwortTime)
                .lastChangesZeitpunkt(lastChanges)
                .build();

        AntwortDTO antwort2 = new AntwortDTO.AntwortDTOBuilder()
                .fachId(UUID.randomUUID())
                .frageFachId(frage2.getFachId())
                .antwortText("Antwort 2")
                .studentFachId(studentUUID)
                .antwortZeitpunkt(antwortTime)
                .lastChangesZeitpunkt(lastChanges)
                .build();

        List<AntwortDTO> antwortDTOList = List.of(antwort1, antwort2);

        KorrekteAntwortenDTO korrekteAntworten1 = new KorrekteAntwortenDTO(
                null,
                UUID.randomUUID(),
                frage1.getFachId(),
                "Antwort 1",
                "Antwort 1\nAntwort A"
        );

        KorrekteAntwortenDTO korrekteAntworten2 = new KorrekteAntwortenDTO(
                null,
                UUID.randomUUID(),
                frage2.getFachId(),
                "Antwort 2",
                "Antwort 2\nAntwort A"
        );

        List<KorrekteAntwortenDTO> korrekteAntwortenList = List.of(korrekteAntworten1, korrekteAntworten2);


        // Act

        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewSC(
                scFragen,
                antwortDTOList,
                korrekteAntwortenList,
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(2);
        assertThat(reviews)
                .extracting(ReviewDTO::getPunkte)
                .containsExactlyInAnyOrder(frage1.getMaxPunkte(), frage2.getMaxPunkte());
    }

    @Test
    @DisplayName("Automatische Bewertung von MC Fragen ist erfolgreich")
    void test_02() {
        // Arrange
        UUID studentUUID = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime lastChanges = LocalDateTime.of(2020, 1, 1, 0, 0);

        AutomaticReviewService automaticReviewService = new AutomaticReviewServiceImpl();
        FrageDTO frage1 = new FrageDTO(
                null,
                UUID.randomUUID(),
                "Fragetext 1",
                5,
                QuestionTypeDTO.MC,
                UUID.randomUUID(),
                UUID.randomUUID());

        FrageDTO frage2 = new FrageDTO(
                null,
                UUID.randomUUID(),
                "Fragetext 2",
                10,
                QuestionTypeDTO.MC,
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        List<FrageDTO> scFragen = List.of(frage1, frage2);

        AntwortDTO antwort1 = new AntwortDTO.AntwortDTOBuilder()
                .fachId(UUID.randomUUID())
                .frageFachId(frage1.getFachId())
                .antwortText("Antwort 1\nAntwort 2")
                .studentFachId(studentUUID)
                .antwortZeitpunkt(antwortTime)
                .lastChangesZeitpunkt(lastChanges)
                .build();

        AntwortDTO antwort2 = new AntwortDTO.AntwortDTOBuilder()
                .fachId(UUID.randomUUID())
                .frageFachId(frage2.getFachId())
                .antwortText("Antwort 2\nAntwort 3")
                .studentFachId(studentUUID)
                .antwortZeitpunkt(antwortTime)
                .lastChangesZeitpunkt(lastChanges)
                .build();

        List<AntwortDTO> antwortDTOList = List.of(antwort1, antwort2);

        KorrekteAntwortenDTO korrekteAntworten1 = new KorrekteAntwortenDTO(
                null,
                UUID.randomUUID(),
                frage1.getFachId(),
                "Antwort 1\nAntwort 2",
                "Antwort 1\nAntwort A\nAntwort 2"
        );

        KorrekteAntwortenDTO korrekteAntworten2 = new KorrekteAntwortenDTO(
                null,
                UUID.randomUUID(),
                frage2.getFachId(),
                "Antwort 2\nAntwort 3",
                "Antwort 2\nAntwort A\nAntwort 3"
        );

        List<KorrekteAntwortenDTO> korrekteAntwortenList = List.of(korrekteAntworten1, korrekteAntworten2);

        // Act

        List<ReviewDTO> reviews = automaticReviewService.automatischeReviewMC(
                scFragen,
                antwortDTOList,
                korrekteAntwortenList,
                studentUUID,
                service
        );

        // Assert
        assertThat(reviews).hasSize(2);
        assertThat(reviews)
                .extracting(ReviewDTO::getPunkte)
                .containsExactlyInAnyOrder(frage1.getMaxPunkte(), frage2.getMaxPunkte());
    }
}
