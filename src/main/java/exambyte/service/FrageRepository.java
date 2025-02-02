package exambyte.service;

import exambyte.persistence.entities.FrageEntity;

import java.util.Optional;
import java.util.UUID;

public interface FrageRepository {

    Optional<FrageEntity> findByFachId(UUID fachId);

    void save(FrageEntity frage);
}
