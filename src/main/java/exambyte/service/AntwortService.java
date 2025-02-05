package exambyte.service;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AntwortService {

    private final AntwortRepository antwortRepository;

    public AntwortService(AntwortRepository antwortRepository) {
        this.antwortRepository = antwortRepository;
    }

    public Antwort findByFrageFachId(UUID frageFachId) {
        return antwortRepository.findByFrageFachId(frageFachId);
    }

    public void addAntwort(Antwort antwort) {
        antwortRepository.save(antwort);
    }
    public Antwort findByStudentAndFrage(UUID studentId, UUID examId) {
        return antwortRepository.findByStudentFachIdAndFrageFachId(studentId, examId)
                .orElse(null);
    }

}
