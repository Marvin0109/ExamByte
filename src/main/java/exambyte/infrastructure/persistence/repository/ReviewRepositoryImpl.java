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
    private final SpringDataReviewRepository springDataReviewRepository;

    public ReviewRepositoryImpl(SpringDataReviewRepository springDataReviewRepository, ReviewMapper reviewMapper) {
        this.springDataReviewRepository = springDataReviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Optional<Review> findByFachId(UUID fachId) {
        Optional<ReviewEntity> entity = springDataReviewRepository.findByFachId(fachId);
        return entity.map(reviewMapper::toDomain);
    }

    @Override
    public void save(Review review) {
        ReviewEntity entity = reviewMapper.toEntity(review);
        springDataReviewRepository.save(entity);
    }

    @Override
    public Review findByAntwortFachId(UUID fachId) {
        Optional<ReviewEntity> entity = springDataReviewRepository.findByAntwortFachId(fachId);
        return entity.map(reviewMapper::toDomain)
                .orElse(null);
    }
}
