package exambyte.application.dto;

import java.util.UUID;

public record KorrekteAntwortenDTO (
        UUID id,
        String antworten,
        String antwortOptionen,
        UUID frageId) {}
