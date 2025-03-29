package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataProfessorRepository extends CrudRepository<ProfessorEntity, Long> {

    Optional<ProfessorEntity> findByName(String name);

    Optional<ProfessorEntity> findByFachId(UUID fachId);

    @Lock(LockMode.PESSIMISTIC_READ)
    ProfessorEntity save(ProfessorEntity professor);

    Optional<UUID> findFachIdByName(String name);
}
