package exambyte.application.dto;

import java.time.LocalDateTime;

public record VersuchDTO(
        LocalDateTime lastChanges,
        double erreichtePunkte,
        double maxPunkte,
        double prozent) {}
