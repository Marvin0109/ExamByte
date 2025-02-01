package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.persistence.entities.ExamEntity;

public class ExamMapper {

    public ExamEntity toEntity(Exam exam) {
        return new ExamEntity(
                null,
                exam.getFachId(),
                exam.getTitle(),
                exam.getProfessorFachId(),
                exam.getStartTime(),
                exam.getEndTime(),
                exam.getResultTime());
    }

    public Exam toDomain(ExamEntity examEntity) {
       return Exam.of(
                null,
                examEntity.getFachId(),
                examEntity.getTitle(),
                examEntity.getProfessorFachId(),
                examEntity.getStartZeitpunkt(),
                examEntity.getEndZeitpunkt(),
                examEntity.getResultZeitpunkt());
    }
}
