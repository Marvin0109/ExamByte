package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.entitymapper.ReviewMapper;
import exambyte.infrastructure.persistence.entities.ReviewEntity;
import exambyte.domain.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewMapper mapper;
    private final ReviewDAO dao;

    public ReviewRepositoryImpl(ReviewDAO reviewDAO, ReviewMapper reviewMapper) {
        this.dao = reviewDAO;
        this.mapper = reviewMapper;
    }

    @Override
    public Optional<Review> findByFachId(UUID fachId) {
        Optional<ReviewEntity> entity = dao.findByFachId(fachId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Review review) {
        ReviewEntity entity = mapper.toEntity(review);
        dao.save(entity);
    }

    @Override
    public Review findByAntwortFachId(UUID fachId) {
        Optional<ReviewEntity> entity = dao.findByAntwortFachId(fachId);
        return entity.map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public void deleteReview(UUID id) {
        dao.deleteByFachId(id);
    }
}
