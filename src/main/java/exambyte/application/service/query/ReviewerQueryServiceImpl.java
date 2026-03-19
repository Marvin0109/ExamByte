package exambyte.application.service.query;

import exambyte.application.dto.ReviewerDTO;
import exambyte.domain.mapper.ReviewerDTOMapper;
import exambyte.domain.service.ReviewerService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewerQueryServiceImpl implements ReviewerQueryService {

    private final ReviewerService reviewerService;
    private final ReviewerDTOMapper reviewerDTOMapper;

    public ReviewerQueryServiceImpl(ReviewerService reviewerService,
                                     ReviewerDTOMapper reviewerDTOMapper) {
        this.reviewerService = reviewerService;
        this.reviewerDTOMapper = reviewerDTOMapper;
    }

    @Override
    public void saveAutomaticReviewer() {
        if (reviewerService.getReviewerByName("Automatischer Reviewer").isEmpty()) {
            reviewerService.saveReviewer("Automatischer Reviewer");
        }
    }

    @Override
    public UUID getReviewerIdByName(String name) {
        Optional<ReviewerDTO> k = reviewerService.getReviewerByName(name).map(reviewerDTOMapper::toDTO);
        return k.map(ReviewerDTO::id).orElse(null);
    }

    @Override
    public ReviewerDTO getReviewerById(UUID reviewerId) {
        return reviewerDTOMapper.toDTO(reviewerService.getReviewer(reviewerId));
    }
}
