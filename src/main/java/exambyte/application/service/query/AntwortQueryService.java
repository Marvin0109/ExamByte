package exambyte.application.service.query;

import exambyte.application.dto.AntwortDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AntwortQueryService {

    boolean saveAnswers(UUID studentId, Map<String, List<String>> antworten);

    List<AntwortDTO> getAntworten(UUID studentId, Set<UUID> frageIds);

    List<AntwortDTO> getFreeResponseAntwortenForExam(UUID examId);

    AntwortDTO findByStudentAndFrage(UUID studentId, UUID frageId);

    void deleteAntwort(UUID id);
}
