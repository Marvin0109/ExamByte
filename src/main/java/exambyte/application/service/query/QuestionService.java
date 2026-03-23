package exambyte.application.service.query;

import exambyte.application.dto.QuestionDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface QuestionService {

    List<QuestionDTO> getQuestionsForExam(UUID examId);

    void createQuestion(QuestionDTO questionDTO);

    void createChoiceQuestion(QuestionDTO questionDTO, String correctAnswer, String choices);

    String getChoiceForQuestion(UUID questionId);

    List<QuestionDTO> getFreeResponseQuestions(UUID examId);

    Map<UUID, QuestionDTO> getQuestionUUIDMap(UUID examId);

    QuestionDTO getQuestion(UUID questionId);
}
