package exambyte.web.form.show_review;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ReviewDTO;

public record ReviewAggregateDTO(
        FrageDTO frage,
        AntwortDTO antwort,
        ReviewDTO review,
        KorrekteAntwortenDTO korrekteAntworten) {}
