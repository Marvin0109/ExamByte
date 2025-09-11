package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;

import java.util.UUID;

public interface KorrekteAntwortenService {
    void addKorrekteAntwort(KorrekteAntworten korrekteAntwort);
    KorrekteAntworten findKorrekteAntwort(UUID frageFachId);
    void deleteAll();
}
