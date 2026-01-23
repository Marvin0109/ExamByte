package exambyte.application.dto;

import java.util.UUID;

public record KorrekteAntwortenDTO (
        UUID fachId,
        String antworten,
        String antwortOptionen,
        UUID frageFachId) {}
