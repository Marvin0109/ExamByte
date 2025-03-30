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

    private final ReviewMapper reviewMapper;
    private final ReviewDAO reviewDAO;

    public ReviewRepositoryImpl(ReviewDAO reviewDAO, ReviewMapper reviewMapper) {
        this.reviewDAO = reviewDAO;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Optional<Review> findByFachId(UUID fachId) {
        Optional<ReviewEntity> entity = reviewDAO.findByFachId(fachId);
        return entity.map(reviewMapper::toDomain);
    }

    @Override
    public void save(Review review) {
        ReviewEntity entity = reviewMapper.toEntity(review);
        reviewDAO.save(entity);
    }

    @Override
    public Review findByAntwortFachId(UUID fachId) {
        Optional<ReviewEntity> entity = reviewDAO.findByAntwortFachId(fachId);
        return entity.map(reviewMapper::toDomain)
                .orElse(null);
    }
}
