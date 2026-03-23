package exambyte.infrastructure.repository;

import exambyte.domain.model.exam.Review;
import exambyte.application.mapper.export.mapper.ReviewMapper;
import exambyte.infrastructure.entity.ReviewEntity;
import exambyte.domain.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewMapper mapper;
    private final ReviewDAO dao;

    public ReviewRepositoryImpl(ReviewDAO dao, ReviewMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
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
    public Review findByAnswerId(UUID id) {
        Optional<ReviewEntity> entity = dao.findByAnswerId(id);
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
