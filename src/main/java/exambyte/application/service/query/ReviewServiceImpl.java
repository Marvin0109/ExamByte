package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;
import exambyte.application.mapper.ReviewDTOMapper;
import exambyte.domain.model.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final ReviewDTOMapper mapper;

    public ReviewServiceImpl(ReviewRepository repository, ReviewDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean answerHasReview(UUID answerId) {
        return repository.findByAnswerId(answerId) != null;
    }

    @Override
    public void createReview(String text, double points, UUID answerId, UUID reviewerId) {
        Review loaded = repository.findByAnswerId(answerId);

        UUID reviewId = loaded != null ? loaded.getId() : null;

        Review review = mapper.toDomain(
                new ReviewDTO(
                        reviewId,
                        answerId,
                        reviewerId,
                        text,
                        points)
        );

        repository.save(review);
    }

    @Override
    public ReviewDTO getReviewByAnswerId(UUID answerId) {
        Review review = repository.findByAnswerId(answerId);
        if (review != null) {
            return mapper.toDTO(review);
        }
        return null;
    }
}
