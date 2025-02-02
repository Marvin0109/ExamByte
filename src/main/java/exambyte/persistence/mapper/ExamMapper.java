package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.persistence.entities.ExamEntity;

public class ExamMapper {

    public static Exam toDomain(ExamEntity examEntity) {
        return new Exam.ExamBuilder()
                .id(null)
                .fachId(examEntity.getFachId())
                .title(examEntity.getTitle())
                .professorFachId(examEntity.getProfessorFachId())
                .startTime(examEntity.getStartZeitpunkt())
                .endTime(examEntity.getEndZeitpunkt())
                .resultTime(examEntity.getResultZeitpunkt())
                .build();
    }

    public ExamEntity toEntity(Exam exam) {
    return new ExamEntity.ExamEntityBuilder()
            .id(null)
            .fachId(exam.getFachId())
            .title(exam.getTitle())
            .professorFachId(exam.getProfessorFachId())
            .startZeitpunkt(exam.getStartTime())
            .endZeitpunkt(exam.getEndTime())
            .resultZeitpunkt(exam.getResultTime())
            .build();
    }
}
