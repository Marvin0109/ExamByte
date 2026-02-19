package exambyte.application.service.query;

import exambyte.application.dto.AntwortDTO;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.service.AntwortService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AntwortQueryServiceImpl implements AntwortQueryService {

    private final FrageQueryService frageQueryService;
    private final AntwortService antwortService;
    private final AntwortDTOMapper antwortDTOMapper;

    private static final Logger logger = Logger.getLogger(AntwortQueryServiceImpl.class.getName());

    public AntwortQueryServiceImpl(FrageQueryService frageQueryService,
                                   AntwortService antwortService,
                                   AntwortDTOMapper antwortDTOMapper) {
        this.frageQueryService = frageQueryService;
        this.antwortService = antwortService;
        this.antwortDTOMapper = antwortDTOMapper;
    }

    @Override
    public boolean saveAnswers(UUID studentId, Map<String, List<String>> antworten) {
        try {
            for (Map.Entry<String, List<String>> entry : antworten.entrySet()) {
                UUID frageFachId = UUID.fromString(entry.getKey());
                String antwortText = String.join("\n", entry.getValue());
                String replaced = antwortText.replace("ĸ", ",");
                AntwortDTO dto = new AntwortDTO(null, replaced, frageFachId, studentId, null);
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
        return frageQueryService.getFreitextFragen(examId).stream()
                .map(frageDTO -> antwortService.findByFrageFachId(frageDTO.fachId()))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }

    @Override
    public AntwortDTO findByStudentAndFrage(UUID studentId, UUID frageId) {
        if (antwortService.findByStudentAndFrage(studentId, frageId) == null) {
            return null;
        }
        return antwortDTOMapper.toDTO(antwortService.findByStudentAndFrage(studentId, frageId));
    }

    @Override
    public void deleteAntwort(UUID id) {
        antwortService.deleteAnswer(id);
    }
}
