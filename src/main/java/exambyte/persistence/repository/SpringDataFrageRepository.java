package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataFrageRepository extends CrudRepository<FrageEntityJDBC, Long> {

    Optional<FrageEntityJDBC> findByFachId(UUID id);

    FrageEntityJDBC save(FrageEntityJDBC frageEntityJDBC);
}
