package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataAntwortRepository extends CrudRepository<AntwortEntity, Long> {

    Optional<AntwortEntity> findByFrageFachId(UUID id);

    Optional<AntwortEntity> findByStudentFachIdAndFrageFachId(UUID studentId, UUID examId);

    Optional<AntwortEntity> findByFachId(UUID id);

    @Lock(LockMode.PESSIMISTIC_READ)
    AntwortEntity save(AntwortEntity entity);
}