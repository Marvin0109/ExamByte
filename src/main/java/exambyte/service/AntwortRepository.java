package exambyte.service;

import exambyte.persistence.entities.AntwortEntity;

import java.util.Optional;
import java.util.UUID;

public interface AntwortRepository {

    Optional<AntwortEntity> findByFachId(UUID id);

    void save(AntwortEntity entity);
}
