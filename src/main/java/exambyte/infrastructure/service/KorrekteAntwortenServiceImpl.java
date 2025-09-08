package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.repository.KorrekteAntwortenRepository;
import exambyte.domain.service.KorrekteAntwortenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    @Override
    public List<KorrekteAntworten> findKorrekteAntwort(UUID frageFachId) {
        return repository.findByFrageFachID(frageFachId).stream().toList();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
