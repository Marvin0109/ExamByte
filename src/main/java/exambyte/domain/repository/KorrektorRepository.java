package exambyte.domain.repository;

import exambyte.domain.aggregate.user.Korrektor;

import java.util.Optional;
import java.util.UUID;

public interface KorrektorRepository {

    Optional<Korrektor> findByFachId(UUID fachId);

    void save(Korrektor korrektor);
}
