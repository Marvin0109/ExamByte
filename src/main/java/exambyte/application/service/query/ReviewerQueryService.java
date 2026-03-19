package exambyte.application.service.query;

import exambyte.application.dto.ReviewerDTO;

import java.util.UUID;

public interface ReviewerQueryService {

    void saveAutomaticReviewer();

    UUID getReviewerIdByName(String name);

    ReviewerDTO getReviewerById(UUID reviewerId);
}
