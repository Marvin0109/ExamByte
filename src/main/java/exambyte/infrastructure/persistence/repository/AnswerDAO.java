package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.AnswerEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface AnswerDAO extends CrudRepository<AnswerEntity, UUID> {

    Optional<AnswerEntity> findByStudentIdAndFrageId(UUID studentId, UUID frageId);

    Optional<AnswerEntity> findByFrageId(UUID frageId);

    @Transactional
    @Modifying
    @Query("""
        INSERT INTO answer (student_id, frage_id, answer, submit_time)
        VALUES (:studentId, :frageId, :answer, CURRENT_TIMESTAMP)
        ON CONFLICT (student_id, frage_id)
        DO UPDATE
            SET answer = EXCLUDED.answer,
                submit_time = CURRENT_TIMESTAMP
    """)
    void upsertAnswer(@Param("studentId") UUID studentId,
                       @Param("frageId") UUID frageId,
                       @Param("answer") String answer);
}