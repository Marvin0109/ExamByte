package exambyte.persistence.repository;

import exambyte.persistence.entities.KorrektorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataKorrektorRepository extends CrudRepository<KorrektorEntity, Long> {

    Optional<KorrektorEntity> findByName(String name);

    Optional<KorrektorEntity> findByFachId(UUID fachId);

    KorrektorEntity save(KorrektorEntity korrektor);
}
