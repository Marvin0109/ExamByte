package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Antwort;

import java.util.Optional;
import java.util.UUID;

public interface AntwortRepository {

    Antwort findByFrageId(UUID id);

    Optional<Antwort> findById(UUID id);

    Optional<Antwort> findByStudentIdAndFrageId(UUID studentFachId, UUID examFachId);

    void save(Antwort antwort);

    void deleteAll();

    void deleteAnswer(UUID fachId);
}
