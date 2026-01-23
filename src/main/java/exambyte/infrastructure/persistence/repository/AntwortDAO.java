package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface AntwortDAO extends CrudRepository<AntwortEntity, Long> {

    Optional<AntwortEntity> findByFrageFachId(UUID id);

    Optional<AntwortEntity> findByStudentFachIdAndFrageFachId(UUID studentId, UUID examId);

    Optional<AntwortEntity> findByFachId(UUID id);

    AntwortEntity save(AntwortEntity entity);

    @Transactional
    @Modifying
    @Query("UPDATE antwort SET antwort_zeitpunkt = CURRENT_TIMESTAMP WHERE fach_id = :fachId")
    void updateTimestamp(@Param("fachId") UUID fachId);

    void deleteAll();

    @Transactional
    @Modifying
    @Query("DELETE FROM antwort a WHERE a.fach_id = :fachId")
    void deleteByFachId(@Param("fachId") UUID fachId);
}