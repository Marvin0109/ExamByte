package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.ReviewData;
import exambyte.application.service.query.CorrectAnswersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ReviewGenerationServiceImpl implements ReviewGenerationService {

    private final AutomaticReviewService automaticReviewService;
    private final CorrectAnswersService correctAnswersService;

    public ReviewGenerationServiceImpl(AutomaticReviewService automaticReviewService,
                                       CorrectAnswersService correctAnswersService) {
        this.automaticReviewService = automaticReviewService;
        this.correctAnswersService = correctAnswersService;
    }

    @Override
    public List<ReviewDTO> generateReviews(UUID studentId, List<QuestionDTO> questions, List<AnswerDTO> answers) {
        ReviewData mcData = new ReviewData(questions, answers,
               correctAnswersService);
        ReviewData scData = new ReviewData(questions, answers,
               correctAnswersService);

        mcData.filterToType(QuestionTypeDTO.MC);
        scData.filterToType(QuestionTypeDTO.SC);

        List<ReviewDTO> reviewsMC = automaticReviewService.autoReviewMC(
                mcData.getQuestions(), mcData.getAnswers(), mcData.getCorrectAnswers(), studentId);
        List<ReviewDTO> reviewsSC = automaticReviewService.autoReviewSC(
                scData.getQuestions(), scData.getAnswers(), scData.getCorrectAnswers(), studentId);

        return Stream.concat(reviewsMC.stream(), reviewsSC.stream()).toList();
    }
}
