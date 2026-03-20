package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AnswerQueryService {

    boolean saveAnswers(UUID studentId, Map<String, List<String>> answers);

    List<AnswerDTO> getAnswers(UUID studentId, Set<UUID> frageIds);

    List<AnswerDTO> getFreeResponseAnswersForExam(UUID examId);

    AnswerDTO findByStudentAndFrage(UUID studentId, UUID frageId);

    void deleteAnswer(UUID id);
}
