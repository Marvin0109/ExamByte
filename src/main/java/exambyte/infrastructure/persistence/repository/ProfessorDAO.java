package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorDAO extends CrudRepository<ProfessorEntity, UUID> {

    Optional<ProfessorEntity> findByName(String name);
}
