package exambyte.infrastructure.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.mapper.ExamDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamDTOMapperImpl implements ExamDTOMapper {

    @Override
    public ExamDTO toDTO(Exam exam) {
        return new ExamDTO(
                exam.getId(),
                exam.getTitle(),
                exam.getProfessorId(),
                exam.getStartTime(),
                exam.getEndTime(),
                exam.getResultTime());
    }

    @Override
    public List<ExamDTO> toExamDTOList(List<Exam> exams) {
        return exams.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Exam toDomain(ExamDTO examDTO) {
        return new Exam.ExamBuilder()
                .id(examDTO.id())
                .title(examDTO.title())
                .professorId(examDTO.professorId())
                .startTime(examDTO.startTime())
                .endTime(examDTO.endTime())
                .resultTime(examDTO.resultTime())
                .build();
    }
}
