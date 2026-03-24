package exambyte.application.mapper;

import exambyte.application.dto.ReviewerDTO;
import exambyte.domain.model.user.Reviewer;

import java.util.List;

public interface ReviewerDTOMapper {

    ReviewerDTO toDTO(Reviewer reviewer);

    List<ReviewerDTO> toReviewerDTOList(List<Reviewer> reviewers);
}
