package exambyte.persistence.repository;


import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringDataAntwortRepository extends CrudRepository<AntwortEntityJDBC, Long> {

    Optional<AntwortEntityJDBC> findById(Long id);

    AntwortEntityJDBC save(AntwortEntityJDBC entity);
}