package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface FrageDAO extends CrudRepository<FrageEntity, UUID> {

    Collection<FrageEntity> findByExamId(UUID examId);

    @NotNull Collection<FrageEntity> findAll();
}