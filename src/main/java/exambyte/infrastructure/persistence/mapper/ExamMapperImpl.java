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
                .id(null)
                .fachId(entity.getFachId())
                .title(entity.getTitle())
                .professorFachId(entity.getProfessorFachId())
                .startTime(entity.getStartZeitpunkt())
                .endTime(entity.getEndZeitpunkt())
                .resultTime(entity.getResultZeitpunkt())
                .build();
    }

    @Override
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
