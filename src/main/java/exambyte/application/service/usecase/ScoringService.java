package exambyte.application.service.usecase;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ScoringService {

    double accumulatedPoints(List<AnswerDTO> answers, Map<UUID, QuestionDTO> questionMap, LocalDateTime result);
}
