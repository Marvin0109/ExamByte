package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.domain.model.common.QuestionType;
import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FrageMapperImpl implements FrageMapper {

    @Override
    public Frage toDomain(FrageEntity frageEntity) {

        UUID professorFachID = frageEntity.getProfessorFachId();

        return new Frage.FrageBuilder()
                .id(null)
                .fachId(frageEntity.getFachId())
                .frageText(frageEntity.getFrageText())
                .maxPunkte(frageEntity.getMaxPunkte())
                .type(QuestionType.valueOf(frageEntity.getType().name()))
                .professorUUID(professorFachID)
                .examUUID(frageEntity.getExamFachId())
                .build();
    }

    @Override
    public FrageEntity toEntity(Frage frage) {

        UUID profUUID = frage.getProfessorUUID();

        return new FrageEntity.FrageEntityBuilder()
                .id(null)
                .fachId(frage.getFachId())
                .frageText(frage.getFrageText())
                .maxPunkte(frage.getMaxPunkte())
                .type(QuestionTypeEntity.valueOf(frage.getType().name()))
                .professorFachId(profUUID)
                .examFachId(frage.getExamUUID())
                .build();
    }
}
