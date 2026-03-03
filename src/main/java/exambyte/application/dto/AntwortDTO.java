package exambyte.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AntwortDTO (
        UUID id,
        String antwortText,
        UUID frageId,
        UUID studentId,
        LocalDateTime antwortZeitpunkt) {}
