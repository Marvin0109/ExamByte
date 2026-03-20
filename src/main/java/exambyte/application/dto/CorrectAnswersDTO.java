package exambyte.application.dto;

import java.util.UUID;

public record CorrectAnswersDTO (
        UUID id,
        String solution,
        String choices,
        UUID frageId) {}
