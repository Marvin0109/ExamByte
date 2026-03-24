package exambyte.application.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.model.exam.Exam;
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
                exam.getStart(),
                exam.getEnd(),
                exam.getResult());
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
                .start(examDTO.start())
                .end(examDTO.end())
                .result(examDTO.result())
                .build();
    }
}
