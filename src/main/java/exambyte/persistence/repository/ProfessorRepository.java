package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Professor;
import org.springframework.data.repository.CrudRepository;

public interface ProfessorRepository extends CrudRepository<Professor, Integer> {

}
