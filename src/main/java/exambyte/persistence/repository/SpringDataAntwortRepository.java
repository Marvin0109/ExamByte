package exambyte.persistence.repository;


import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataAntwortRepository extends CrudRepository<AntwortEntityJDBC, Long> {

    Optional<AntwortEntityJDBC> findByFachId(UUID id);

    AntwortEntityJDBC save(AntwortEntityJDBC entity);
}