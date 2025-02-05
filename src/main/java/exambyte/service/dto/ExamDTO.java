package exambyte.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExamDTO(Long id, UUID fachId, String title, UUID professorFachId,
                      LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
}
