package exambyte.domain.service;

import exambyte.domain.aggregate.exam.Antwort;

import java.util.UUID;

public interface AntwortService {

    Antwort findByFrageFachId(UUID frageFachId);

    void addAntwort(Antwort antwort);

    Antwort findByStudentAndFrage(UUID studentId, UUID frageId);
}
