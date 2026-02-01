package exambyte.application.service.submission;

import exambyte.application.dto.AntwortDTO;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.service.AntwortService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AnswerSubmissionServiceImpl implements AnswerSubmissionService {

    private final AntwortService antwortService;
    private final AntwortDTOMapper antwortDTOMapper;

    private static final Logger logger = Logger.getLogger(AnswerSubmissionServiceImpl.class.getName());

    public AnswerSubmissionServiceImpl(AntwortService antwortService,
                                       AntwortDTOMapper antwortDTOMapper) {
        this.antwortService = antwortService;
        this.antwortDTOMapper = antwortDTOMapper;
    }

    @Override
    public boolean saveAnswers(UUID studentId, Map<String, List<String>> antworten) {
        try {
            for (Map.Entry<String, List<String>> entry : antworten.entrySet()) {
                UUID frageFachId = UUID.fromString(entry.getKey());
                String antwortText = String.join("\n", entry.getValue());
                AntwortDTO dto = new AntwortDTO(null, antwortText, frageFachId, studentId, null);
                antwortService.addAntwort(antwortDTOMapper.toDomain(dto));
            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Antworten", e);
            return false;
        }
    }
}
