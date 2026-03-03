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
    public Optional<Review> findById(UUID id) {
        Optional<ReviewEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Review review) {
        ReviewEntity entity = mapper.toEntity(review);
        dao.save(entity);
    }

    @Override
    public Review findByAntwortId(UUID id) {
        Optional<ReviewEntity> entity = dao.findByAntwortId(id);
        return entity.map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public void deleteReview(UUID id) {
        dao.deleteById(id);
    }
}
