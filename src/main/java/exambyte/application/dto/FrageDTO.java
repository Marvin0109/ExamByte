package exambyte.application.dto;

import exambyte.application.common.QuestionTypeDTO;

import java.util.UUID;

public record FrageDTO (
        UUID fachId,
        String frageText,
        int maxPunkte,
        UUID profUUID,
        UUID examUUID,
        QuestionTypeDTO type) {}
