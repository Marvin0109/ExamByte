package exambyte.domain.repository;

import exambyte.domain.aggregate.exam.Frage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FrageRepository {

    Collection<Frage> findAll();

    Optional<Frage> findByFachId(UUID fachId);

    List<Frage> findByExamFachId(UUID examFachId);

    void save(Frage frage);
}
