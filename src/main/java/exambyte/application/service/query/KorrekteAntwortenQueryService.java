package exambyte.application.service.query;

import exambyte.application.dto.KorrekteAntwortenDTO;

import java.util.UUID;

public interface KorrekteAntwortenQueryService {

    KorrekteAntwortenDTO getLoesungForFrage(UUID frageId);
}
