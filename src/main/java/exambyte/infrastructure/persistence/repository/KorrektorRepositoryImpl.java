package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.entitymapper.KorrektorMapper;
import exambyte.infrastructure.persistence.entities.KorrektorEntity;
import exambyte.domain.repository.KorrektorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class KorrektorRepositoryImpl implements KorrektorRepository {

    private final KorrektorMapper mapper;
    private final KorrektorDAO dao;

    public KorrektorRepositoryImpl(KorrektorDAO korrektorDAO,
                                   KorrektorMapper korrektorMapper) {
        this.dao = korrektorDAO;
        this.mapper = korrektorMapper;
    }

    @Override
    public Optional<Korrektor> findByFachId(UUID fachId) {
        Optional<KorrektorEntity> entity = dao.findByFachId(fachId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Korrektor korrektor) {
        KorrektorEntity entity = mapper.toEntity(korrektor);
        dao.save(entity);
    }

    @Override
    public Optional<Korrektor> findByName(String name) {
        Optional<KorrektorEntity> entity = dao.findByName(name);
        return entity.map(mapper::toDomain);
    }
}
