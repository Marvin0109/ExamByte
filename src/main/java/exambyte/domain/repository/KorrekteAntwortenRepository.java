package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;

import java.util.Optional;
import java.util.UUID;

public interface KorrekteAntwortenRepository {

    Optional<KorrekteAntworten> findById(UUID id);

    Optional<KorrekteAntworten> findByFrageId(UUID frageID);

    void save(KorrekteAntworten antworten);

    void deleteAll();
}
