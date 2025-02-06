package exambyte.domain.entitymapper;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.persistence.entities.ExamEntity;

public interface ExamMapper {

    Exam toDomain(ExamEntity examEntity);

    ExamEntity toEntity(Exam exam);
}
