package exambyte.application.dto;

import java.util.UUID;

public record ProfessorDTO(Long id, UUID fachId, String name) {
}
