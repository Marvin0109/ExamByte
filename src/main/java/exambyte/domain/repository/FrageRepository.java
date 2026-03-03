package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Frage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FrageRepository {

    Collection<Frage> findAll();

    Optional<Frage> findById(UUID id);

    List<Frage> findByExamId(UUID examId);

    UUID save(Frage frage);

    void deleteAll();
}
