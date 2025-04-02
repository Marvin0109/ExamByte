package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.repository.KorrekteAntwortenRepository;
import exambyte.domain.service.KorrekteAntwortenService;
import org.springframework.stereotype.Service;

@Service
public class KorrekteAntwortenServiceImpl implements KorrekteAntwortenService {

    private final KorrekteAntwortenRepository repository;

    public KorrekteAntwortenServiceImpl(KorrekteAntwortenRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addKorrekteAntwort(KorrekteAntworten korrekteAntwort) {
        repository.save(korrekteAntwort);
    }
}
