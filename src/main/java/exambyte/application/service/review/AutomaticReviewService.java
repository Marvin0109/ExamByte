package exambyte.application.service.review;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.ReviewService;

import java.util.List;
import java.util.UUID;

public interface AutomaticReviewService {

    List<ReviewDTO> autoReviewSC(List<FrageDTO> fragen, List<AntwortDTO> antworten,
                                         List<KorrekteAntwortenDTO> korrekteAntworten, UUID studentUUID,
                                         ReviewService reviewService);

    List<ReviewDTO> autoReviewMC(List<FrageDTO> fragen, List<AntwortDTO> antworten,
                                         List<KorrekteAntwortenDTO> answers,
                                         UUID studentUUID, ReviewService reviewService);
}
