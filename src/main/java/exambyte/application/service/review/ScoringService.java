package exambyte.application.service.review;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ScoringService {

    double berechneErreichtePunkte(List<AntwortDTO> antworten, Map<UUID, FrageDTO> fragen);
}
