package exambyte.application.dto;

import exambyte.application.enums.QuestionTypeDTO;

import java.util.UUID;

public record QuestionDTO(
        UUID id,
        String text,
        double points,
        UUID examId,
        QuestionTypeDTO type) {}
