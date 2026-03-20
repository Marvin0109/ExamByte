package exambyte.application.service.query;

import exambyte.application.dto.CorrectAnswersDTO;

import java.util.UUID;

public interface CorrectAnswersQueryService {

    CorrectAnswersDTO getSolutionForFrage(UUID frageId);
}
