package exambyte.application.dto;

import java.util.UUID;

public record ReviewDTO (
        UUID id,
        UUID antwortId,
        UUID reviewerId,
        String bewertung,
        double punkte) {}
