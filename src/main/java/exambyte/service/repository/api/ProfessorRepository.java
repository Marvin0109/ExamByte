package exambyte.service.repository.api;

import exambyte.persistence.entities.ProfessorEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository {

    Optional<ProfessorEntity> findByFachId(UUID fachId);

    void save(ProfessorEntity professor);
}
