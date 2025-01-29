package exambyte.persistence.JDBC.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends CrudRepository<ProfessorEntityJDBC, Long> {

}
