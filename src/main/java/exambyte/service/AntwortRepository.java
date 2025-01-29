package exambyte.service;

import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;

import java.util.Optional;

public interface AntwortRepository {

    Optional<AntwortEntityJDBC> findById(Long id);

    void save(AntwortEntityJDBC entity);
}
