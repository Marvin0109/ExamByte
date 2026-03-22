package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.domain.mapper.AnswerDTOMapper;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.service.AnswerService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnswerQueryServiceImpl implements AnswerQueryService {

    private final QuestionQueryService questionQueryService;
    private final AnswerService answerService;
    private final AnswerDTOMapper answerDTOMapper;

    public AnswerQueryServiceImpl(QuestionQueryService questionQueryService,
                                  AnswerService answerService,
                                  AnswerDTOMapper answerDTOMapper) {
        this.questionQueryService = questionQueryService;
        this.answerService = answerService;
        this.answerDTOMapper = answerDTOMapper;
    }

    @Override
    public boolean saveAnswers(UUID studentId, Map<String, List<String>> answerMap) {
        for (Map.Entry<String, List<String>> entry : answerMap.entrySet()) {
            UUID questionId = UUID.fromString(entry.getKey());
            String answer;
            String replaced;
            AnswerDTO dto;

            Answer loaded = answerService.findByStudentIdAndQuestionId(studentId, questionId);

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

            answerService.addAnswer(answerDTOMapper.toDomain(dto));
        }
        return true;
    }

    @Override
    public List<AnswerDTO> getAnswers(UUID studentId, Set<UUID> questionIds) {
        return questionIds.stream()
                .map(id -> answerService.findByStudentIdAndQuestionId(studentId, id))
                .filter(Objects::nonNull)
                .map(answerDTOMapper::toDTO)
                .toList();
    }

    @Override
    public List<AnswerDTO> getFreeResponseAnswersForExam(UUID examId) {
        return questionQueryService.getFreeResponseQuestions(examId).stream()
                .map(frageDTO -> answerService.findByQuestionId(frageDTO.id()))
                .filter(Objects::nonNull)
                .map(answerDTOMapper::toDTO)
                .toList();
    }

    @Override
    public AnswerDTO findByStudentAndQuestion(UUID studentId, UUID questionId) {
        if (answerService.findByStudentIdAndQuestionId(studentId, questionId) == null) {
            return null;
        }
        return answerDTOMapper.toDTO(answerService.findByStudentIdAndQuestionId(studentId, questionId));
    }

    @Override
    public void deleteAnswer(UUID id) {
        answerService.deleteAnswer(id);
    }
}
