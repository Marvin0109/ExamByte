package exambyte.application.service.review;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;

import java.util.List;
import java.util.UUID;

public interface ReviewGenerationService {

    List<ReviewDTO> generateReviews(UUID studentId, List<FrageDTO> fragen, List<AntwortDTO> antworten);
}
