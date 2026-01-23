package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;

import java.util.Optional;
import java.util.UUID;

public interface KorrekteAntwortenRepository {

    Optional<KorrekteAntworten> findByFachId(UUID fachId);

    Optional<KorrekteAntworten> findByFrageFachID(UUID frageFachID);

    void save(KorrekteAntworten antworten);

    void deleteAll();
}
