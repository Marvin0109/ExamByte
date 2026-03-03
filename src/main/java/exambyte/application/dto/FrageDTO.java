package exambyte.application.dto;

import exambyte.application.common.QuestionTypeDTO;

import java.util.UUID;

public record FrageDTO (
        UUID id,
        String frageText,
        int maxPunkte,
        UUID examId,
        QuestionTypeDTO type) {}
