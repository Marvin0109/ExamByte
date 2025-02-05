package exambyte.application.dto;

import exambyte.domain.aggregate.exam.Exam;

import java.util.List;
import java.util.stream.Collectors;

public class ExamDTOMapper {

    public static ExamDTO toDTO(Exam exam) {
        return new ExamDTO(
                exam.getId(),
                exam.getFachId(),
                exam.getTitle(),
                exam.getProfessorFachId(),
                exam.getStartTime(),
                exam.getEndTime(),
                exam.getResultTime());
    }

    public static List<ExamDTO> toExamDTOList(List<Exam> exams) {
        return exams.stream()
                .map(ExamDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
