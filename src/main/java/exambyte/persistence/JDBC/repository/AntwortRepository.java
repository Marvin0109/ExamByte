package exambyte.persistence.JDBC.repository;


import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AntwortRepository extends CrudRepository<AntwortEntityJDBC, Long> {
}
