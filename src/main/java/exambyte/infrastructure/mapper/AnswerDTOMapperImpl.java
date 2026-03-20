package exambyte.infrastructure.mapper;

import exambyte.application.dto.AnswerDTO;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.mapper.AnswerDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnswerDTOMapperImpl implements AnswerDTOMapper {

    @Override
    public AnswerDTO toDTO(Answer answer) {
        return new AnswerDTO(answer.getId(),
                answer.getAnswer(),
                answer.getFrageId(),
                answer.getStudentUUID(),
                answer.getSubmitTime());
    }

    @Override
    public List<AnswerDTO> toAnswerDTOList(List<Answer> answers) {
        return answers.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Answer toDomain(AnswerDTO dto) {
        return new Answer.AnswerBuilder()
            .id(dto.id())
            .answer(dto.answer())
            .frageId(dto.frageId())
            .studentId(dto.studentId())
            .submitTime(dto.submitTime())
            .build();
    }
}
