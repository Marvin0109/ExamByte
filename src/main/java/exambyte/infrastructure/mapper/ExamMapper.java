package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.Exam;
import exambyte.infrastructure.entity.ExamEntity;
import org.springframework.stereotype.Component;

@Component
public class ExamMapper {

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
