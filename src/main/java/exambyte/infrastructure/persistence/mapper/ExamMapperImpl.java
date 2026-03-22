package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.entitymapper.ExamMapper;
import exambyte.infrastructure.persistence.entities.ExamEntity;
import org.springframework.stereotype.Component;

@Component
public class ExamMapperImpl implements ExamMapper {

    @Override
    public Exam toDomain(ExamEntity entity) {
        return new Exam.ExamBuilder()
                .id(entity.getId())
                .title(entity.getTitle())
                .professorId(entity.getProfessorId())
                .start(entity.getStart())
                .end(entity.getEnd())
                .result(entity.getResult())
                .build();
    }

    @Override
    public ExamEntity toEntity(Exam exam) {
        return new ExamEntity.ExamEntityBuilder()
                .id(exam.getId())
                .title(exam.getTitle())
                .professorId(exam.getProfessorId())
                .start(exam.getStart())
                .end(exam.getEnd())
                .result(exam.getResult())
                .build();
    }
}
