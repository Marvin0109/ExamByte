package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ExamEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ExamDAO extends CrudRepository<ExamEntity, UUID> {

    @NotNull Collection<ExamEntity> findAll();

    Optional<ExamEntity> findByStartZeitpunkt(LocalDateTime startZeitpunkt);
}
