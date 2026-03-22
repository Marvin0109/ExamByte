package exambyte.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExamDTO(
        UUID id,
        String title,
        UUID professorId,
        LocalDateTime start,
        LocalDateTime end,
        LocalDateTime result) {}
