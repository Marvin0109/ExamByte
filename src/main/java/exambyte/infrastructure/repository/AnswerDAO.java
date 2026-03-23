package exambyte.infrastructure.repository;

import exambyte.infrastructure.entity.AnswerEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface AnswerDAO extends CrudRepository<AnswerEntity, UUID> {

    @Query("SELECT * FROM answer WHERE question_id = :questionId AND student_id = :studentId")
    Optional<AnswerEntity> findByStudentIdAndQuestionId(
            @Param("studentId") UUID studentId,
            @Param("questionId") UUID questionId);

    @Query("SELECT * FROM answer WHERE question_id = :questionId")
    Optional<AnswerEntity> findByQuestionId(@Param("questionId") UUID questionId);

    @Transactional
    @Modifying
    @Query("""
        INSERT INTO answer (student_id, question_id, answer, submit_time)
        VALUES (:studentId, :questionId, :answer, CURRENT_TIMESTAMP)
        ON CONFLICT (student_id, question_id)
        DO UPDATE
            SET answer = EXCLUDED.answer,
                submit_time = CURRENT_TIMESTAMP
    """)
    void upsertAnswer(@Param("studentId") UUID studentId,
                       @Param("questionId") UUID questionId,
                       @Param("answer") String answer);
}