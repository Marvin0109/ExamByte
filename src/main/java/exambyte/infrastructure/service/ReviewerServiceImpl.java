package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.repository.ReviewerRepository;
import exambyte.domain.service.ReviewerService;
import exambyte.infrastructure.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewerServiceImpl  implements ReviewerService {

    private final ReviewerRepository repository;

    public ReviewerServiceImpl(ReviewerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Reviewer getReviewer(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void saveReviewer(String name) {
        Reviewer reviewer = new Reviewer.ReviewerBuilder()
            .name(name)
            .build();

        repository.save(reviewer);
    }

    @Override
    public Optional<Reviewer> getReviewerByName(String name) {
        return repository.findByName(name);
    }
}
