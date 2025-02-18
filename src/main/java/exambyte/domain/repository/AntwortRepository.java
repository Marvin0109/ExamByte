package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Antwort;

import java.util.Optional;
import java.util.UUID;

public interface AntwortRepository {

    Antwort findByFrageFachId(UUID id);

    Optional<Antwort> findByFachId(UUID id);

    Optional<Antwort> findByStudentFachIdAndFrageFachId(UUID studentFachId, UUID examFachId);

    void save(Antwort antwort);

}
