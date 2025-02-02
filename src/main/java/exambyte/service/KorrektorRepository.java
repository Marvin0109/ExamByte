package exambyte.service;

import exambyte.persistence.entities.KorrektorEntity;

import java.util.Optional;
import java.util.UUID;

public interface KorrektorRepository {

    Optional<KorrektorEntity> findByFachId(UUID fachId);

    void save(KorrektorEntity korrektor);
}
