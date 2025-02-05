package exambyte.persistence.repository;

import exambyte.persistence.entities.ProfessorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataProfessorRepository extends CrudRepository<ProfessorEntity, Long> {

    Optional<ProfessorEntity> findByName(String name);

    Optional<ProfessorEntity> findByFachId(UUID fachId);

    ProfessorEntity save(ProfessorEntity professor);

    UUID findFachIdByName(String name);
}
