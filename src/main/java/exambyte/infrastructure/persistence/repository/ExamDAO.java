package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ExamEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ExamDAO extends CrudRepository<ExamEntity, Long> {

    Collection<ExamEntity> findAll();

    Optional<ExamEntity> findByFachId(UUID id);

    ExamEntity save(ExamEntity test);

    @Query("SELECT fach_id FROM exam WHERE start_time = :start")
    Optional<UUID> findByStartTime(@Param("start") LocalDateTime startTime);

    @Transactional
    @Modifying
    @Query("DELETE FROM exam e WHERE e.fach_id = :uuid")
    void deleteByFachId(@Param("uuid") UUID uuid);

    void deleteAll();
}
