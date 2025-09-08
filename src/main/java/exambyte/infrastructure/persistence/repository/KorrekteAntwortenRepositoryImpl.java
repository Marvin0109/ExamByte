package exambyte.infrastructure.persistence.repository;

import exambyte.domain.entitymapper.KorrekteAntwortenMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.repository.KorrekteAntwortenRepository;
import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class KorrekteAntwortenRepositoryImpl implements KorrekteAntwortenRepository {

    private final KorrekteAntwortenDAO dao;
    private final KorrekteAntwortenMapper mapper;

    public KorrekteAntwortenRepositoryImpl(KorrekteAntwortenDAO dao, KorrekteAntwortenMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public List<KorrekteAntworten> findByFrageFachID(UUID frageFachID) {
        List<KorrekteAntwortenEntity> entityList = dao.findByFrageFachID(frageFachID).stream().toList();
        return entityList.stream().map(mapper::toDomain).toList();
    }

    @Override
    public void save(KorrekteAntworten antwort) {
        KorrekteAntwortenEntity entity = mapper.toEntity(antwort);
        dao.save(entity);
    }

    @Override
    public Optional<KorrekteAntworten> findByFachId(UUID fachId) {
        Optional<KorrekteAntwortenEntity> entity = dao.findByFachID(fachId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
