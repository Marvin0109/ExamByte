package exambyte.application.service.submission;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AnswerSubmissionService {

    boolean saveAnswers(UUID studentId, Map<String, List<String>> antworten);
}
