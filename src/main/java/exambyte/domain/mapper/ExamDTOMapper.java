package exambyte.domain.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.model.aggregate.exam.Exam;

import java.util.List;

public interface ExamDTOMapper {

    ExamDTO toDTO(Exam exam);

    List<ExamDTO> toExamDTOList(List<Exam> exams);
}
