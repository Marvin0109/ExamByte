package exambyte.persistence.repository.impl;

import exambyte.persistence.entities.ReviewEntity;
import exambyte.persistence.repository.SpringDataReviewRepository;
import exambyte.service.repository.api.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final SpringDataReviewRepository springDataReviewRepository;

    public ReviewRepositoryImpl(SpringDataReviewRepository springDataReviewRepository) {
        this.springDataReviewRepository = springDataReviewRepository;
    }

    @Override
    public Optional<ReviewEntity> findByFachId(UUID fachId) { return springDataReviewRepository.findByFachId(fachId); }

    @Override
    public void save(ReviewEntity reviewEntity) { springDataReviewRepository.save(reviewEntity); }
}
