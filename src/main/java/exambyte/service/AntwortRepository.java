package exambyte.service;

import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;

import java.util.Optional;
import java.util.UUID;

public interface AntwortRepository {

    Optional<AntwortEntityJDBC> findByFachId(UUID id);

    void save(AntwortEntityJDBC entity);
}
