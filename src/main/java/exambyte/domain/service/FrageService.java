package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.Frage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FrageService {

    List<Frage> getFragenForExam(UUID examId);

    UUID addFrage(Frage frage);

    Optional<Frage> getFrage(UUID frageId);

    void deleteAll();
}
