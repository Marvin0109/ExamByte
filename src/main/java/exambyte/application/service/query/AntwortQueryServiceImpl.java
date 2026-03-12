package exambyte.application.service.query;

import exambyte.application.dto.AntwortDTO;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.service.AntwortService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AntwortQueryServiceImpl implements AntwortQueryService {

    private final FrageQueryService frageQueryService;
    private final AntwortService antwortService;
    private final AntwortDTOMapper antwortDTOMapper;

    public AntwortQueryServiceImpl(FrageQueryService frageQueryService,
                                   AntwortService antwortService,
                                   AntwortDTOMapper antwortDTOMapper) {
        this.frageQueryService = frageQueryService;
        this.antwortService = antwortService;
        this.antwortDTOMapper = antwortDTOMapper;
    }

    @Override
    public boolean saveAnswers(UUID studentId, Map<String, List<String>> antworten) {
        for (Map.Entry<String, List<String>> entry : antworten.entrySet()) {
            UUID frageId = UUID.fromString(entry.getKey());
            String antwortText = String.join("\n", entry.getValue());
            String replaced = antwortText.replace("ĸ", ",");

            Antwort loaded = antwortService.findByStudentAndFrage(studentId, frageId);

            UUID antwortId = loaded != null ? loaded.getId() : null;

            AntwortDTO dto = new AntwortDTO(antwortId, replaced, frageId, studentId, null);
            antwortService.addAntwort(antwortDTOMapper.toDomain(dto));
        }
        return true;
    }

    @Override
    public List<AntwortDTO> getAntworten(UUID studentId, Set<UUID> frageIds) {
        return frageIds.stream()
                .map(id -> antwortService.findByStudentAndFrage(studentId, id))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }

    @Override
    public List<AntwortDTO> getFreitextAntwortenForExam(UUID examId) {
        return frageQueryService.getFreitextFragen(examId).stream()
                .map(frageDTO -> antwortService.findByFrageId(frageDTO.id()))
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
