package exambyte.application.service.query;

import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.FrageService;
import exambyte.domain.service.KorrekteAntwortenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FrageQueryServiceImpl implements FrageQueryService {

    private final FrageService frageService;
    private final KorrekteAntwortenService korrekteAntwortenService;
    private final FrageDTOMapper frageDTOMapper;
    private final KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;

    public FrageQueryServiceImpl(FrageService frageService,
                                 KorrekteAntwortenService korrekteAntwortenService,
                                 FrageDTOMapper frageDTOMapper,
                                 KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper) {
        this.frageService = frageService;
        this.frageDTOMapper = frageDTOMapper;
        this.korrekteAntwortenService = korrekteAntwortenService;
        this.korrekteAntwortenDTOMapper = korrekteAntwortenDTOMapper;
    }

    @Override
    public List<FrageDTO> getFragenForExam(UUID examId) {
        return frageDTOMapper.toFrageDTOList(frageService.getFragenForExam(examId));
    }

    @Override
    public void createFrage(FrageDTO frageDTO) {
        frageService.addFrage(frageDTOMapper.toDomain(frageDTO));
    }

    @Override
    public void createChoiceFrage(FrageDTO frageDTO, String correctAnswer, String choices) {
        UUID frageId = frageService.addFrage(frageDTOMapper.toDomain(frageDTO));
        KorrekteAntwortenDTO dto = new KorrekteAntwortenDTO(null, correctAnswer, choices, frageId);
        korrekteAntwortenService.addKorrekteAntwort(korrekteAntwortenDTOMapper.toDomain(dto));
    }

    @Override
    public String getChoiceForFrage(UUID frageId) {
        return korrekteAntwortenService.findKorrekteAntwort(frageId).getAntwortOptionen();
    }

    @Override
    public List<FrageDTO> getFreitextFragen(UUID examId) {
        List<Frage> fragen = frageService.getFragenForExam(examId);

        return fragen.stream()
                .filter(frage -> QuestionType.FREITEXT == frage.getType())
                .map(frageDTOMapper::toDTO)
                .toList();
    }

    @Override
    public Map<UUID, FrageDTO> getFragenUUIDMap(UUID examId) {
        return frageService.getFragenForExam(examId).stream()
                .map(frageDTOMapper::toDTO)
                .collect(Collectors.toMap(FrageDTO::id, f -> f));
    }

    @Override
    public FrageDTO getFrage(UUID frageId) {
        Optional<Frage> frage = frageService.getFrage(frageId);
        return frage.map(frageDTOMapper::toDTO).orElse(null);
    }
}
