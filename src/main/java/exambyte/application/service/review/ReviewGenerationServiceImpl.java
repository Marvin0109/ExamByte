package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.ReviewData;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.service.KorrekteAntwortenService;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ReviewGenerationServiceImpl implements ReviewGenerationService {

    private final AutomaticReviewService automaticReviewService;
    private final ReviewService reviewService;
    private final KorrekteAntwortenService korrekteAntwortenService;
    private final KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;

    public ReviewGenerationServiceImpl(AutomaticReviewService automaticReviewService,
                                       ReviewService reviewService,
                                       KorrekteAntwortenService korrekteAntwortenService,
                                       KorrekteAntwortenDTOMapper antwortDTOMapper) {
        this.automaticReviewService = automaticReviewService;
        this.reviewService = reviewService;
        this.korrekteAntwortenService = korrekteAntwortenService;
        this.korrekteAntwortenDTOMapper = antwortDTOMapper;
    }

    @Override
    public List<ReviewDTO> generateReviews(UUID studentId, List<FrageDTO> fragen, List<AntwortDTO> antworten) {
        ReviewData mcData = new ReviewData(fragen, antworten,
                korrekteAntwortenDTOMapper, korrekteAntwortenService);
        ReviewData scData = new ReviewData(fragen, antworten,
                korrekteAntwortenDTOMapper, korrekteAntwortenService);

        mcData.filterToType(QuestionTypeDTO.MC);
        scData.filterToType(QuestionTypeDTO.SC);

        List<ReviewDTO> reviewsMC = automaticReviewService.autoReviewMC(
                mcData.getFragen(), mcData.getAntworten(), mcData.getKorrekteAntworten(), studentId,
                reviewService);
        List<ReviewDTO> reviewsSC = automaticReviewService.autoReviewSC(
                scData.getFragen(), scData.getAntworten(), scData.getKorrekteAntworten(), studentId,
                reviewService);

        return Stream.concat(reviewsMC.stream(), reviewsSC.stream()).toList();
    }
}
