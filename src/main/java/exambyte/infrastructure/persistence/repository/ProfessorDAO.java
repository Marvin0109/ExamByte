package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorDAO extends CrudRepository<ProfessorEntity, Long> {

    Optional<ProfessorEntity> findByName(String name);

    Optional<ProfessorEntity> findByFachId(UUID fachId);

    ProfessorEntity save(ProfessorEntity professor);
}
