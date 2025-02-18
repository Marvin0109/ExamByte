package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.infrastructure.persistence.entities.ExamEntity;

public interface ExamMapper {

    Exam toDomain(ExamEntity examEntity);

    ExamEntity toEntity(Exam exam);
}
