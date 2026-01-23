package exambyte.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AntwortDTO (
        UUID fachId,
        String antwortText,
        UUID frageFachId,
        UUID studentFachId,
        LocalDateTime antwortZeitpunkt) {}
