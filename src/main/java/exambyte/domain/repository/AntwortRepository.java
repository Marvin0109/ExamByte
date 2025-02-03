package exambyte.domain.repository;

import exambyte.domain.aggregate.exam.Antwort;

import java.util.Optional;
import java.util.UUID;

public interface AntwortRepository {

    Antwort findByFrageFachId(UUID id);

    Optional<Antwort> findByFachId(UUID id);

    void save(Antwort antwort);
}
