package exambyte.application.dto;

import java.util.UUID;

public record ReviewDTO (
        UUID id,
        UUID answerId,
        UUID reviewerId,
        String text,
        double points) {}
