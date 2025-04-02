package exambyte.application.service;

import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> automatischeReviewSC(List<FrageDTO> fragen, String answer);

    List<ReviewDTO> automatischeReviewMC(List<FrageDTO> fragen, List<String> answers);
}
