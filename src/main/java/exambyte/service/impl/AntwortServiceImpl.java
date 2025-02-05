package exambyte.service.impl;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import exambyte.application.interfaces.AntwortService;
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

}
