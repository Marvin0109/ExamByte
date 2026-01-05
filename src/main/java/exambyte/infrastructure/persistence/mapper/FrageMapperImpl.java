package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.domain.model.common.QuestionType;
import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.springframework.stereotype.Component;

@Component
public class FrageMapperImpl implements FrageMapper {

    @Override
    public Frage toDomain(FrageEntity entity) {

        return new Frage.FrageBuilder()
                .id(null)
                .fachId(entity.getFachId())
                .frageText(entity.getFrageText())
                .maxPunkte(entity.getMaxPunkte())
                .type(QuestionType.valueOf(entity.getType().name()))
                .professorUUID(entity.getProfessorFachId())
                .examUUID(entity.getExamFachId())
                .build();
    }

    @Override
    public FrageEntity toEntity(Frage frage) {

        return new FrageEntity.FrageEntityBuilder()
                .id(null)
                .fachId(frage.getFachId())
                .frageText(frage.getFrageText())
                .maxPunkte(frage.getMaxPunkte())
                .type(QuestionTypeEntity.valueOf(frage.getType().name()))
                .professorFachId(frage.getProfessorUUID())
                .examFachId(frage.getExamUUID())
                .build();
    }
}
