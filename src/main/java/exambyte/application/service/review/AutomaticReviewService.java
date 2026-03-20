package exambyte.application.service.review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.ReviewService;

import java.util.List;
import java.util.UUID;

public interface AutomaticReviewService {

    List<ReviewDTO> autoReviewSC(List<FrageDTO> fragen,
                                 List<AnswerDTO> answers,
                                 List<CorrectAnswersDTO> correctAnswers,
                                 UUID studentUUID,
                                 ReviewService reviewService);

    List<ReviewDTO> autoReviewMC(List<FrageDTO> fragen,
                                 List<AnswerDTO> answers,
                                 List<CorrectAnswersDTO> correctAnswers,
                                 UUID studentUUID,
                                 ReviewService reviewService);
}
