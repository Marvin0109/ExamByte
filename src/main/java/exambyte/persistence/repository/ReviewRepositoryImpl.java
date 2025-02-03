package exambyte.persistence.repository;

import exambyte.domain.aggregate.exam.Review;
import exambyte.persistence.entities.ReviewEntity;
import exambyte.persistence.mapper.ReviewMapper;
import exambyte.domain.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewMapper reviewMapper = new ReviewMapper();
    private final SpringDataReviewRepository springDataReviewRepository;

    public ReviewRepositoryImpl(SpringDataReviewRepository springDataReviewRepository) {
        this.springDataReviewRepository = springDataReviewRepository;
    }

    @Override
    public Optional<Review> findByFachId(UUID fachId) {
        Optional<ReviewEntity> entity = springDataReviewRepository.findByFachId(fachId);
        return entity.map(ReviewMapper::toDomain);
    }

    @Override
    public void save(Review review) {
        ReviewEntity entity = reviewMapper.toEntity(review);
        springDataReviewRepository.save(entity);
    }

    @Override
    public Review findByAntwortFachId(UUID fachId) {
        Optional<ReviewEntity> entity = springDataReviewRepository.findByAntwortFachId(fachId);
        return entity.map(reviewEntity -> new Review.ReviewBuilder()
                .id(null)
                .fachId(reviewEntity.getFachId())
                .antwortFachId(reviewEntity.getAntwortFachId())
                .korrektorFachId(reviewEntity.getKorrektorFachId())
                .bewertung(reviewEntity.getBewertung())
                .punkte(reviewEntity.getPunkte())
                .build()).orElse(null);
    }
}
