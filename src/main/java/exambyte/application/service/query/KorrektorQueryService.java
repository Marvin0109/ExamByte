package exambyte.application.service.query;

import java.util.UUID;

public interface KorrektorQueryService {

    void saveAutomaticReviewer();

    UUID getReviewerIdByName(String name);
}
