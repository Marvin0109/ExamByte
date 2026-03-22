package exambyte.application.dto;

import java.time.LocalDateTime;

public record AttemptDTO(
        LocalDateTime lastChanges,
        double accumulatedPoints,
        double totalPoints,
        double scoreInPercent) {}
