package exambyte.application.service.submission;

import exambyte.application.dto.AntwortDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AnswerSubmissionService {

    boolean saveAnswers(UUID studentId, Map<String, List<String>> antworten);

    List<AntwortDTO> getAntworten(UUID studentId, Set<UUID> frageIds);

    List<AntwortDTO> getFreitextAntwortenForExam(UUID examId);
}
