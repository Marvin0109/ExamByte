package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.infrastructure.persistence.entities.AntwortEntity;
import exambyte.domain.repository.AntwortRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AntwortRepositoryImpl implements AntwortRepository {

    private final AntwortMapper antwortMapper;
    private final AntwortDAO antwortDAO;

    public AntwortRepositoryImpl(AntwortDAO antwortDAO, AntwortMapper antwortMapper) {
        this.antwortDAO = antwortDAO;
        this.antwortMapper = antwortMapper;
    }

    @Override
    public Optional<Antwort> findByFachId(UUID id) {
        Optional<AntwortEntity> entity = antwortDAO.findByFachId(id);
        return entity.map(antwortMapper::toDomain);
    }

    @Override
    public Optional<Antwort> findByStudentFachIdAndFrageFachId(UUID studentFachId, UUID fachId) {
        Optional<AntwortEntity> entity = antwortDAO
                .findByStudentFachIdAndFrageFachId(studentFachId, fachId);
        return entity.map(antwortMapper::toDomain);
    }

    @Override
    public void save(Antwort antwort) {
        AntwortEntity antwortEntity = antwortMapper.toEntity(antwort);
        antwortDAO.save(antwortEntity);
    }

    @Override
    public Antwort findByFrageFachId(UUID id) {
        Optional<AntwortEntity> entity = antwortDAO.findByFrageFachId(id);
        return entity.map(antwortMapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        antwortDAO.deleteAll();
    }

    @Override
    public void deleteAnswer(UUID id) {
        antwortDAO.deleteByFachId(id);
    }
}
