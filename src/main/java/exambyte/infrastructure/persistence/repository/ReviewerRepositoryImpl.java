package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.entitymapper.ReviewerMapper;
import exambyte.infrastructure.persistence.entities.ReviewerEntity;
import exambyte.domain.repository.ReviewerRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewerRepositoryImpl implements ReviewerRepository {

    private final ReviewerMapper mapper;
    private final ReviewerDAO dao;

    public ReviewerRepositoryImpl(ReviewerDAO dao,
                                   ReviewerMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public Optional<Reviewer> findById(UUID id) {
        Optional<ReviewerEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Reviewer reviewer) {
        ReviewerEntity entity = mapper.toEntity(reviewer);
        dao.save(entity);
    }

    @Override
    public Optional<Reviewer> findByName(String name) {
        Optional<ReviewerEntity> entity = dao.findByName(name);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
