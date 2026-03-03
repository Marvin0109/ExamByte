package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.Antwort;

import java.util.UUID;

public interface AntwortService {

    Antwort findByFrageId(UUID frageId);

    void addAntwort(Antwort antwort);

    Antwort findByStudentAndFrage(UUID studentId, UUID frageId);

    void deleteAll();

    void deleteAnswer(UUID id);
}
