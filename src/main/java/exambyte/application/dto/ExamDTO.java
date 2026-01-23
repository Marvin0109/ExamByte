package exambyte.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExamDTO(
        UUID fachId,
        String title,
        UUID professorFachId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime resultTime) {}
