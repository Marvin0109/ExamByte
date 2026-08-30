package exambyte.application.mapper;

import exambyte.application.enums.QuestionTypeDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.enums.QuestionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionDTOMapper {

    public QuestionDTO toDTO(Question question) {
        return new QuestionDTO(
                question.getId(),
                question.getText(),
                question.getPoints(),
                question.getExamId(),
                QuestionTypeDTO.valueOf(question.getType().name()));
    }

    public List<QuestionDTO> toQuestionDTOList(List<Question> questions) {
        return questions.stream()
                .map(this::toDTO)
                .toList();
    }

    public Question toDomain(QuestionDTO dto) {
        return new Question.FrageBuilder()
                .id(dto.id())
                .text(dto.text())
                .points(dto.points())
                .type(QuestionType.valueOf(dto.type().name()))
                .examId(dto.examId())
                .build();
    }
}
