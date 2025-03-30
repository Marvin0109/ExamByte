package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AntwortDAO extends CrudRepository<AntwortEntity, Long> {

    Optional<AntwortEntity> findByFrageFachId(UUID id);

    Optional<AntwortEntity> findByStudentFachIdAndFrageFachId(UUID studentId, UUID examId);

    Optional<AntwortEntity> findByFachId(UUID id);

    AntwortEntity save(AntwortEntity entity);
}