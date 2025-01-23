package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Integer> {

}
