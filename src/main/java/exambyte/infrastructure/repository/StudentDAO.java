package exambyte.infrastructure.repository;

import exambyte.infrastructure.entity.StudentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentDAO extends CrudRepository<StudentEntity, UUID> {

    Optional<StudentEntity> findByName(String name);

    Optional<StudentEntity> findIdByName(String name);
}
