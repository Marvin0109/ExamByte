package exambyte.application.service.query;

import exambyte.application.dto.ReviewerDTO;
import exambyte.application.exception.NotFoundException;
import exambyte.application.mapper.ReviewerDTOMapper;
import exambyte.domain.model.user.Reviewer;
import exambyte.domain.repository.ReviewerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewerServiceImpl implements ReviewerService {

    private final ReviewerRepository repository;
    private final ReviewerDTOMapper mapper;

    public ReviewerServiceImpl(ReviewerRepository repository,
                               ReviewerDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveAutomaticReviewer() {
        if (getReviewerByUsername("Auto reviewer").isEmpty()) {
            saveReviewer("Auto reviewer");
        }
    }

    @Override
    public UUID getReviewerIdByName(String name) {
        Optional<ReviewerDTO> reviewer = getReviewerByUsername(name).map(mapper::toDTO);
        return reviewer.map(ReviewerDTO::id).orElse(null);
    }

    @Override
    public ReviewerDTO getReviewerById(UUID reviewerId) {
        return mapper.toDTO(getReviewer(reviewerId));
    }

    @Override
    public void saveReviewer(String name) {
        Reviewer reviewer = new Reviewer.ReviewerBuilder()
                .name(name)
                .build();

        repository.save(reviewer);
    }

    @Override
    public Optional<ReviewerDTO> getReviewerByName(String name) {
        Optional<Reviewer> reviewer = getReviewerByUsername(name);
        return reviewer.map(mapper::toDTO);
    }

    private Optional<Reviewer> getReviewerByUsername(String name) {
        return repository.findByName(name);
    }

    private Reviewer getReviewer(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

}
