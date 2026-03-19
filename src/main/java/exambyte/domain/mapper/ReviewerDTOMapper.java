package exambyte.domain.mapper;

import exambyte.application.dto.ReviewerDTO;
import exambyte.domain.model.aggregate.user.Reviewer;

import java.util.List;

public interface ReviewerDTOMapper {

    ReviewerDTO toDTO(Reviewer reviewer);

    List<ReviewerDTO> toReviewerDTOList(List<Reviewer> reviewers);
}
