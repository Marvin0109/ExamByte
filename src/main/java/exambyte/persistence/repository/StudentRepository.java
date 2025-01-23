package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

}
