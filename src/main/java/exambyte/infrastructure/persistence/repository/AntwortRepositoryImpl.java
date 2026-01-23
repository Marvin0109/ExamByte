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

    private final AntwortMapper mapper;
    private final AntwortDAO dao;

    public AntwortRepositoryImpl(AntwortDAO antwortDAO, AntwortMapper antwortMapper) {
        this.dao = antwortDAO;
        this.mapper = antwortMapper;
    }

    @Override
    public Optional<Antwort> findByFachId(UUID id) {
        Optional<AntwortEntity> entity = dao.findByFachId(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public Optional<Antwort> findByStudentFachIdAndFrageFachId(UUID studentFachId, UUID fachId) {
        Optional<AntwortEntity> entity = dao
                .findByStudentFachIdAndFrageFachId(studentFachId, fachId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Antwort antwort) {
        AntwortEntity antwortEntity = mapper.toEntity(antwort);
        dao.save(antwortEntity);
        dao.updateTimestamp(antwortEntity.getFachId());
    }

    @Override
    public Antwort findByFrageFachId(UUID id) {
        Optional<AntwortEntity> entity = dao.findByFrageFachId(id);
        return entity.map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public void deleteAnswer(UUID id) {
        dao.deleteByFachId(id);
    }

    @Override
    public void updateAntwortZeitpunkt(UUID id) {
        dao.updateTimestamp(id);
    }
}
