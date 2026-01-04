package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface ReviewDAO extends CrudRepository<ReviewEntity, Long> {

    @Query("SELECT * FROM review WHERE antwort_fach_id = :id")
    Optional<ReviewEntity> findByAntwortFachId(@Param("id") UUID id);

    Optional<ReviewEntity> findByFachId(UUID fachId);

    ReviewEntity save(ReviewEntity review);

    void deleteAll();

    @Transactional
    @Modifying
    @Query("DELETE FROM review r WHERE r.fach_id = :fachId")
    void deleteByFachId(@Param("fachId") UUID fachId);
}
