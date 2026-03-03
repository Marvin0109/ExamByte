package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import exambyte.domain.service.AntwortService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AntwortServiceImpl implements AntwortService {

    private final AntwortRepository repository;

    public AntwortServiceImpl(AntwortRepository antwortRepository) {
        this.repository = antwortRepository;
    }

    @Override
    public Antwort findByFrageId(UUID frageId) {
        return repository.findByFrageId(frageId);
    }

    @Override
    public void addAntwort(Antwort antwort) {
        repository.save(antwort);
    }

    @Override
    public Antwort findByStudentAndFrage(UUID studentId, UUID examId) {
        return repository.findByStudentIdAndFrageId(studentId, examId)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAnswer(UUID id) {
        repository.deleteAnswer(id);
    }
}
