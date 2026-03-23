package exambyte.application.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.model.exam.Exam;

import java.util.List;

public interface ExamDTOMapper {

    ExamDTO toDTO(Exam exam);

    Exam toDomain(ExamDTO dto);

    List<ExamDTO> toExamDTOList(List<Exam> exams);
}
