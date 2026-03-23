package exambyte.application.service.query;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.exam.CorrectAnswers;
import exambyte.domain.service.CorrectAnswersService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CorrectAnswersQueryServiceImpl implements CorrectAnswersQueryService {

    private final CorrectAnswersService service;
    private final CorrectAnswersDTOMapper mapper;

    public CorrectAnswersQueryServiceImpl(CorrectAnswersService service,
                                          CorrectAnswersDTOMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public CorrectAnswersDTO getCorrectAnswerForQuestion(UUID questionId) {
        CorrectAnswers correctAnswers = service.findSolution(questionId);
        return correctAnswers != null ? mapper.toDTO(correctAnswers) : null;
    }
}
