package exambyte.persistence.repository.impl;

import exambyte.persistence.entities.AntwortEntity;
import exambyte.persistence.repository.SpringDataAntwortRepository;
import exambyte.service.repository.api.AntwortRepository;
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
    public Optional<AntwortEntity> findByFachId(UUID id) {
        return springDataAntwortRepository.findByFachId(id);
    }

    @Override
    public void save(AntwortEntity entity) {
        springDataAntwortRepository.save(entity);
    }
}
