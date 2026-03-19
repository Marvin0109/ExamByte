package exambyte.infrastructure.mapper;

import exambyte.application.dto.ReviewerDTO;
import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.mapper.ReviewerDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewerDTOMapperImpl implements ReviewerDTOMapper {

    @Override
    public ReviewerDTO toDTO(Reviewer reviewer) {
        return new ReviewerDTO(
                reviewer.id(),
                reviewer.getName());
    }

    @Override
    public List<ReviewerDTO> toReviewerDTOList(List<Reviewer> reviewers) {
        return reviewers.stream()
                .map(this::toDTO)
                .toList();
    }
}
