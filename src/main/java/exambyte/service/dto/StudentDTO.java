package exambyte.service.dto;

import java.util.UUID;

public record StudentDTO(Long id, UUID fachId, String name) {
}
