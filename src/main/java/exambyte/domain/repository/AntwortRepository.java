package exambyte.domain.repository;

import exambyte.domain.aggregate.exam.Antwort;

import java.util.Optional;
import java.util.UUID;

public interface AntwortRepository {

    Optional<Antwort> findByFachId(UUID id);

    void save(Antwort antwort);
}
