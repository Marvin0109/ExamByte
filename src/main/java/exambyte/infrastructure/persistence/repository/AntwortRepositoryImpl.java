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
    public Optional<Antwort> findById(UUID id) {
        Optional<AntwortEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public Optional<Antwort> findByStudentIdAndFrageId(UUID studentId, UUID id) {
        Optional<AntwortEntity> entity = dao
                .findByStudentIdAndFrageId(studentId, id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Antwort antwort) {
        AntwortEntity antwortEntity = mapper.toEntity(antwort);
        dao.upsertAntwort(antwortEntity.getStudentId(),
                antwortEntity.getFrageId(),
                antwortEntity.getAntwortText());

        updateAntwortZeitpunkt(antwortEntity.getId());
    }

    @Override
    public Antwort findByFrageId(UUID id) {
        Optional<AntwortEntity> entity = dao.findByFrageId(id);
        return entity.map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public void deleteAnswer(UUID id) {
        dao.deleteById(id);
    }

    @Override
    public void updateAntwortZeitpunkt(UUID id) {
        dao.updateTimestamp(id);
    }
}
