package exambyte.application.service.question;

import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.service.FrageService;
import exambyte.domain.service.KorrekteAntwortenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
}
