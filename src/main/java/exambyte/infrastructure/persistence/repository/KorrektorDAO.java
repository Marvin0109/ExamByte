package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.KorrektorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface KorrektorDAO extends CrudRepository<KorrektorEntity, UUID> {
    Optional<KorrektorEntity> findByName(String name);
}
