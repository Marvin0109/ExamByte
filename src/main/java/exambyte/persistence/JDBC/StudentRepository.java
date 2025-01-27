package exambyte.persistence.JDBC;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntityJDBC, Long> {

}
