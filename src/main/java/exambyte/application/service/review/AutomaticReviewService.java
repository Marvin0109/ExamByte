package exambyte.application.service.review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;

import java.util.List;
import java.util.UUID;

public interface AutomaticReviewService {

    List<ReviewDTO> autoReviewSC(List<QuestionDTO> questions,
                                 List<AnswerDTO> answers,
                                 List<CorrectAnswersDTO> correctAnswers,
                                 UUID studentId);

    List<ReviewDTO> autoReviewMC(List<QuestionDTO> questions,
                                 List<AnswerDTO> answers,
                                 List<CorrectAnswersDTO> correctAnswers,
                                 UUID studentId);
}
