package exambyte.infrastructure.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.mapper.ExamDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExamDTOMapperImpl implements ExamDTOMapper {

    @Override
    public ExamDTO toDTO(Exam exam) {
        return new ExamDTO(
                exam.getId(),
                exam.getFachId(),
                exam.getTitle(),
                exam.getProfessorFachId(),
                exam.getStartTime(),
                exam.getEndTime(),
                exam.getResultTime());
    }

    @Override
    public List<ExamDTO> toExamDTOList(List<Exam> exams) {
        return exams.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
