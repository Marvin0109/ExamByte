package exambyte.application.dto;

import java.time.LocalDateTime;

public record AttemptDTO(
        LocalDateTime lastChanges,
        double erreichtePunkte,
        double maxPunkte,
        double prozent) {}
