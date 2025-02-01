package exambyte.persistence.repository;


import exambyte.persistence.entities.AntwortEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataAntwortRepository extends CrudRepository<AntwortEntity, Long> {

    Optional<AntwortEntity> findByFachId(UUID id);

    AntwortEntity save(AntwortEntity entity);
}