package exambyte.application.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.service.CorrectAnswersService;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviewData {

    private List<QuestionDTO> questions;
    private List<AnswerDTO> answers;
    private List<CorrectAnswersDTO> correctAnswers;
    private final CorrectAnswersDTOMapper mapper;
    private final CorrectAnswersService service;

    public ReviewData(List<QuestionDTO> questions,
                      List<AnswerDTO> answers,
                      CorrectAnswersDTOMapper mapper,
                      CorrectAnswersService service) {
        this.questions = questions;
        this.answers = answers;
        this.mapper = mapper;
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
                .map(f -> mapper.toDTO(
                        service.findSolution(f.id())))
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
