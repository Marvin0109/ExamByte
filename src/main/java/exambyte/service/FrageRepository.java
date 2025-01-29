package exambyte.service;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;

import java.util.Optional;

public interface FrageRepository {

    Optional<FrageEntityJDBC> findById(Long id);

    void save(FrageEntityJDBC frage);
}
