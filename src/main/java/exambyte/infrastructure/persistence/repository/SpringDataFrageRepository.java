package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataFrageRepository extends CrudRepository<FrageEntity, Long> {

    Collection<FrageEntity> findByExamFachId(UUID examFachId);

    Collection<FrageEntity> findAll();

    Optional<FrageEntity> findByFachId(UUID id);

    FrageEntity save(FrageEntity frageEntity);
}
