package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.exam.Exam;
import exambyte.infrastructure.entity.ExamEntity;

public interface ExamMapper {

    Exam toDomain(ExamEntity entity);

    ExamEntity toEntity(Exam exam);
}
