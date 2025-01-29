package exambyte.persistence.repository;


import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface SpringDataAntwortRepository{

    Optional<AntwortEntityJDBC> findById(Long id);

    AntwortEntityJDBC save(AntwortEntityJDBC entity);
}