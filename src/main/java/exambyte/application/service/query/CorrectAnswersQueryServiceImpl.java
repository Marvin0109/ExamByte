package exambyte.application.service.query;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
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
    public CorrectAnswersDTO getSolutionForFrage(UUID frageId) {
        CorrectAnswers k = service.findSolution(frageId);
        return k != null ? mapper.toDTO(k) : null;
    }
}
