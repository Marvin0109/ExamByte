package exambyte.domain.repository;

import exambyte.domain.model.aggregate.user.Korrektor;

import java.util.Optional;
import java.util.UUID;

public interface KorrektorRepository {

    Optional<Korrektor> findByName(String name);

    Optional<Korrektor> findByFachId(UUID fachId);

    void save(Korrektor korrektor);
}
