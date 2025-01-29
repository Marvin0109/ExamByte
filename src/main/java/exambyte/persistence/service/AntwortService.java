package exambyte.persistence.service;

import exambyte.persistence.JDBC.repository.AntwortRepository;
import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AntwortService {

    private final AntwortRepository antwortRepository;

    public AntwortService(AntwortRepository antwortRepository) {
        this.antwortRepository = antwortRepository;
    }

    @Transactional
    public AntwortEntityJDBC saveAntwort(AntwortEntityJDBC antwort) {
        return antwortRepository.save(antwort);
    }

    public Optional<AntwortEntityJDBC> getAntwortById(Long id) {
        return antwortRepository.findById(id);
    }
}
