package exambyte.web.form.load_old_submit_data;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;

public record OldDataDTO(
        FrageDTO fragen,
        KorrekteAntwortenDTO korrekteAntworten,
        AntwortDTO antwort) {}
