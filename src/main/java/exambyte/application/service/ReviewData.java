package exambyte.application.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.service.query.CorrectAnswersQueryService;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviewData {

    private List<QuestionDTO> questions;
    private List<AnswerDTO> answers;
    private List<CorrectAnswersDTO> correctAnswers;
    private final CorrectAnswersQueryService service;

    public ReviewData(List<QuestionDTO> questions,
                      List<AnswerDTO> answers,
                      CorrectAnswersQueryService service) {
        this.questions = questions;
        this.answers = answers;
        this.service = service;
        this.correctAnswers = new ArrayList<>();
    }

    public void filterToType(QuestionTypeDTO type){
        questions = questions.stream()
                .filter(f -> f.type().equals(type))
                .toList();

        List<UUID> questionIds = questions.stream()
                .map(QuestionDTO::id)
                .toList();

        answers = answers.stream()
                .filter(a -> questionIds.contains(a.questionId()))
                .toList();

        correctAnswers = questions.stream()
                .map(f ->
                        service.getCorrectAnswerForQuestion(f.id()))
                .toList();
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public List<CorrectAnswersDTO> getCorrectAnswers() {
        return correctAnswers;
    }
}
