package exambyte.application.service.exam;

import exambyte.application.dto.ProfessorDTO;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorQueryService {

    Optional<UUID> getProfIdByName(String name);

    ProfessorDTO getProfessorById(UUID id);
}
