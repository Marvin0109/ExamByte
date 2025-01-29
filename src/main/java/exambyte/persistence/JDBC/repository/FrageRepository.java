package exambyte.persistence.JDBC.repository;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface FrageRepository extends CrudRepository<FrageEntityJDBC, Long> {
}
