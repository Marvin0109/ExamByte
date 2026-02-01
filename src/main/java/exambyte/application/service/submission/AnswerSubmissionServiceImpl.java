package exambyte.application.service.submission;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.service.question.QuestionQueryService;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.service.AntwortService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AnswerSubmissionServiceImpl implements AnswerSubmissionService {

    private final QuestionQueryService questionQueryService;
    private final AntwortService antwortService;
    private final AntwortDTOMapper antwortDTOMapper;

    private static final Logger logger = Logger.getLogger(AnswerSubmissionServiceImpl.class.getName());

    public AnswerSubmissionServiceImpl(QuestionQueryService questionQueryService,
                                       AntwortService antwortService,
                                       AntwortDTOMapper antwortDTOMapper) {
        this.questionQueryService = questionQueryService;
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

    @Override
    public List<AntwortDTO> getAntworten(UUID studentFachId, Set<UUID> frageFachIds) {
        return frageFachIds.stream()
                .map(id -> antwortService.findByStudentAndFrage(studentFachId, id))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }

    @Override
    public List<AntwortDTO> getFreitextAntwortenForExam(UUID examId) {
        return questionQueryService.getFreitextFragen(examId).stream()
                .map(frageDTO -> antwortService.findByFrageFachId(frageDTO.fachId()))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }
}
