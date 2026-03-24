package exambyte.application.service.query;

import exambyte.application.dto.ProfessorDTO;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorService {

    Optional<UUID> getProfIdByName(String name);

    ProfessorDTO getProfessorById(UUID id);

    Optional<ProfessorDTO> getProfessorByName(String name);

    void saveProfessor(String name);
}
