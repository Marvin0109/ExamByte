package exambyte.web.service;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;

public record PreparedFrageData(
        FrageDTO frage,
        AntwortDTO antwort,
        KorrekteAntwortenDTO korrekteAntwortenDTO) {}
