package exambyte.domain.service;

import exambyte.domain.model.user.Reviewer;

import java.util.Optional;
import java.util.UUID;

public interface ReviewerService {

    Reviewer getReviewer(UUID id);

    void saveReviewer(String name);

    Optional<Reviewer> getReviewerByName(String name);
}
