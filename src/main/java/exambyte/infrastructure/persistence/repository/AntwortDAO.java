package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface AntwortDAO extends CrudRepository<AntwortEntity, UUID> {

    Optional<AntwortEntity> findByStudentIdAndFrageId(UUID studentId, UUID frageId);

    Optional<AntwortEntity> findByFrageId(UUID frageId);

    @Transactional
    @Modifying
    @Query("""
        INSERT INTO antwort (student_id, frage_id, antwort_text, antwort_zeitpunkt)
        VALUES (:studentId, :frageId, :text, CURRENT_TIMESTAMP)
        ON CONFLICT (student_id, frage_id)
        DO UPDATE
            SET antwort_text = EXCLUDED.antwort_text,
                antwort_zeitpunkt = CURRENT_TIMESTAMP
    """)
    void upsertAntwort(@Param("studentId") UUID studentId,
                       @Param("frageId") UUID frageId,
                       @Param("text") String antwortText);
}