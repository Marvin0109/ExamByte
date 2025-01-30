package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringDataFrageRepository extends CrudRepository<FrageEntityJDBC, Long> {

    Optional<FrageEntityJDBC> findById(Long id);

    FrageEntityJDBC save(FrageEntityJDBC frageEntityJDBC);
}
