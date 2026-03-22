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

    private List<QuestionDTO> fragen;
    private List<AnswerDTO> answers;
    private List<CorrectAnswersDTO> correctAnswers;
    private final CorrectAnswersDTOMapper correctAnswersDTOMapper;
    private final CorrectAnswersService correctAnswersService;

    public ReviewData(List<QuestionDTO> fragen, List<AnswerDTO> answers,
                      CorrectAnswersDTOMapper correctAnswersDTOMapper,
                      CorrectAnswersService correctAnswersService) {
        this.fragen = fragen;
        this.answers = answers;
        this.correctAnswersDTOMapper = correctAnswersDTOMapper;
        this.correctAnswersService = correctAnswersService;
        this.correctAnswers = new ArrayList<>();
    }

    public void filterToType(QuestionTypeDTO type){
        fragen = fragen.stream()
                .filter(f -> f.type().equals(type))
                .toList();

        List<UUID> frageIds = fragen.stream()
                .map(QuestionDTO::id)
                .toList();

        answers = answers.stream()
                .filter(a -> frageIds.contains(a.frageId()))
                .toList();

        correctAnswers = fragen.stream()
                .map(f -> correctAnswersDTOMapper.toDTO(
                        correctAnswersService.findSolution(f.id())))
                .toList();
    }

    public List<QuestionDTO> getFragen() {
        return fragen;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public List<CorrectAnswersDTO> getCorrectAnswers() {
        return correctAnswers;
    }
}
