package exambyte.application.service.query;

import exambyte.application.dto.ReviewerDTO;
import exambyte.application.mapper.ReviewerDTOMapper;
import exambyte.domain.service.ReviewerService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewerQueryServiceImpl implements ReviewerQueryService {

    private final ReviewerService service;
    private final ReviewerDTOMapper mapper;

    public ReviewerQueryServiceImpl(ReviewerService service,
                                     ReviewerDTOMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public void saveAutomaticReviewer() {
        if (service.getReviewerByName("Auto reviewer").isEmpty()) {
            service.saveReviewer("Auto reviewer");
        }
    }

    @Override
    public UUID getReviewerIdByName(String name) {
        Optional<ReviewerDTO> reviewer = service.getReviewerByName(name).map(mapper::toDTO);
        return reviewer.map(ReviewerDTO::id).orElse(null);
    }

    @Override
    public ReviewerDTO getReviewerById(UUID reviewerId) {
        return mapper.toDTO(service.getReviewer(reviewerId));
    }
}
