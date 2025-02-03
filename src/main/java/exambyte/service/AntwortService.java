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

    public UUID addAntwort(Antwort antwort) {
        antwortRepository.save(antwort);
        return antwort.getFachId();
    }
}
