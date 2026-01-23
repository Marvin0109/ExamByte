package exambyte.application.dto;

import java.util.UUID;

public record ReviewDTO (
        UUID fachId,
        UUID antwortFachId,
        UUID korrektorFachId,
        String bewertung,
        int punkte) {}
