package exambyte.application.dto;

import java.util.UUID;

public record ReviewDTO (
        UUID id,
        UUID antwortId,
        UUID korrektorId,
        String bewertung,
        int punkte) {}
