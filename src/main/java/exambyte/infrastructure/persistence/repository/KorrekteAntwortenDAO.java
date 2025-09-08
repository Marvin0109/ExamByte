package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface KorrekteAntwortenDAO extends CrudRepository<KorrekteAntwortenEntity, Long> {

    Optional<KorrekteAntwortenEntity> findByFachID(UUID fachId);

    Collection<KorrekteAntwortenEntity> findByFrageFachID(UUID frageFachID);

    KorrekteAntwortenEntity save(KorrekteAntwortenEntity entity);

    void deleteAll();
}
