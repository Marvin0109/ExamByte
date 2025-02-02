package exambyte.persistence.repository;

import exambyte.persistence.entities.AntwortEntity;
import exambyte.service.AntwortRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AntwortRepositoryImpl implements AntwortRepository {

    private final SpringDataAntwortRepository springDataAntwortRepository;

    public AntwortRepositoryImpl(SpringDataAntwortRepository springDataAntwortRepository) {
        this.springDataAntwortRepository = springDataAntwortRepository;
    }

    @Override
    public Optional<Antwort> findByFachId(UUID id) {
        return springDataAntwortRepository.findByFachId(id);
    }

    @Override
    public void save(AntwortEntity entity) {
        springDataAntwortRepository.save(entity);
    }
}
