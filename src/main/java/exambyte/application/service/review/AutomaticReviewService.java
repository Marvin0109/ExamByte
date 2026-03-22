package exambyte.application.service.review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.ReviewService;

import java.util.List;
import java.util.UUID;

public interface AutomaticReviewService {

    List<ReviewDTO> autoReviewSC(List<QuestionDTO> fragen,
                                 List<AnswerDTO> answers,
                                 List<CorrectAnswersDTO> correctAnswers,
                                 UUID studentUUID,
                                 ReviewService reviewService);

    List<ReviewDTO> autoReviewMC(List<QuestionDTO> fragen,
                                 List<AnswerDTO> answers,
                                 List<CorrectAnswersDTO> correctAnswers,
                                 UUID studentUUID,
                                 ReviewService reviewService);
}
