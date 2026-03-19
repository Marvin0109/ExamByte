package exambyte.domain.repository;

import exambyte.domain.model.aggregate.user.Reviewer;

import java.util.Optional;
import java.util.UUID;

public interface ReviewerRepository {

    Optional<Reviewer> findByName(String name);

    Optional<Reviewer> findById(UUID id);

    void save(Reviewer reviewer);

    void deleteAll();
}
