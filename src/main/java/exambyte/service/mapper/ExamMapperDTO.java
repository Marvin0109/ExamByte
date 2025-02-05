package exambyte.service.mapper;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.service.dto.ExamDTO;

public class ExamMapperDTO {

    public static ExamDTO toDTO(Exam exam) {
        return new ExamDTO(null, exam.getFachId(), exam.getTitle(), exam.getProfessorFachId(), exam.getStartTime(),
                exam.getEndTime(), exam.getResultTime());
    }

    public static Exam toDomain(ExamDTO examDTO) {
        return new Exam.ExamBuilder()
                .id(null)
                .fachId(examDTO.fachId())
                .title(examDTO.title())
                .professorFachId(examDTO.professorFachId())
                .startTime(examDTO.startTime())
                .endTime(examDTO.endTime())
                .resultTime(examDTO.resultTime())
                .build();
    }
}
