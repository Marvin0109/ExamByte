package exambyte.application.dto;

import exambyte.application.common.QuestionTypeDTO;

import java.util.UUID;

public record QuestionDTO(
        UUID id,
        String text,
        double points,
        UUID examId,
        QuestionTypeDTO type) {}
