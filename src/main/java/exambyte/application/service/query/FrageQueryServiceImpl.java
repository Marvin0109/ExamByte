package exambyte.application.service.query;

import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.FrageService;
import exambyte.domain.service.CorrectAnswersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FrageQueryServiceImpl implements FrageQueryService {

    private final FrageService frageService;
    private final CorrectAnswersService correctAnswersService;
    private final FrageDTOMapper frageDTOMapper;
    private final CorrectAnswersDTOMapper correctAnswersDTOMapper;

    public FrageQueryServiceImpl(FrageService frageService,
                                 CorrectAnswersService correctAnswersService,
                                 FrageDTOMapper frageDTOMapper,
                                 CorrectAnswersDTOMapper correctAnswersDTOMapper) {
        this.frageService = frageService;
        this.frageDTOMapper = frageDTOMapper;
        this.correctAnswersService = correctAnswersService;
        this.correctAnswersDTOMapper = correctAnswersDTOMapper;
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
        CorrectAnswersDTO dto = new CorrectAnswersDTO(null, correctAnswer, choices, frageId);
        correctAnswersService.addCorrectAnswer(correctAnswersDTOMapper.toDomain(dto));
    }

    @Override
    public String getChoiceForFrage(UUID frageId) {
        return correctAnswersService.findSolution(frageId).getChoices();
    }

    @Override
    public List<FrageDTO> getFreeResponseFragen(UUID examId) {
        List<Frage> fragen = frageService.getFragenForExam(examId);

        return fragen.stream()
                .filter(frage -> QuestionType.FREE_RESPONSE == frage.getType())
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
