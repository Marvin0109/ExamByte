package exambyte.persistence.repository;

import exambyte.persistence.entities.ProfessorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends CrudRepository<ProfessorEntity, Integer> {

}
