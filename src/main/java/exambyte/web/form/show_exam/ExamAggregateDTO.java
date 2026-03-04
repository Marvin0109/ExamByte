package exambyte.web.form.show_exam;

import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;

public record ExamAggregateDTO(
        FrageDTO frage,
        KorrekteAntwortenDTO korrekteAntworten) {}
