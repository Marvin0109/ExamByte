package exambyte.service;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;

import java.util.Optional;
import java.util.UUID;

public interface FrageRepository {

    Optional<FrageEntityJDBC> findByFachId(UUID fachId);

    void save(FrageEntityJDBC frage);
}
