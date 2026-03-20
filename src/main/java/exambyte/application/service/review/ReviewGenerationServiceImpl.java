package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.ReviewData;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.service.CorrectAnswersService;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ReviewGenerationServiceImpl implements ReviewGenerationService {

    private final AutomaticReviewService automaticReviewService;
    private final ReviewService reviewService;
    private final CorrectAnswersService correctAnswersService;
    private final CorrectAnswersDTOMapper mapper;

    public ReviewGenerationServiceImpl(AutomaticReviewService automaticReviewService,
                                       ReviewService reviewService,
                                       CorrectAnswersService correctAnswersService,
                                       CorrectAnswersDTOMapper mapper) {
        this.automaticReviewService = automaticReviewService;
        this.reviewService = reviewService;
        this.correctAnswersService = correctAnswersService;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewDTO> generateReviews(UUID studentId, List<FrageDTO> fragen, List<AnswerDTO> answers) {
        ReviewData mcData = new ReviewData(fragen, answers,
                mapper, correctAnswersService);
        ReviewData scData = new ReviewData(fragen, answers,
                mapper, correctAnswersService);

        mcData.filterToType(QuestionTypeDTO.MC);
        scData.filterToType(QuestionTypeDTO.SC);

        List<ReviewDTO> reviewsMC = automaticReviewService.autoReviewMC(
                mcData.getFragen(), mcData.getAnswers(), mcData.getCorrectAnswers(), studentId,
                reviewService);
        List<ReviewDTO> reviewsSC = automaticReviewService.autoReviewSC(
                scData.getFragen(), scData.getAnswers(), scData.getCorrectAnswers(), studentId,
                reviewService);

        return Stream.concat(reviewsMC.stream(), reviewsSC.stream()).toList();
    }
}
