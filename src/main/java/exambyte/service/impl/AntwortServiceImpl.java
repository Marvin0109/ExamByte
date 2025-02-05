package exambyte.service.impl;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import exambyte.service.interfaces.AntwortService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AntwortServiceImpl implements AntwortService {

    private final AntwortRepository antwortRepository;

    public AntwortServiceImpl(AntwortRepository antwortRepository) {
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
