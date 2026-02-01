package exambyte.application.service.question;

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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final FrageService frageService;
    private final KorrekteAntwortenService korrekteAntwortenService;
    private final FrageDTOMapper frageDTOMapper;
    private final KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;

    public QuestionQueryServiceImpl(FrageService frageService,
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
        UUID frageFachId = frageService.addFrage(frageDTOMapper.toDomain(frageDTO));
        KorrekteAntwortenDTO dto = new KorrekteAntwortenDTO(null, correctAnswer, choices, frageFachId);
        korrekteAntwortenService.addKorrekteAntwort(korrekteAntwortenDTOMapper.toDomain(dto));
    }

    @Override
    public String getChoiceForFrage(UUID frageId) {
        return korrekteAntwortenService.findKorrekteAntwort(frageId).getAntwortOptionen();
    }

    @Override
    public List<FrageDTO> getFreitextFragen(UUID examFachId) {
        List<Frage> fragen = frageService.getFragenForExam(examFachId);

        return fragen.stream()
                .filter(frage -> QuestionType.FREITEXT == frage.getType())
                .map(frageDTOMapper::toDTO)
                .toList();
    }

    @Override
    public Map<UUID, FrageDTO> getFragenUUIDMap(UUID examId) {
        return frageService.getFragenForExam(examId).stream()
                .map(frageDTOMapper::toDTO)
                .collect(Collectors.toMap(FrageDTO::fachId, f -> f));
    }
}
