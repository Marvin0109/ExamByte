package exambyte.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnswerDTO (
        UUID id,
        String answer,
        UUID frageId,
        UUID studentId,
        LocalDateTime submitTime) {}
