package exambyte.application.mapper;

import exambyte.application.dto.ReviewerDTO;
import exambyte.domain.model.user.Reviewer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewerDTOMapper {

    public ReviewerDTO toDTO(Reviewer reviewer) {
        return new ReviewerDTO(
                reviewer.id(),
                reviewer.getName());
    }

    public List<ReviewerDTO> toReviewerDTOList(List<Reviewer> reviewers) {
        return reviewers.stream()
                .map(this::toDTO)
                .toList();
    }
}
