package exambyte.application.service.query;

import exambyte.application.dto.ReviewerDTO;

import java.util.Optional;
import java.util.UUID;

public interface ReviewerService {

    void saveAutomaticReviewer();

    UUID getReviewerIdByName(String name);

    ReviewerDTO getReviewerById(UUID reviewerId);

    void saveReviewer(String name);

    Optional<ReviewerDTO> getReviewerByName(String name);
}
