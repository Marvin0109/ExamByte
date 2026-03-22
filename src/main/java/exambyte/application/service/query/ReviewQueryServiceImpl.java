package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.mapper.ReviewDTOMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewService service;
    private final ReviewDTOMapper mapper;

    public ReviewQueryServiceImpl(ReviewService service, ReviewDTOMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public UUID getReviewIdByAnswerId(UUID answerId) {
        return service.getReviewByAnswerId(answerId).getAnswerId();
    }

    @Override
    public boolean answerHasReview(UUID answerId) {
        return service.getReviewByAnswerId(answerId) != null;
    }

    @Override
    public void createReview(String text, double points, UUID answerId, UUID reviewerId) {
        Review loaded = service.getReviewByAnswerId(answerId);

        UUID reviewId = loaded != null ? loaded.getId() : null;

        Review review = mapper.toDomain(
                new ReviewDTO(
                        reviewId,
                        answerId,
                        reviewerId,
                        text,
                        points)
        );

        service.addReview(review);
    }

    @Override
    public ReviewDTO getReviewByAnswerId(UUID answerId) {
        Review review = service.getReviewByAnswerId(answerId);
        if (review != null) {
            return mapper.toDTO(review);
        }
        return null;
    }

    @Override
    public void deleteReview(UUID id) {
        service.deleteReview(id);
    }
}
