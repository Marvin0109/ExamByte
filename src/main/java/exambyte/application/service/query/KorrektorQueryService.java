package exambyte.application.service.query;

import exambyte.application.dto.KorrektorDTO;

import java.util.UUID;

public interface KorrektorQueryService {

    void saveAutomaticReviewer();

    UUID getReviewerIdByName(String name);

    KorrektorDTO getReviewerById(UUID reviewerId);
}
