package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.mapper.AnswerDTOMapper;
import exambyte.domain.model.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final QuestionQueryService questionQueryService;
    private final AnswerRepository repository;
    private final AnswerDTOMapper mapper;

    public AnswerServiceImpl(QuestionQueryService questionQueryService,
                             AnswerRepository repository,
                             AnswerDTOMapper mapper) {
        this.questionQueryService = questionQueryService;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean saveAnswers(UUID studentId, Map<String, List<String>> answerMap) {
        for (Map.Entry<String, List<String>> entry : answerMap.entrySet()) {
            UUID questionId = UUID.fromString(entry.getKey());
            String answer;
            String replaced;
            AnswerDTO dto;

            Answer loaded = findByStudentIdAndQuestionId(studentId, questionId);

            UUID answerId = loaded != null ? loaded.getId() : null;

            QuestionDTO loadedQuestion = questionQueryService.getQuestion(questionId);
            if (!loadedQuestion.type().name().equals("FREE_RESPONSE")) {
                answer = String.join("\n", entry.getValue());
                replaced = answer.replace("ĸ", ",");
                dto = new AnswerDTO(answerId, replaced, questionId, studentId, null);
            } else {
                answer = String.join(", ", entry.getValue());
                dto = new AnswerDTO(answerId, answer, questionId, studentId, null);
            }

            addAnswer(mapper.toDomain(dto));
        }
        return true;
    }

    @Override
    public List<AnswerDTO> getAnswers(UUID studentId, Set<UUID> questionIds) {
        return questionIds.stream()
                .map(id -> findByStudentIdAndQuestionId(studentId, id))
                .filter(Objects::nonNull)
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<AnswerDTO> getFreeResponseAnswersForExam(UUID examId) {
        return questionQueryService.getFreeResponseQuestions(examId).stream()
                .map(frageDTO -> findByQuestionId(frageDTO.id()))
                .filter(Objects::nonNull)
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public AnswerDTO findByStudentAndQuestion(UUID studentId, UUID questionId) {
        if (findByStudentIdAndQuestionId(studentId, questionId) == null) {
            return null;
        }
        return mapper.toDTO(findByStudentIdAndQuestionId(studentId, questionId));
    }

    private void deleteAnswer(UUID id) {
        repository.deleteAnswer(id);
    }

    private void deleteAll() {
        repository.deleteAll();
    }

    private Answer findByQuestionId(UUID id) {
        return repository.findByQuestionId(id);
    }

    private void addAnswer(Answer answer) {
        repository.save(answer);
    }

    private Answer findByStudentIdAndQuestionId(UUID studentId, UUID questionId) {
        return repository.findByStudentIdAndQuestionId(studentId, questionId)
                .orElse(null);
    }
}
