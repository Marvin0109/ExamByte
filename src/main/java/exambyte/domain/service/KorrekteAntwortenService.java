package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;

import java.util.List;
import java.util.UUID;

public interface KorrekteAntwortenService {
    void addKorrekteAntwort(KorrekteAntworten korrekteAntwort);
    List<KorrekteAntworten> findKorrekteAntwort(UUID frageFachId);
    void deleteAll();
}
