package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import exambyte.domain.service.AntwortService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AntwortServiceImpl implements AntwortService {

    private final AntwortRepository antwortRepository;

    public AntwortServiceImpl(AntwortRepository antwortRepository) {
        this.antwortRepository = antwortRepository;
    }

    @Override
    public Antwort findByFrageFachId(UUID frageFachId) {
        if(frageFachId == null) {
            throw new IllegalArgumentException("FrageFachId darf nicht null sein");
        }
        return antwortRepository.findByFrageFachId(frageFachId);
    }

    @Override
    public void addAntwort(Antwort antwort) {
        antwortRepository.save(antwort);
    }

    @Override
    public Antwort findByStudentAndFrage(UUID studentId, UUID examId) {
        return antwortRepository.findByStudentFachIdAndFrageFachId(studentId, examId)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        antwortRepository.deleteAll();
    }

    @Override
    public void deleteAnswer(UUID id) {
        antwortRepository.deleteAnswer(id);
    }
}
