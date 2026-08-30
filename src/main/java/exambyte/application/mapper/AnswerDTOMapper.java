package exambyte.application.mapper;

import exambyte.application.dto.AnswerDTO;
import exambyte.domain.model.exam.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerDTOMapper {

    public AnswerDTO toDTO(Answer answer) {
        return new AnswerDTO(answer.getId(),
                answer.getAnswer(),
                answer.getQuestionId(),
                answer.getStudentUUID(),
                answer.getSubmitTime());
    }

    public Answer toDomain(AnswerDTO dto) {
        return new Answer.AnswerBuilder()
            .id(dto.id())
            .answer(dto.answer())
            .questionId(dto.questionId())
            .studentId(dto.studentId())
            .submitTime(dto.submitTime())
            .build();
    }
}
