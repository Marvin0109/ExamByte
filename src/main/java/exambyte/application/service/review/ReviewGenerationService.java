package exambyte.application.service.review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.ReviewDTO;

import java.util.List;
import java.util.UUID;

public interface ReviewGenerationService {

    List<ReviewDTO> generateReviews(UUID studentId, List<QuestionDTO> questions, List<AnswerDTO> answers);
}
