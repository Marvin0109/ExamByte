package exambyte.infrastructure.repository;

import exambyte.infrastructure.entity.ExamEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ExamDAO extends CrudRepository<ExamEntity, UUID> {

    Collection<ExamEntity> findAll();

    Optional<ExamEntity> findByStart(LocalDateTime start);
}
