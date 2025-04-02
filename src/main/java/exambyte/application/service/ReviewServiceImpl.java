package exambyte.application.service;

import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Override
    public List<ReviewDTO> automatischeReviewSC(List<FrageDTO> fragen, String answer) {
        // TODO: SC bewerten nach submitExam()
    }

    @Override
    public List<ReviewDTO> automatischeReviewMC(List<FrageDTO> fragen, List<String> answers) {
        // TODO: MC bewerten nach submitExam()
    }
}
