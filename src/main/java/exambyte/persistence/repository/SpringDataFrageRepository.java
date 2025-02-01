package exambyte.persistence.repository;

import exambyte.persistence.entities.FrageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataFrageRepository extends CrudRepository<FrageEntity, Long> {

    Optional<FrageEntity> findByFachId(UUID id);

    FrageEntity save(FrageEntity frageEntity);
}
