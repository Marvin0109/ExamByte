package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;

public interface KorrekteAntwortenService {
    void addKorrekteAntwort(KorrekteAntworten korrekteAntwort);
    void deleteAll();
}
