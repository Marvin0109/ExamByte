package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.AnswerDTOMapper;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.service.AnswerService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnswerQueryServiceImpl implements AnswerQueryService {

    private final FrageQueryService frageQueryService;
    private final AnswerService answerService;
    private final AnswerDTOMapper answerDTOMapper;

    public AnswerQueryServiceImpl(FrageQueryService frageQueryService,
                                   AnswerService answerService,
                                   AnswerDTOMapper answerDTOMapper) {
        this.frageQueryService = frageQueryService;
        this.answerService = answerService;
        this.answerDTOMapper = answerDTOMapper;
    }

    @Override
    public boolean saveAnswers(UUID studentId, Map<String, List<String>> answerMap) {
        for (Map.Entry<String, List<String>> entry : answerMap.entrySet()) {
            UUID frageId = UUID.fromString(entry.getKey());
            String answer;
            String replaced;
            AnswerDTO dto;

            Answer loaded = answerService.findByStudentAndFrage(studentId, frageId);

            UUID answerId = loaded != null ? loaded.getId() : null;

            FrageDTO loadedFrage = frageQueryService.getFrage(frageId);
            if (!loadedFrage.type().name().equals("FREE_RESPONSE")) {
                answer = String.join("\n", entry.getValue());
                replaced = answer.replace("ĸ", ",");
                dto = new AnswerDTO(answerId, replaced, frageId, studentId, null);
            } else {
                answer = String.join(", ", entry.getValue());
                dto = new AnswerDTO(answerId, answer, frageId, studentId, null);
            }

            answerService.addAnswer(answerDTOMapper.toDomain(dto));
        }
        return true;
    }

    @Override
    public List<AnswerDTO> getAnswers(UUID studentId, Set<UUID> frageIds) {
        return frageIds.stream()
                .map(id -> answerService.findByStudentAndFrage(studentId, id))
                .filter(Objects::nonNull)
                .map(answerDTOMapper::toDTO)
                .toList();
    }

    @Override
    public List<AnswerDTO> getFreeResponseAnswersForExam(UUID examId) {
        return frageQueryService.getFreeResponseFragen(examId).stream()
                .map(frageDTO -> answerService.findByFrageId(frageDTO.id()))
                .filter(Objects::nonNull)
                .map(answerDTOMapper::toDTO)
                .toList();
    }

    @Override
    public AnswerDTO findByStudentAndFrage(UUID studentId, UUID frageId) {
        if (answerService.findByStudentAndFrage(studentId, frageId) == null) {
            return null;
        }
        return answerDTOMapper.toDTO(answerService.findByStudentAndFrage(studentId, frageId));
    }

    @Override
    public void deleteAnswer(UUID id) {
        answerService.deleteAnswer(id);
    }
}
