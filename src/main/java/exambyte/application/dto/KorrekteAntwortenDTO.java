package exambyte.application.dto;

import java.util.UUID;

public record KorrekteAntwortenDTO(Long id, UUID fachID, UUID frageFachID, String korrekteAntworten) {
}
