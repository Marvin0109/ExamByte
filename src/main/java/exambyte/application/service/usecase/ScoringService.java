package exambyte.application.service.usecase;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ScoringService {

    double berechneErreichtePunkte(List<AntwortDTO> antworten, Map<UUID, FrageDTO> fragen, LocalDateTime result);
}
